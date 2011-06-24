package ttp.main;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import ttp.constructionheuristics.*;
import ttp.io.TTPProblemInstanceReader;
import ttp.localsearch.neighborhood.INeighborhood;
import ttp.localsearch.neighborhood.impl.*;
import ttp.metaheuristic.LocalSearchStatistics;
import ttp.metaheuristic.SearchStatistics;
import ttp.metaheuristic.grasp.GRASP;
import ttp.metaheuristic.tabu.TabuSearch;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TravelingTournamentProblem {
    public enum Method {
        GRASP,
        TABU
    }

    public enum Neighborhood {
        SHIFT_ROUND(new ShiftRoundNeighborhood()),
        SWAP_HOME_VISITOR(new SwapHomeVisitorNeighborhood()),
        SWAP_MATCHES(new SwapMatchesNeighborhood()),
        SWAP_MATCH_ROUND(new SwapMatchRoundNeighborhood()),
        TWO_OPT_SWAP_ROUNDS(new TwoOptSwapRoundsNeighborhood()),
        TWO_OPT_SWAP_TEAMS(new TwoOptSwapTeamsNeighborhood());

        private INeighborhood<TTPSolution> neighborhood;

        Neighborhood(INeighborhood<TTPSolution> neighborhood) {
            this.neighborhood = neighborhood;
        }

        public INeighborhood<TTPSolution> getNeighborhood() {
            return neighborhood;
        }
    }

    public enum ConstructionHeuristic {
        RANDOM(new TtpRandomConstructionHeuristic()),
        GREEDY(new SimpleConstruction()),
        GRASP(new GraspConstructionHeuristic());

        private IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristics;

        ConstructionHeuristic(IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristics) {
            this.constructionHeuristics = constructionHeuristics;
        }

        public IConstructionHeuristics<TTPInstance, TTPSolution> getConstructionHeuristics() {
            return constructionHeuristics;
        }
    }

    @Option(name = "-m", aliases = "--method", usage = "Method used for search")
    private Method method = Method.GRASP;

    @Option(name = "-n", aliases = "--neighborhoods", usage = "Neighborhoods to use")
    private List<Neighborhood> neighborhoods = new ArrayList<Neighborhood>();

    @Option(name = "-c", aliases = "--construction-heuristic",
            usage = "Construction Heuristic used to generate initial solution")
    private ConstructionHeuristic constructionHeuristic = ConstructionHeuristic.RANDOM;

    @Option(name = "-v", aliases = "--virtual-schedule-construction",
            usage = "Method used by the GRASP construction heuristic to generate a virtual schedule")
    private VirtualScheduleConstructionMethod virtualScheduleConstructionMethod =
            VirtualScheduleConstructionMethod.FIRSTPOLYGONTHENGREEK;

    @Option(name = "-t", aliases = "--tabu-list-length", usage = "Length of the tabu list")
    private int tabuListLength = 50;

    @Option(name = "-g", aliases = "--grasp-tries", usage = "Number of tries for GRASP")
    private int graspTries = 10;

    @Option(name = "-i", aliases = "--max-iterations",
            usage = "Number of iterations -in which no improvement happened- when tabu search should abort.")
    private int iterationsWithoutImprovement = 250;

    @Option(name = "-tc", aliases = "--thread-count", usage = "Number of threads to use for GRASP.")
    private int threadCount = 1;

    @Argument(required = true, index = 0, usage = "Problem instance file")
    private File instanceFile;

    @Argument(required = false, index = 1,
            usage = "Directory to write statistics to, if not set the directory of the instance " +
                    "file will be used and the directory name will be the name of the instance " +
                    "file plus _statistics")
    private File outputDirectory = null;

    private int run() throws Exception {
        if (!instanceFile.exists()) {
            System.err.println("Instance file not found!");
            return 1;
        }

        if (instanceFile.isDirectory()) {
            System.err.println("Instance file is a directory!");
            return 1;
        }

        if (tabuListLength < 0) {
            System.err.println("Tabu list length mustn't be smaller than 0.");
            return 1;
        }

        if (graspTries < 0) {
            System.err.println("Number of tries for GRASP mustn't be smaller than 0.");
            return 1;
        }

        if (iterationsWithoutImprovement < 0) {
            System.err.println("Max iterations mustn't be smaller than 0.");
            return 1;
        }

        if (threadCount < 1) {
            System.err.println("Thread count mustn't be smaller than 1.");
            return 1;
        }

        if (neighborhoods.isEmpty()) {
            neighborhoods.add(Neighborhood.TWO_OPT_SWAP_ROUNDS);
            neighborhoods.add(Neighborhood.TWO_OPT_SWAP_TEAMS);
            neighborhoods.add(Neighborhood.SWAP_HOME_VISITOR);
            neighborhoods.add(Neighborhood.SHIFT_ROUND);
            neighborhoods.add(Neighborhood.SWAP_MATCH_ROUND);
            //neighborhoods.add(Neighborhood.SWAP_MATCHES);
        }

        if (outputDirectory == null) {
            File parent = instanceFile.getParentFile();
            outputDirectory = new File(parent, instanceFile.getName() + "_statistics");
        }

        if (outputDirectory.exists()) {
            System.err.println("Output directory already exists");
            return 1;
        }

        if (!outputDirectory.mkdirs()) {
            System.err.println("Could not create output directory " + outputDirectory.getCanonicalPath());
            return 1;
        }

        System.out.println("Search-Method: " + method);
        System.out.println("Construction : " + constructionHeuristic);
        System.out.println("Virtual-Sched: " + virtualScheduleConstructionMethod);
        System.out.println("Instance     : " + instanceFile.getCanonicalPath());
        System.out.println("Output-Dir   : " + outputDirectory.getCanonicalPath());
        System.out.println("Tabu-List-Len: " + tabuListLength);
        System.out.println("GRASP-tries  : " + graspTries);
        System.out.println("Max-Iteration: " + iterationsWithoutImprovement);
        System.out.println("Thread-Count : " + threadCount);

        System.out.print("Neighborhoods  : ");
        for (Neighborhood neighborhood : neighborhoods) {
            System.out.print(neighborhood);
            System.out.print(" ");
        }

        System.out.println();

        File parameterOutFile = new File(outputDirectory, "parameters.txt");

        if (parameterOutFile.createNewFile()) {
            PrintWriter parameterWriter = new PrintWriter(parameterOutFile, "utf-8");

            parameterWriter.println("Search-Method: " + method);
            parameterWriter.println("Construction : " + constructionHeuristic);
            parameterWriter.println("Virtual-Sched: " + virtualScheduleConstructionMethod);
            parameterWriter.println("Instance     : " + instanceFile.getCanonicalPath());
            parameterWriter.println("Output-Dir   : " + outputDirectory.getCanonicalPath());
            parameterWriter.println("Tabu-List-Len: " + tabuListLength);
            parameterWriter.println("GRASP-tries  : " + graspTries);
            parameterWriter.println("Max-Iteration: " + iterationsWithoutImprovement);
            parameterWriter.println("Thread-Count : " + threadCount);

            parameterWriter.print("Neighborhoods  : ");
            for (Neighborhood neighborhood : neighborhoods) {
                parameterWriter.print(neighborhood);
                parameterWriter.print(" ");
            }

            parameterWriter.close();
        } else {
            System.err.println("failed to create parameter file " + parameterOutFile.getCanonicalPath());
        }

        TTPInstance instance = TTPProblemInstanceReader.readProblemInstance(instanceFile);

        NeighborhoodUnion<TTPSolution> unionNeighborhood = new NeighborhoodUnion<TTPSolution>();

        for (Neighborhood neighborhood : neighborhoods)
            unionNeighborhood.addNeighborhood(neighborhood.getNeighborhood());

        TabuSearch tabuSearch = new TabuSearch();
        tabuSearch.setNeighborhood(unionNeighborhood);
        tabuSearch.setTabuListLength(tabuListLength);
        tabuSearch.setMaxNoImprovement(iterationsWithoutImprovement);

        IConstructionHeuristics<TTPInstance, TTPSolution> usedConstruction =
                constructionHeuristic.getConstructionHeuristics();
        usedConstruction.setProblemInstance(instance);

        if (usedConstruction instanceof GraspConstructionHeuristic)
            ((GraspConstructionHeuristic) usedConstruction).setMethod(virtualScheduleConstructionMethod);

        TTPSolution solution = null;

        switch (method) {
            case GRASP:
                GRASP grasp = new GRASP();
                grasp.setConstructionHeuristic(usedConstruction);
                grasp.setLocalSearch(tabuSearch);
                grasp.setNoTries(graspTries);
                grasp.setNoThreads(threadCount);

                SearchStatistics searchStatistics = new SearchStatistics();
                grasp.setSearchStatistics(searchStatistics);

                solution = grasp.doSearch(instance);

                File localSearchesOutFile = new File(outputDirectory, "local_searches.csv");
                PrintWriter localSearchesWriter = null;

                if (!localSearchesOutFile.createNewFile()) {
                    System.err.println(
                            "failed to create local searches file " + localSearchesOutFile.getCanonicalPath());
                } else {
                    localSearchesWriter = new PrintWriter(localSearchesOutFile, "utf-8");
                    searchStatistics.getLocalSearchStatistics().entrySet().iterator().next().getValue()
                            .writeInformationHeader(localSearchesWriter);
                }

                for (Map.Entry<Integer, LocalSearchStatistics> localSearch : searchStatistics.getLocalSearchStatistics()
                        .entrySet()) {
                    int iteration = localSearch.getKey();
                    LocalSearchStatistics localSearchStatistics = localSearch.getValue();
                    File localSearchOutFile = new File(outputDirectory, "local_search_" + iteration + ".csv");

                    if (!localSearchOutFile.createNewFile()) {
                        System.err.println(
                                "failed to create local search statistic file " +
                                        localSearchOutFile.getCanonicalPath());
                        continue;
                    }

                    PrintWriter localSearchWriter = new PrintWriter(localSearchOutFile, "utf-8");

                    localSearchStatistics.writeIterationsHeader(localSearchWriter);
                    localSearchStatistics.writeIterations(localSearchWriter);

                    localSearchWriter.close();

                    localSearchStatistics.writeInformation(localSearchesWriter);
                }

                if (localSearchesWriter != null)
                    localSearchesWriter.close();

                File resultOutFile = new File(outputDirectory, "result.txt");

                if (resultOutFile.createNewFile()) {
                    PrintWriter resultWriter = new PrintWriter(resultOutFile, "utf-8");

                    resultWriter.println(solution.toString());
                    resultWriter.println("Cost             : " + solution.getCost());
                    resultWriter.println("Cost with penalty: " + solution.getCostWithPenalty());
                    resultWriter.println("Penalty          : " + solution.getPenalty());
                    resultWriter.println("Soft-Constraint v: " + solution.getScTotal());
                    resultWriter.println("Start            : " + searchStatistics.getStart());
                    resultWriter.println("End              : " + searchStatistics.getEnd());
                    resultWriter.println("Duration         : " + searchStatistics.getDurationInMilliSeconds() + "ms");

                    resultWriter.close();
                } else
                    System.err.println("failed to create result file " + resultOutFile.getCanonicalPath());

                break;
            case TABU:
                TTPSolution initialSolution = usedConstruction.getInitialSolution();
                LocalSearchStatistics localSearchStatistics = new LocalSearchStatistics();
                tabuSearch.setLocalSearchStatistics(localSearchStatistics);

                solution = tabuSearch.doLocalSearch(initialSolution);

                File localSearchOutFile = new File(outputDirectory, "tabu_search.csv");

                if (!localSearchOutFile.createNewFile()) {
                    System.err.println(
                            "failed to create tabu search statistic file " +
                                    localSearchOutFile.getCanonicalPath());
                } else {
                    PrintWriter localSearchWriter = new PrintWriter(localSearchOutFile, "utf-8");

                    localSearchStatistics.writeIterationsHeader(localSearchWriter);
                    localSearchStatistics.writeIterations(localSearchWriter);

                    localSearchWriter.close();
                }

                File resultOutFileTabu = new File(outputDirectory, "result.txt");

                if (resultOutFileTabu.createNewFile()) {
                    PrintWriter resultWriter = new PrintWriter(resultOutFileTabu, "utf-8");

                    resultWriter.println(solution.toString());
                    resultWriter.println("Cost             : " + solution.getCost());
                    resultWriter.println("Cost with penalty: " + solution.getCostWithPenalty());
                    resultWriter.println("Penalty          : " + solution.getPenalty());
                    resultWriter.println("Soft-Constraint v: " + solution.getScTotal());

                    localSearchStatistics.writeInformationHeader(resultWriter);
                    localSearchStatistics.writeInformation(resultWriter);

                    resultWriter.close();
                } else
                    System.err.println("failed to create result file " + resultOutFileTabu.getCanonicalPath());

                break;
            default:
                System.err.println("This shouldn't happen ;-)");
        }

        System.out.println("Result:");
        System.out.println(solution);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        TravelingTournamentProblem travelingTournamentProblem = new TravelingTournamentProblem();
        CmdLineParser parser = new CmdLineParser(travelingTournamentProblem);
        parser.setUsageWidth(80); // width of the error display area

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java ttp.main.TravelingTournamentProblem [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();
            System.exit(1);
        }

        System.exit(travelingTournamentProblem.run());
    }
}

package ttp.main;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import ttp.constructionheuristics.VirtualScheduleConstructionMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParameterExploration {

    @Option(name = "-m", aliases = "--method", usage = "Method used for search")
    private TravelingTournamentProblem.Method method = TravelingTournamentProblem.Method.GRASP;

    @Argument(required = true, index = 0, usage = "Problem instance file")
    private File instanceFile;

    @Argument(required = false, index = 1,
            usage = "Directory to write statistics to, if not set the directory of the instance " +
                    "file will be used and the directory name will be the name of the instance " +
                    "file plus _statistics")
    private File outputDirectory = null;

    private int run() throws Exception {
        List<TravelingTournamentProblem.Neighborhood> neighborhoods =
                new ArrayList<TravelingTournamentProblem.Neighborhood>();
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.TWO_OPT_SWAP_ROUNDS);
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.TWO_OPT_SWAP_TEAMS);
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.SWAP_HOME_VISITOR);
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.SHIFT_ROUND);
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.SWAP_MATCH_ROUND);
        //neighborhoods.add(Neighborhood.SWAP_MATCHES);

        File baselineOutDir = new File(outputDirectory, instanceFile.getName() + "_baseline");

        TTPParameters baseline =
                new TTPParameters(method, neighborhoods, TravelingTournamentProblem.ConstructionHeuristic.GRASP,
                        VirtualScheduleConstructionMethod.FIRSTPOLYGONTHENGREEK, 50, 10, 250, 10, instanceFile,
                        baselineOutDir);

        TravelingTournamentProblem travelingTournamentProblem = new TravelingTournamentProblem();
        travelingTournamentProblem.run(baseline);

        boolean headerWritten = false;
        File sumFile = new File(outputDirectory, instanceFile.getName() + "tabulistlen_sum.csv");
        File avgFile = new File(outputDirectory, instanceFile.getName() + "tabulistlen_avg.csv");
        File intermediateDir = new File(outputDirectory, instanceFile.getName() + "_tabulistlen");

        for (int i = 0; i <= 200; i += 10) {
            File subOutDir = new File(intermediateDir, instanceFile.getName() + "_tabulistlen_" + i);
            TTPParameters current = new TTPParameters(baseline);
            current.setTabuListLength(i);
            current.setOutputDirectory(subOutDir);
            TTPResult result = travelingTournamentProblem.run(current);

            if (!headerWritten) {
                writeResultHeader(sumFile, "tabulistlen", result);
                writeResultHeader(avgFile, "tabulistlen", result);
                headerWritten = true;
            }

            writeResultSum(sumFile, "" + i, result);
            writeResultAvg(avgFile, "" + i, result);
        }

        if (method == TravelingTournamentProblem.Method.GRASP) {
            headerWritten = false;
            sumFile = new File(outputDirectory, instanceFile.getName() + "grasptries_sum.csv");
            avgFile = new File(outputDirectory, instanceFile.getName() + "grasptries_avg.csv");
            intermediateDir = new File(outputDirectory, instanceFile.getName() + "_tabulistlen");

            for (int i = 0; i <= 100; i += 10) {
                File subOutDir = new File(intermediateDir, instanceFile.getName() + "_grasptries_" + i);
                TTPParameters current = new TTPParameters(baseline);
                current.setGraspTries(Math.max(i, 1));
                current.setThreadCount(Math.max(i, 1));
                current.setOutputDirectory(subOutDir);
                TTPResult result = travelingTournamentProblem.run(current);

                if (!headerWritten) {
                    writeResultHeader(sumFile, "grasptries", result);
                    writeResultHeader(avgFile, "grasptries", result);
                    headerWritten = true;
                }

                writeResultSum(sumFile, "" + i, result);
                writeResultAvg(avgFile, "" + i, result);
            }
        }

        headerWritten = false;
        sumFile = new File(outputDirectory, instanceFile.getName() + "noimprovment_sum.csv");
        avgFile = new File(outputDirectory, instanceFile.getName() + "noimprovment_avg.csv");
        intermediateDir = new File(outputDirectory, instanceFile.getName() + "_noimprovment");

        for (int i = 0; i <= 500; i += 50) {
            File subOutDir = new File(intermediateDir, instanceFile.getName() + "_noimprovment_" + i);
            TTPParameters current = new TTPParameters(baseline);
            current.setIterationsWithoutImprovement(i);
            current.setOutputDirectory(subOutDir);
            TTPResult result = travelingTournamentProblem.run(current);

            if (!headerWritten) {
                writeResultHeader(sumFile, "noimprovment", result);
                writeResultHeader(avgFile, "noimprovment", result);
                headerWritten = true;
            }

            writeResultSum(sumFile, "" + i, result);
            writeResultAvg(avgFile, "" + i, result);
        }

        headerWritten = false;
        sumFile = new File(outputDirectory, instanceFile.getName() + "grasptries_sum.csv");
        avgFile = new File(outputDirectory, instanceFile.getName() + "grasptries_avg.csv");
        intermediateDir = new File(outputDirectory, instanceFile.getName() + "_grasptries");

        for (TravelingTournamentProblem.ConstructionHeuristic constructionHeuristic : TravelingTournamentProblem.ConstructionHeuristic
                .values()) {
            if (constructionHeuristic == TravelingTournamentProblem.ConstructionHeuristic.GRASP) continue;

            File subOutDir = new File(intermediateDir,
                    instanceFile.getName() + "_constructioheuristic_" + constructionHeuristic);
            TTPParameters current = new TTPParameters(baseline);
            current.setConstructionHeuristic(constructionHeuristic);
            current.setOutputDirectory(subOutDir);
            TTPResult result = travelingTournamentProblem.run(current);

            if (!headerWritten) {
                writeResultHeader(sumFile, "constructioheuristic", result);
                writeResultHeader(avgFile, "constructioheuristic", result);
                headerWritten = true;
            }

            writeResultSum(sumFile, "" + constructionHeuristic, result);
            writeResultAvg(avgFile, "" + constructionHeuristic, result);
        }

        headerWritten = false;
        sumFile = new File(outputDirectory, instanceFile.getName() + "virtschedule_sum.csv");
        avgFile = new File(outputDirectory, instanceFile.getName() + "virtschedule_avg.csv");
        intermediateDir = new File(outputDirectory, instanceFile.getName() + "_virtschedule");

        for (VirtualScheduleConstructionMethod virtualScheduleConstructionMethod : VirtualScheduleConstructionMethod.values()) {
            File subOutDir = new File(intermediateDir,
                    instanceFile.getName() + "_virtschedule_" + virtualScheduleConstructionMethod);
            TTPParameters current = new TTPParameters(baseline);
            current.setVirtualScheduleConstructionMethod(virtualScheduleConstructionMethod);
            current.setOutputDirectory(subOutDir);
            TTPResult result = travelingTournamentProblem.run(current);

            if (!headerWritten) {
                writeResultHeader(sumFile, "virtschedule", result);
                writeResultHeader(avgFile, "virtschedule", result);
                headerWritten = true;
            }

            writeResultSum(sumFile, "" + virtualScheduleConstructionMethod, result);
            writeResultAvg(avgFile, "" + virtualScheduleConstructionMethod, result);
        }

        Set<TravelingTournamentProblem.Neighborhood> neighborhoodsBase = new HashSet<TravelingTournamentProblem
                .Neighborhood>(neighborhoods);
        Set<Set<TravelingTournamentProblem.Neighborhood>> neighborhoodsPowerSet = powerSet(neighborhoodsBase);

        headerWritten = false;
        sumFile = new File(outputDirectory, instanceFile.getName() + "neighborhoods_sum.csv");
        avgFile = new File(outputDirectory, instanceFile.getName() + "neighborhoods_avg.csv");
        intermediateDir = new File(outputDirectory, instanceFile.getName() + "_neighborhoods");

        for (Set<TravelingTournamentProblem.Neighborhood> neighborhoodSet : neighborhoodsPowerSet) {
            if (neighborhoodSet.size() == 0) continue;

            List<TravelingTournamentProblem.Neighborhood> currentNeighborhoods =
                    new ArrayList<TravelingTournamentProblem.Neighborhood>(neighborhoodSet);

            String neighborhoodNames = "";

            for (TravelingTournamentProblem.Neighborhood currentNeighborhood : currentNeighborhoods)
                neighborhoodNames += "_" + currentNeighborhood;

            File subOutDir = new File(intermediateDir, instanceFile.getName() + "_neighborhoods" + neighborhoodNames);
            TTPParameters current = new TTPParameters(baseline);
            current.setNeighborhoods(currentNeighborhoods);
            current.setOutputDirectory(subOutDir);
            TTPResult result = travelingTournamentProblem.run(current);

            if (!headerWritten) {
                writeResultHeader(sumFile, "neighborhoods", result);
                writeResultHeader(avgFile, "neighborhoods", result);
                headerWritten = true;
            }

            writeResultSum(sumFile, "" + neighborhoodNames, result);
            writeResultAvg(avgFile, "" + neighborhoodNames, result);
        }

        return 0;
    }

    public static void main(String[] args) throws Exception {
        ParameterExploration parameterExploration = new ParameterExploration();
        CmdLineParser parser = new CmdLineParser(parameterExploration);
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

        System.exit(parameterExploration.run());
    }

    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    private static void writeResultHeader(File outputFile, String prefix, TTPResult result) throws IOException {
        if (!outputFile.exists()) {
            if (!outputFile.createNewFile()) {
                System.err.println("failed to create result file " + outputFile.getCanonicalPath());
                return;
            }
        }

        PrintWriter writer = new PrintWriter(outputFile, "utf-8");

        writer.print(prefix);
        writer.print(',');

        if (result.getLocalSearchStatistics() != null) {
            result.getLocalSearchStatistics().writeInformationHeader(writer);
        } else if (result.getSearchStatistics() != null) {
            result.getSearchStatistics().writeStatisticsHeader(writer);
        }

        writer.close();
    }

    private static void writeResultSum(File outputFile, String prefix, TTPResult result) throws FileNotFoundException {
        if (!outputFile.exists()) return;

        FileOutputStream fso = new FileOutputStream(outputFile, true);
        PrintWriter writer = new PrintWriter(fso);

        writer.print(prefix);
        writer.print(',');

        if (result.getLocalSearchStatistics() != null) {
            result.getLocalSearchStatistics().writeInformation(writer);
        } else if (result.getSearchStatistics() != null) {
            result.getSearchStatistics().writeStatisticSum(writer);
        }

        writer.close();
    }

    private static void writeResultAvg(File outputFile, String prefix, TTPResult result) throws FileNotFoundException {
        if (!outputFile.exists()) return;

        FileOutputStream fso = new FileOutputStream(outputFile, true);
        PrintWriter writer = new PrintWriter(fso);

        writer.print(prefix);
        writer.print(',');

        if (result.getLocalSearchStatistics() != null) {
            result.getLocalSearchStatistics().writeInformation(writer);
        } else if (result.getSearchStatistics() != null) {
            result.getSearchStatistics().writeStatisticAverage(writer);
        }

        writer.close();
    }
}

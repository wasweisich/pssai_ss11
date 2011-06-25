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
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TravelingTournamentProblem {
	public enum Method {
		GRASP, TABU
	}

	public enum Neighborhood {
		SHIFT_ROUND(new ShiftRoundNeighborhood()), SWAP_HOME_VISITOR(
				new SwapHomeVisitorNeighborhood()), SWAP_MATCHES(
				new SwapMatchesNeighborhood()), SWAP_MATCH_ROUND(
				new SwapMatchRoundNeighborhood()), TWO_OPT_SWAP_ROUNDS(
				new TwoOptSwapRoundsNeighborhood()), TWO_OPT_SWAP_TEAMS(
				new TwoOptSwapTeamsNeighborhood());

		private INeighborhood<TTPSolution> neighborhood;

		Neighborhood(INeighborhood<TTPSolution> neighborhood) {
			this.neighborhood = neighborhood;
		}

		public INeighborhood<TTPSolution> getNeighborhood() {
			return neighborhood;
		}
	}

	public enum ConstructionHeuristic {
		RANDOM(new TtpRandomConstructionHeuristic()), GREEDY(
				new SimpleConstruction()), GRASP(
				new GraspConstructionHeuristic());

		private IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristics;

		ConstructionHeuristic(
				IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristics) {
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

	@Option(name = "-c", aliases = "--construction-heuristic", usage = "Construction Heuristic used to generate initial solution")
	private ConstructionHeuristic constructionHeuristic = ConstructionHeuristic.RANDOM;

	@Option(name = "-v", aliases = "--virtual-schedule-construction", usage = "Method used by the GRASP construction heuristic to generate a virtual schedule")
	private VirtualScheduleConstructionMethod virtualScheduleConstructionMethod = VirtualScheduleConstructionMethod.FIRSTPOLYGONTHENGREEK;

	@Option(name = "-t", aliases = "--tabu-list-length", usage = "Length of the tabu list")
	private int tabuListLength = 50;

	@Option(name = "-g", aliases = "--grasp-tries", usage = "Number of tries for GRASP")
	private int graspTries = 10;

	@Option(name = "-d", aliases = "--max-duration", usage = "Maximal duration for the search")
	private long maxDuration = Long.MAX_VALUE;

	@Option(name = "-i", aliases = "--max-iterations", usage = "Number of iterations -in which no improvement happened- when tabu search should abort.")
	private int iterationsWithoutImprovement = 250;

	@Option(name = "-tc", aliases = "--thread-count", usage = "Number of threads to use for GRASP.")
	private int threadCount = 1;

	@Argument(required = true, index = 0, usage = "Problem instance file")
	private File instanceFile;

	@Argument(required = false, index = 1, usage = "Directory to write statistics to, if not set the directory of the instance "
			+ "file will be used and the directory name will be the name of the instance "
			+ "file plus _statistics")
	private File outputDirectory = null;

	private TTPResult run() throws Exception {
		return run(new TTPParameters(method, neighborhoods,
				constructionHeuristic, virtualScheduleConstructionMethod,
				tabuListLength, graspTries, iterationsWithoutImprovement,
				threadCount, instanceFile, outputDirectory, maxDuration));
	}

	public TTPResult run(TTPParameters parameters) throws Exception {
		if (!parameters.getInstanceFile().exists()) {
			System.err.println("Instance file not found!");
			return null;
		}

		if (parameters.getInstanceFile().isDirectory()) {
			System.err.println("Instance file is a directory!");
			return null;
		}

		if (parameters.getTabuListLength() < 0) {
			System.err.println("Tabu list length mustn't be smaller than 0.");
			return null;
		}

		if (parameters.getMaxDuration() < 0) {
			System.err.println("MaxDuration mustn't be smaller than 0.");
			return null;
		}

		if (parameters.getGraspTries() < 0) {
			System.err
					.println("Number of tries for GRASP mustn't be smaller than 0.");
			return null;
		}

		if (parameters.getIterationsWithoutImprovement() < 0) {
			System.err.println("Max iterations mustn't be smaller than 0.");
			return null;
		}

		if (parameters.getThreadCount() < 1) {
			System.err.println("Thread count mustn't be smaller than 1.");
			return null;
		}

		if (parameters.getNeighborhoods().isEmpty()) {
			parameters.getNeighborhoods().add(Neighborhood.TWO_OPT_SWAP_ROUNDS);
			parameters.getNeighborhoods().add(Neighborhood.TWO_OPT_SWAP_TEAMS);
			parameters.getNeighborhoods().add(Neighborhood.SWAP_HOME_VISITOR);
			parameters.getNeighborhoods().add(Neighborhood.SHIFT_ROUND);
			parameters.getNeighborhoods().add(Neighborhood.SWAP_MATCH_ROUND);
			parameters.getNeighborhoods().add(Neighborhood.SWAP_MATCHES);
		}

		if (parameters.getOutputDirectory() == null) {
			File parent = parameters.getInstanceFile().getParentFile();
			parameters.setOutputDirectory(new File(parent, parameters
					.getInstanceFile().getName() + "_statistics"));
			if (parameters.getOutputDirectory().exists()) {
				parameters.setOutputDirectory(new File(parent, parameters
						.getInstanceFile().getName() + "_statistics"));
				int i = 2;
				while (parameters.getOutputDirectory().exists()) {
					parameters.setOutputDirectory(new File(parent, parameters
							.getInstanceFile().getName()
							+ "_statistics("
							+ i
							+ ")"));
					i++;
				}
			}

		}

		if (!parameters.getOutputDirectory().mkdirs()) {
			System.err.println("Could not create output directory "
					+ parameters.getOutputDirectory().getCanonicalPath());
			return null;
		}

		System.out.println("Search-Method: " + parameters.getMethod());
		System.out.println("Construction : "
				+ parameters.getConstructionHeuristic());
		System.out.println("Virtual-Sched: "
				+ parameters.getVirtualScheduleConstructionMethod());
		System.out.println("Instance     : "
				+ parameters.getInstanceFile().getCanonicalPath());
		System.out.println("Output-Dir   : "
				+ parameters.getOutputDirectory().getCanonicalPath());
		System.out.println("Tabu-List-Len: " + parameters.getTabuListLength());
		System.out.println("GRASP-tries  : " + parameters.getGraspTries());
		System.out.println("Max-Iteration: "
				+ parameters.getIterationsWithoutImprovement());
		System.out.println("Thread-Count : " + parameters.getThreadCount());
		
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss, EEE, d MMM yyyy ");
		
		System.out.println("Start time : " + formater.format(new Date()));
		
		if(parameters.getMaxDuration() != Long.MAX_VALUE)
		{
			formater = new SimpleDateFormat("HH 'Hours,' mm 'Minutes,' ss 'Seconds'");
			
		System.out.println("Use Time Limit : yes");
		System.out.println("Time Limit : " + formater.format(new Date(maxDuration)));
		}
		else
		{
			System.out.println("Use Time Limit : no");
		}

		System.out.print("Neighborhoods  : ");
		for (Neighborhood neighborhood : parameters.getNeighborhoods()) {
			System.out.print(neighborhood);
			System.out.print(" ");
		}

		System.out.println();

		writeParametersFile(parameters.getConstructionHeuristic(),
				parameters.getGraspTries(), parameters.getInstanceFile(),
				parameters.getIterationsWithoutImprovement(),
				parameters.getMethod(), parameters.getNeighborhoods(),
				parameters.getOutputDirectory(),
				parameters.getTabuListLength(), parameters.getThreadCount(),
				parameters.getVirtualScheduleConstructionMethod());

		TTPInstance instance = TTPProblemInstanceReader
				.readProblemInstance(parameters.getInstanceFile());

		NeighborhoodUnion<TTPSolution> unionNeighborhood = new NeighborhoodUnion<TTPSolution>();

		for (Neighborhood neighborhood : parameters.getNeighborhoods())
			unionNeighborhood.addNeighborhood(neighborhood.getNeighborhood());

		TabuSearch tabuSearch = new TabuSearch();
		tabuSearch.setNeighborhood(unionNeighborhood);
		tabuSearch.setTabuListLength(parameters.getTabuListLength());
		tabuSearch.setMaxNoImprovement(parameters
				.getIterationsWithoutImprovement());
		if (maxDuration != Long.MAX_VALUE) {
			Date finishTime = new Date();
			finishTime.setTime(finishTime.getTime() + maxDuration);
			tabuSearch.setLastFinishTime(finishTime);
			tabuSearch.setUseTimeLimit(true);
		} else {
			tabuSearch.setUseTimeLimit(false);
		}

		IConstructionHeuristics<TTPInstance, TTPSolution> usedConstruction = parameters
				.getConstructionHeuristic().getConstructionHeuristics();
		usedConstruction.setProblemInstance(instance);

		if (usedConstruction instanceof GraspConstructionHeuristic)
			((GraspConstructionHeuristic) usedConstruction)
					.setMethod(parameters
							.getVirtualScheduleConstructionMethod());

		TTPSolution solution = null;

		switch (parameters.getMethod()) {
		case GRASP:
			GRASP grasp = new GRASP();
			grasp.setConstructionHeuristic(usedConstruction);
			grasp.setLocalSearch(tabuSearch);
			grasp.setNoTries(parameters.getGraspTries());
			grasp.setNoThreads(parameters.getThreadCount());

			SearchStatistics searchStatistics = new SearchStatistics();
			grasp.setSearchStatistics(searchStatistics);

			solution = grasp.doSearch(instance);

			writeGraspIterations(searchStatistics,
					parameters.getOutputDirectory());
			writeResultsFile(solution, searchStatistics, null,
					parameters.getOutputDirectory());

			System.out.println("TTPResult:");
			System.out.println(solution);

			return new TTPResult(solution, null, searchStatistics);
		case TABU:
			TTPSolution initialSolution = usedConstruction.getInitialSolution();
			LocalSearchStatistics localSearchStatistics = new LocalSearchStatistics();
			tabuSearch.setLocalSearchStatistics(localSearchStatistics);

			solution = tabuSearch.doLocalSearch(initialSolution);

			writeLocalSearchIteration(0, localSearchStatistics,
					parameters.getOutputDirectory());
			writeResultsFile(solution, null, localSearchStatistics,
					parameters.getOutputDirectory());

			System.out.println("TTPResult:");
			System.out.println(solution);

			return new TTPResult(solution, localSearchStatistics, null);
		default:
			System.err.println("This shouldn't happen ;-)");
		}

		return null;
	}

	private static void writeGraspIterations(SearchStatistics searchStatistics,
			final File outputDirectory) throws IOException {
		File localSearchesOutFile = new File(outputDirectory,
				"local_searches.csv");
		PrintWriter localSearchesWriter = null;

		if (searchStatistics.getLocalSearchStatistics().size() > 0)
			if (!localSearchesOutFile.createNewFile()) {
				System.err.println("failed to create local searches file "
						+ localSearchesOutFile.getCanonicalPath());
			} else {
				localSearchesWriter = new PrintWriter(localSearchesOutFile,
						"utf-8");
				searchStatistics.getLocalSearchStatistics().entrySet()
						.iterator().next().getValue()
						.writeInformationHeader(localSearchesWriter);
			}
		else
			return;

		for (Map.Entry<Integer, LocalSearchStatistics> localSearch : searchStatistics
				.getLocalSearchStatistics().entrySet()) {
			int iteration = localSearch.getKey();
			LocalSearchStatistics localSearchStatistics = localSearch
					.getValue();
			writeLocalSearchIteration(iteration, localSearchStatistics,
					outputDirectory);

			localSearchStatistics.writeInformation(localSearchesWriter);
		}

		if (localSearchesWriter != null)
			localSearchesWriter.close();
	}

	private static void writeResultsFile(TTPSolution solution,
			SearchStatistics searchStatistics,
			LocalSearchStatistics localSearchStatistics,
			final File outputDirectory) throws IOException {
		File resultOutFile = new File(outputDirectory, "result.txt");

		if (solution == null)
			return;

		if (resultOutFile.createNewFile()) {
			PrintWriter resultWriter = new PrintWriter(resultOutFile, "utf-8");

			resultWriter.println(solution.toString());
			resultWriter.println("Cost             : " + solution.getCost());
			resultWriter.println("Cost with penalty: "
					+ solution.getCostWithPenalty());
			resultWriter.println("Penalty          : " + solution.getPenalty());
			resultWriter.println("Soft-Constraint v: " + solution.getScTotal());

			if (searchStatistics != null) {
				resultWriter.println("Start            : "
						+ searchStatistics.getStart());
				resultWriter.println("End              : "
						+ searchStatistics.getEnd());
				resultWriter.println("Duration         : "
						+ searchStatistics.getDurationInMilliSeconds() + "ms");

				resultWriter.println("Sum:");
				searchStatistics.writeStatisticSum(resultWriter);

				resultWriter.println("Average:");
				searchStatistics.writeStatisticAverage(resultWriter);
			}

			if (localSearchStatistics != null) {
				localSearchStatistics.writeInformationHeader(resultWriter);
				localSearchStatistics.writeInformation(resultWriter);
			}

			resultWriter.close();
		} else
			System.err.println("failed to create result file "
					+ resultOutFile.getCanonicalPath());
	}

	private static boolean writeLocalSearchIteration(int iteration,
			LocalSearchStatistics localSearchStatistics,
			final File outputDirectory) throws IOException {
		File localSearchOutFile = new File(outputDirectory, "local_search_"
				+ iteration + ".csv");

		if (!localSearchOutFile.createNewFile()) {
			System.err.println("failed to create local search statistic file "
					+ localSearchOutFile.getCanonicalPath());
			return false;
		}

		PrintWriter localSearchWriter = new PrintWriter(localSearchOutFile,
				"utf-8");

		localSearchStatistics.writeIterationsHeader(localSearchWriter);
		localSearchStatistics.writeIterations(localSearchWriter);

		localSearchWriter.close();

		return true;
	}

	private static void writeParametersFile(
			ConstructionHeuristic constructionHeuristic, int graspTries,
			File instanceFile, int iterationsWithoutImprovement, Method method,
			List<Neighborhood> neighborhoods, File outputDirectory,
			int tabuListLength, int threadCount,
			VirtualScheduleConstructionMethod virtualScheduleConstructionMethod)
			throws IOException {
		File parameterOutFile = new File(outputDirectory, "parameters.txt");

		if (parameterOutFile.createNewFile()) {
			PrintWriter parameterWriter = new PrintWriter(parameterOutFile,
					"utf-8");

			parameterWriter.println("Search-Method: " + method);
			parameterWriter.println("Construction : " + constructionHeuristic);
			parameterWriter.println("Virtual-Sched: "
					+ virtualScheduleConstructionMethod);
			parameterWriter.println("Instance     : "
					+ instanceFile.getCanonicalPath());
			parameterWriter.println("Output-Dir   : "
					+ outputDirectory.getCanonicalPath());
			parameterWriter.println("Tabu-List-Len: " + tabuListLength);
			parameterWriter.println("GRASP-tries  : " + graspTries);
			parameterWriter.println("Max-Iteration: "
					+ iterationsWithoutImprovement);
			parameterWriter.println("Thread-Count : " + threadCount);

			parameterWriter.print("Neighborhoods  : ");
			for (Neighborhood neighborhood : neighborhoods) {
				parameterWriter.print(neighborhood);
				parameterWriter.print(" ");
			}

			parameterWriter.close();
		} else {
			System.err.println("failed to create parameter file "
					+ parameterOutFile.getCanonicalPath());
		}
	}

	public static void main(String[] args) throws Exception {
		TravelingTournamentProblem travelingTournamentProblem = new TravelingTournamentProblem();
		CmdLineParser parser = new CmdLineParser(travelingTournamentProblem);
		parser.setUsageWidth(80); // width of the error display area

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err
					.println("java ttp.main.TravelingTournamentProblem [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();
			System.exit(1);
		}

		TTPResult result = travelingTournamentProblem.run();

		if (result == null)
			System.exit(1);
	}

}

package ttp.main;

import java.io.File;

import org.apache.log4j.Logger;

import ttp.constructionheuristics.GraspConstructionHeuristic;
import ttp.io.TTPProblemInstanceReader;
import ttp.localsearch.neighborhood.impl.NeighborhoodUnion;
import ttp.localsearch.neighborhood.impl.ShiftRoundNeighborhood;
import ttp.localsearch.neighborhood.impl.SwapHomeVisitorNeighborhood;
import ttp.localsearch.neighborhood.impl.SwapMatchRoundNeighborhood;
import ttp.localsearch.neighborhood.impl.TwoOptSwapRoundsNeighborhood;
import ttp.localsearch.neighborhood.impl.TwoOptSwapTeamsNeighborhood;
import ttp.metaheuristic.ISearch;
import ttp.metaheuristic.grasp.GRASP;
import ttp.metaheuristic.tabu.TabuSearch;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

public class Main {

	private static final String METHOD = "-method";
	private static final String GRASP = "grasp";
	private static final String TABU = "tabu";
	private static final String NEIGHBORHOOD = "-n";
	private static final String SWAPHOME = "sh";
	private static final String SWAPTEAM = "st";
	private static final String SWAPROUND = "sr";
	private static final String SWAPPARTIALROUND = "spr";
	private static final String SWAPPARTIALTEAM = "spt";
	private static final String SHIFTROUNDS = "shr";
	private static final String CONSTRUCTIONHEURISTIC = "-constHeu";
	private static final String CONSTR_HEUR_RANDOM = "random";
	private static final String GREEDY = "greedy";
	private static final String NOTHREADS = "-noThreads";
	private static Logger logger = Logger.getLogger(Main.class);;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ISearch<TTPInstance, TTPSolution> search = new GRASP();
		boolean multithreading = false;
		int noThreads = 1;
		String path = "";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-i")) {
				path = args[i + 1];
				i++;
			}

			if (args[i].equals(METHOD)) {
				String method = args[i + 1];
				i++;

				if (method.equals(GRASP)) {
					GRASP grasp = new GRASP();
					grasp.setConstructionHeuristic(new GraspConstructionHeuristic());

					TabuSearch taubSearch = new TabuSearch();

					NeighborhoodUnion<TTPSolution> neighborhood = new NeighborhoodUnion<TTPSolution>();

					neighborhood
							.addNeighborhood(new TwoOptSwapRoundsNeighborhood());
					neighborhood
							.addNeighborhood(new TwoOptSwapTeamsNeighborhood());
					neighborhood
							.addNeighborhood(new SwapHomeVisitorNeighborhood());
					neighborhood.addNeighborhood(new ShiftRoundNeighborhood());
					neighborhood
							.addNeighborhood(new SwapMatchRoundNeighborhood());

					taubSearch.setNeighborhood(neighborhood);

					taubSearch.setTabuListLength(50);
					grasp.setLocalSearch(taubSearch);

					grasp.setNoTries(10);

					search = grasp;
				} else

				if (method.equals(TABU)) {

				} else {
					logger.error("illigal parameter for argument " + METHOD);
				}
			}

			if (args[i].equals(NEIGHBORHOOD)) {
				String n = args[i + 1];
				i++;

				String[] neighborhoods = n.split(",");
				for (String s : neighborhoods) {
					if (s.equals(SWAPHOME)) {

					} else if (s.equals(SWAPTEAM)) {

					}
					if (s.equals(SWAPROUND)) {

					}
					if (s.equals(SWAPPARTIALROUND)) {

					}
					if (s.equals(SWAPPARTIALTEAM)) {

					}
					if (s.equals(SHIFTROUNDS)) {

					} else {
						logger.error("unknown neighborhood " + s);
					}
				}

			}

			if (args[i].equals(CONSTRUCTIONHEURISTIC)) {
				String constHeuStr = args[i + 1];
				i++;
				if (constHeuStr.equals(CONSTR_HEUR_RANDOM)) {

				} else if (constHeuStr.equals(GREEDY)) {

				} else {
					logger.error("unknown construction heuristic "
							+ constHeuStr);
				}
			}

			if (args[i].equals(NOTHREADS)) {
				String no = args[i + 1];
				i++;
				multithreading = true;
				noThreads = Integer.parseInt(no);
			}

		}

		File file = new File(path);
		if (!file.exists()) {
			logger.error("File does not exist");
			System.exit(1);
		}

		if (search == null) {
			logger.error("No search specified");
		}

		try {
			TTPInstance instance = TTPProblemInstanceReader
					.readProblemInstance(file);

			TTPSolution result = search.doSearch(instance);

			logger.info("Result: ");
			logger.info(result.toString());

		} catch (Exception e) {
			logger.error("Error during search", e);
		}
	}

}

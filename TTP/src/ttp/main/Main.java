package ttp.main;

import java.io.File;

import org.apache.log4j.Logger;

import ttp.constructionheuristics.SimpleConstruction;
import ttp.io.TTPProblemInstanceReader;
import ttp.localsearch.neighborhood.ILocalSearch;
import ttp.metaheuristic.ISearch;
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
	private static final Object CONSTRUCTIONHEURISTIC = null;
	private static final Object CONSTR_HEUR_RANDOM = null;
	private static final Object GREEDY = null;
	private static final Object NOTHREADS = null;
	private static Logger logger = Logger.getLogger(Main.class);;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ISearch<TTPInstance, TTPSolution> search = null;
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
					search = new ttp.metaheuristic.GRASP();
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

package ttp.main;

import java.io.File;

import org.apache.log4j.Logger;

import ttp.constructionheuristics.SimpleConstruction;
import ttp.io.TTPProblemInstanceReader;
import ttp.localsearch.neighborhood.ILocalSearch;
import ttp.metaheuristic.tabu.TabuSearch;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class);;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String path = "";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-i")) {
				path = args[i + 1];
				i++;
			}
		}

		File file = new File(path);
		if (!file.exists()) {
			logger.error("File does not exist");
			System.exit(1);
		}

		ILocalSearch<TTPSolution> localSearch = new TabuSearch();

		try {
			TTPInstance instance = TTPProblemInstanceReader
					.readProblemInstance(file);

			SimpleConstruction construction = new SimpleConstruction();
			construction.setProblemInstance(instance);

			TTPSolution initialSolution = construction.getInitialSolution();
			logger.info("Initial Solution: ");
			logger.info(initialSolution.toString());

			TTPSolution result = localSearch.doLocalSearch(initialSolution);

			logger.info("Result: ");

			logger.info(result.toString());

		} catch (Exception e) {
			logger.error("Error during search", e);
		}
	}

}

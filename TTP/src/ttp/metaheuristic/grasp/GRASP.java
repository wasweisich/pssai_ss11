package ttp.metaheuristic.grasp;

import org.apache.log4j.Logger;
import ttp.constructionheuristics.IConstructionHeuristics;
import ttp.metaheuristic.ILocalSearch;
import ttp.metaheuristic.ISearch;
import ttp.metaheuristic.LocalSearchStatistics;
import ttp.metaheuristic.SearchStatistics;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GRASP implements ISearch<TTPInstance, TTPSolution> {
	private static Logger logger = Logger.getLogger(GRASP.class);

	private ILocalSearch<TTPSolution> localSearch;
	private IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristic;
	private SearchStatistics searchStatistics;
	private int noTries = 10;
	private int noThreads = 1;

	public ILocalSearch<TTPSolution> getLocalSearch() {
		return localSearch;
	}

	public void setLocalSearch(ILocalSearch<TTPSolution> localSearch) {
		this.localSearch = localSearch;
	}

	public IConstructionHeuristics<TTPInstance, TTPSolution> getConstructionHeuristic() {
		return constructionHeuristic;
	}

	public void setConstructionHeuristic(
			IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristic) {
		this.constructionHeuristic = constructionHeuristic;
	}

	public int getNoTries() {
		return noTries;
	}

	public void setNoTries(int noTries) {
		this.noTries = noTries;
	}

	public int getNoThreads() {
		return noThreads;
	}

	public void setNoThreads(int noThreads) {
		this.noThreads = noThreads;
	}

	public SearchStatistics getSearchStatistics() {
		return searchStatistics;
	}

	public void setSearchStatistics(SearchStatistics searchStatistics) {
		this.searchStatistics = searchStatistics;
	}

	@Override
	public TTPSolution doSearch(TTPInstance instance) {
		if (searchStatistics != null)
			searchStatistics.start();

		ExecutorService executorService = Executors
				.newFixedThreadPool(noThreads);
		List<LocalSearchCallable> localSearches = new ArrayList<LocalSearchCallable>(
				noTries);
		constructionHeuristic.setProblemInstance(instance);

		try {
			for (int i = 0; i < noTries; i++) {
				IConstructionHeuristics<TTPInstance, TTPSolution> clonedConstructionHeuristic = constructionHeuristic
						.clone();
				ILocalSearch<TTPSolution> clonedLocalSearch = localSearch
						.clone();

				localSearches.add(new LocalSearchCallable(
						clonedConstructionHeuristic, clonedLocalSearch, i,
						searchStatistics != null));
			}
		} catch (CloneNotSupportedException e) {
			logger.error(
					"Failed to create clone for multi-threaded local search", e);
		}

		try {
			List<Future<LocalSearchResult>> solutions = executorService
					.invokeAll(localSearches);
			TTPSolution bestSolution = null;

			for (Future<LocalSearchResult> futureSolution : solutions) {
				try {
					LocalSearchResult result = futureSolution.get();

					TTPSolution solution = result.getSolution();

					if (solution == null) {
						logger.warn("Solution was null");
						continue;
					}

					if (searchStatistics != null)
						searchStatistics.addLocalSearchStatistic(
								result.getIteration(), result.getStatistics());

					if (solution != null
							&& (bestSolution == null || solution.getCost() < bestSolution
									.getCost()))
						bestSolution = solution;
				} catch (ExecutionException e) {
					logger.warn("One of the local search executors failed", e);
					e.printStackTrace();
				}
			}

			executorService.shutdown();

			if (searchStatistics != null) {
				searchStatistics.end();
				searchStatistics.setSolution(bestSolution);
			}

			return bestSolution;
		} catch (InterruptedException e) {
			logger.error("Failed to do multi-threaded local search", e);
			if (searchStatistics != null) {
				searchStatistics.setException(e);
				searchStatistics.end();
			}
			return null;
		}
	}

	private static class LocalSearchResult {
		private int iteration;
		private TTPSolution solution;
		private LocalSearchStatistics statistics;

		private LocalSearchResult(int iteration, TTPSolution solution,
				LocalSearchStatistics statistics) {
			this.iteration = iteration;
			this.solution = solution;
			this.statistics = statistics;
		}

		public int getIteration() {
			return iteration;
		}

		public TTPSolution getSolution() {
			return solution;
		}

		public LocalSearchStatistics getStatistics() {
			return statistics;
		}
	}

	private static class LocalSearchCallable implements
			Callable<LocalSearchResult> {
		private IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristic;
		private ILocalSearch<TTPSolution> localSearch;
		private int iteration;
		private boolean collectStatistics;

		private LocalSearchCallable(
				IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristic,
				ILocalSearch<TTPSolution> localSearch, int iteration,
				boolean collectStatistics) {
			this.constructionHeuristic = constructionHeuristic;
			this.localSearch = localSearch;
			this.iteration = iteration;
			this.collectStatistics = collectStatistics;
		}

		@Override
		public LocalSearchResult call() throws Exception {
			// get initial solution
			TTPSolution initialSolution = constructionHeuristic
					.getInitialSolution();
			LocalSearchStatistics localSearchStatistics = null;

			if (collectStatistics) {
				localSearchStatistics = new LocalSearchStatistics();
				localSearch.setLocalSearchStatistics(localSearchStatistics);
			}

			// apply local search
			TTPSolution lsSolution = localSearch.doLocalSearch(initialSolution);

			// logger.info("Iter: " + iteration + " Current Solution: " +
			// lsSolution.getCost());
			return new LocalSearchResult(iteration, lsSolution,
					localSearchStatistics);
		}
	}
}

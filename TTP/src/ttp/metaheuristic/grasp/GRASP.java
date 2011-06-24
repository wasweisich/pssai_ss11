package ttp.metaheuristic.grasp;

import org.apache.log4j.Logger;
import ttp.constructionheuristics.IConstructionHeuristics;
import ttp.localsearch.neighborhood.ILocalSearch;
import ttp.metaheuristic.ISearch;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GRASP implements ISearch<TTPInstance, TTPSolution> {
    private static Logger logger = Logger.getLogger(GRASP.class);

    private ILocalSearch<TTPSolution> localSearch;
    private IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristic;
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

    @Override
    public TTPSolution doSearch(TTPInstance instance) {
        ExecutorService executorService = Executors.newFixedThreadPool(noThreads);
        List<LocalSearchCallable> localSearches = new ArrayList<LocalSearchCallable>(noTries);
        constructionHeuristic.setProblemInstance(instance);

        try {
            for (int i = 0; i < noTries; i++) {
                IConstructionHeuristics<TTPInstance, TTPSolution> clonedConstructionHeuristic =
                        constructionHeuristic.clone();
                ILocalSearch<TTPSolution> clonedLocalSearch = localSearch.clone();

                localSearches.add(new LocalSearchCallable(clonedConstructionHeuristic, clonedLocalSearch, i));
            }
        } catch (CloneNotSupportedException e) {
            logger.error("Failed to create clone for multi-threaded local search", e);
        }

        try {
            List<Future<TTPSolution>> solutions = executorService.invokeAll(localSearches);
            TTPSolution bestSolution = null;

            for (Future<TTPSolution> futureSolution : solutions) {
                try {
                    TTPSolution solution = futureSolution.get();

                    if (solution == null) {
                        logger.warn("Solution was null");
                        continue;
                    }

                    if (bestSolution == null || solution.getCost() < bestSolution.getCost())
                        bestSolution = solution;
                } catch (ExecutionException e) {
                    logger.warn("One of the local search executors failed", e);
                    e.printStackTrace();
                }
            }

            executorService.shutdown();

            return bestSolution;
        } catch (InterruptedException e) {
            logger.error("Failed to do multi-threaded local search", e);
            e.printStackTrace();
            return null;
        }
    }

    private static class LocalSearchCallable implements Callable<TTPSolution> {
        private IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristic;
        private ILocalSearch<TTPSolution> localSearch;
        private int iteration;

        private LocalSearchCallable(IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristic,
                                    ILocalSearch<TTPSolution> localSearch, int iteration) {
            this.constructionHeuristic = constructionHeuristic;
            this.localSearch = localSearch;
            this.iteration = iteration;
        }

        @Override
        public TTPSolution call() throws Exception {
            //get initial solution
            TTPSolution initialSolution = constructionHeuristic.getInitialSolution();

            //apply local search
            TTPSolution lsSolution = localSearch.doLocalSearch(initialSolution);

            logger.info("Iter: " + iteration + " Current Solution: " + lsSolution.getCost());

            return lsSolution;
        }
    }
}

package ttp.metaheuristic;

import ttp.constructionheuristics.IConstructionHeuristics;
import ttp.localsearch.neighborhood.ILocalSearch;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

public class GRASP implements ISearch<TTPInstance, TTPSolution> {

	private ILocalSearch<TTPSolution> localSearch;

	private IConstructionHeuristics<TTPInstance, TTPSolution> constructionHeuristic;
	
	private int noTries = 10;

	@Override
	public TTPSolution doSearch(TTPInstance instance) {
		
		TTPSolution bestSolution = null;
		constructionHeuristic.setProblemInstance(instance);
		
		for(int i = 0; i < noTries; i++)
		{
			//get initial solution
			TTPSolution initialSolution = constructionHeuristic.getInitialSolution();
			
			
			//apply local search
			TTPSolution lsSolution = localSearch.doLocalSearch(initialSolution);
			
			if(bestSolution == null || lsSolution.getCost() < bestSolution.getCost())
			{
				bestSolution = lsSolution;
			}			
		}

		return bestSolution;
	}

}

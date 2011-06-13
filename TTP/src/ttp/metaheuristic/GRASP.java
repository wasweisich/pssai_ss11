package ttp.metaheuristic;

import ttp.constructionheuristics.IConstructionHeuristics;
import ttp.localsearch.neighborhood.ILocalSearch;
import ttp.model.TTPSolution;

public class GRASP implements ISearch<TTPSolution> {

	private ILocalSearch<TTPSolution> localSearch;

	private IConstructionHeuristics<TTPSolution> constructionHeuristic;
	
	private int noTries = 10;

	@Override
	public TTPSolution doSearch() {
		
		TTPSolution bestSolution = null;
		
		for(int i = 0; i < noTries; i++)
		{
			//get initial solution
			TTPSolution initialSolution = constructionHeuristic.getInitialSolution();
			
			
			//apply local search
			TTPSolution lsSolution = localSearch.doSearch(initialSolution);
			
			if(bestSolution == null || lsSolution.getCost() < bestSolution.getCost())
			{
				bestSolution = lsSolution;
			}			
		}

		return bestSolution;
	}

}

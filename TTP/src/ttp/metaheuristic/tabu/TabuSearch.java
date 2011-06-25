package ttp.metaheuristic.tabu;

import java.util.Date;

import org.apache.log4j.Logger;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.metaheuristic.ILocalSearch;
import ttp.metaheuristic.LocalSearchStatistics;
import ttp.model.TTPSolution;

public class TabuSearch implements ILocalSearch<TTPSolution> {

	private int tabuListLength = 50;
	private INeighborhood<TTPSolution> neighborhood;
	private int maxNoImprovement = 250;
	private LocalSearchStatistics localSearchStatistics;
	private Date lastFinishTime;
	private boolean useTimeLimit = false;

	public Date getLastFinishTime() {
		return lastFinishTime;
	}

	public void setLastFinishTime(Date lastFinishTime) {
		this.lastFinishTime = lastFinishTime;
	}

	public boolean isUseTimeLimit() {
		return useTimeLimit;
	}

	public void setUseTimeLimit(boolean useTimeLimit) {
		this.useTimeLimit = useTimeLimit;
	}

	private static Logger logger = Logger.getLogger(TabuSearch.class);

	@Override
	public TabuSearch clone() throws CloneNotSupportedException {
		TabuSearch clone = (TabuSearch) super.clone();
		clone.setTabuListLength(tabuListLength);
		clone.setMaxNoImprovement(maxNoImprovement);
		clone.setNeighborhood(neighborhood.clone());
		clone.setUseTimeLimit(useTimeLimit);
		clone.setLastFinishTime(lastFinishTime);

		return clone;
	}

	@Override
	public TTPSolution doLocalSearch(TTPSolution initialSolution) {
		if (localSearchStatistics != null)
			localSearchStatistics.start();

		int lastImprovementIterNo = 1;
		int iterNo = 1;

		IPenalty<TTPSolution> penaltyFunction = new AdaptiveTTPPenaltyFunction();

		// TTPSolution bestTabuSol = null;
		TTPSolution bestNonTabuSol = null;
		ITabuList<TTPSolution> tabuList = new SimpleTTPTabuList(tabuListLength);

		TTPSolution currentSolution = initialSolution;
		TTPSolution bestFoundLegalSolution = null;

		if (initialSolution.isLegal())
			bestFoundLegalSolution = initialSolution;

		do {
			bestNonTabuSol = null;

			neighborhood.init(currentSolution);

			while (neighborhood.hasNext()) {
				TTPSolution sol = neighborhood.getNext();

				if (sol == null)
					continue;

				if (localSearchStatistics != null)
					localSearchStatistics.neighborhoodExplored();

				penaltyFunction.doPenalty(sol);

				// check if sol in tabu list
				if (!tabuList.contains(sol)) {
					if (localSearchStatistics != null)
						localSearchStatistics.nonTabuNeighborhoodExplored();

					if (bestNonTabuSol == null
							|| sol.getCostWithPenalty() < bestNonTabuSol
									.getCostWithPenalty()) {
						if (localSearchStatistics != null)
							localSearchStatistics
									.betterNonTabuNeighborhoodExplored();
						bestNonTabuSol = sol;
					}
				} else if (localSearchStatistics != null)
					localSearchStatistics.tabuNeighborhoodExplored();

				// save best legal solution
				if (sol.isLegal()) {
					if (localSearchStatistics != null)
						localSearchStatistics.legalNeighborhoodExplored();

					if (bestFoundLegalSolution == null
							|| sol.getCost() < bestFoundLegalSolution.getCost()) {
						if (localSearchStatistics != null)
							localSearchStatistics
									.betterNonTabuNeighborhoodExplored();

						bestFoundLegalSolution = sol;
						lastImprovementIterNo = iterNo;
					}
				}
			}

			if (bestNonTabuSol != null) {
				// take new solution
				currentSolution = bestNonTabuSol;

				// add current solution to tabuList
				tabuList.add(bestNonTabuSol);

				// update penalty
				penaltyFunction.updatePenalty(bestNonTabuSol);

				if (localSearchStatistics != null)
					localSearchStatistics.solutionCostAtIteration(iterNo,
							bestNonTabuSol.getCost(),
							penaltyFunction.getPenaltyFactor(),
							bestNonTabuSol.getScTotal());
			} else {
				break;
			}

			iterNo++;
			if (useTimeLimit) {
				if ((new Date()).after(lastFinishTime))
					break;
			}

		} while (iterNo < (lastImprovementIterNo + maxNoImprovement));

		if (localSearchStatistics != null) {
			localSearchStatistics.end();
			localSearchStatistics.setSolution(bestFoundLegalSolution);
		}

		return bestFoundLegalSolution;
	}

	public int getMaxNoImprovement() {
		return maxNoImprovement;
	}

	public INeighborhood<TTPSolution> getNeighborhood() {
		return neighborhood;
	}

	public int getTabuListLength() {
		return tabuListLength;
	}

	public void setMaxNoImprovement(int maxNoImprovement) {
		this.maxNoImprovement = maxNoImprovement;
	}

	public void setNeighborhood(INeighborhood<TTPSolution> neighborhood) {
		this.neighborhood = neighborhood;
	}

	public void setTabuListLength(int tabuListLength) {
		this.tabuListLength = tabuListLength;
	}

	@Override
	public LocalSearchStatistics getLocalSearchStatistics() {
		return localSearchStatistics;
	}

	@Override
	public void setLocalSearchStatistics(
			LocalSearchStatistics localSearchStatistics) {
		this.localSearchStatistics = localSearchStatistics;
	}
}

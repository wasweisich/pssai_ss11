package ttp.metaheuristic.tabu;

import ttp.model.TTPSolution;

public class AdaptiveTTPPenaltyFunction implements IPenalty<TTPSolution> {

	private static final double INCREASEFACTOR = 1.0;
	private double penaltyFactor = 1.0;

	@Override
	public double doPenalty(TTPSolution sol) {

		double result = sol.getScTotal() * penaltyFactor;

		result = sol.getCost() * result / 100;
		sol.setPenalty(result);

		return result;
	}

	@Override
	public void updatePenalty(TTPSolution sol) {
		if (sol.isLegal()) {
			penaltyFactor = Math.max(1.0, penaltyFactor - INCREASEFACTOR);
		} else {
			penaltyFactor += INCREASEFACTOR;
		}
	}

}

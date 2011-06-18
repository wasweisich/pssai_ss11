package ttp.metaheuristic.tabu;

import ttp.model.TTPSolution;

public class AdaptiveTTPPenaltyFunction implements IPenalty<TTPSolution> {

	private static final double INCREASEFACTOR = 0.125;
	private static final double MINVALUE = 0;
	private double penaltyFactor = 0.0;

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
			penaltyFactor = Math.max(MINVALUE, penaltyFactor - INCREASEFACTOR);
		} else {
			penaltyFactor += INCREASEFACTOR;
		}
	}

	@Override
	public String toString() {
		return Double.toString(penaltyFactor);
	}

}

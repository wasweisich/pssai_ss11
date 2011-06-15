package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class TwoOptSwapRoundsNeighborhood extends TTPNeighborhoodBase {

	private int index1 = 0;
	private int index2 = 1;

	@Override
	public TTPSolution getNext() {
		TTPSolution next = new TTPSolution(baseSolution);
		int[][] schedule = next.getSchedule();

		int tmp;

		for (int i = 0; i < totalTeams; i++) {
			tmp = schedule[index1][i];
			schedule[index1][i] = schedule[index2][i];
			schedule[index2][i] = tmp;
		}

		next.setSchedule(schedule);

		TTPSolution tata = new TTPSolution(next);

		// update costs & penalties
		TtpSolutionHelper.updateAll(next);

		if (next.isLegal() && next.getScTotal() > 0) {
			int wtf = 0;
			wtf++;
			TtpSolutionHelper.updateAll(tata);
		}

		// increment indices
		index2++;
		if (index2 == totalGames) {
			// next team
			index1++;
			index2 = index1 + 1;
		}

		return next;
	}

	@Override
	public boolean hasNext() {
		if (index1 < totalGames - 1)
			return true;

		return false;
	}

	@Override
	public void init(TTPSolution solution) {
		super.init(solution);

		index1 = 0;
		index2 = 1;
	}

}

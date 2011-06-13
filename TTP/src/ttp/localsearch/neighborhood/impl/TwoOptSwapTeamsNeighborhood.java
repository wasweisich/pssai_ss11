package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;

public class TwoOptSwapTeamsNeighborhood extends TTPNeighborhoodBase {

	private int index1 = 0;
	private int index2 = 1;

	@Override
	public boolean hasNext() {
		if (index1 < totalTeams - 1)
			return true;

		return false;
	}

	@Override
	public TTPSolution getNext() {
		TTPSolution next = new TTPSolution();
		int[][] schedule = copySchedule(baseSolution.getSchedule());
		int tmp;

		for (int i = 0; i < totalGames; i++) {
			if (schedule[index1][i] == (index2 + 1)) {
				// change home to visitor
				schedule[index1][i] = -(index2 + 1);
				schedule[index2][i] = (index1 + 1);

			} else if (schedule[index1][i] == -(index2 + 1)) {
				// change visitor to home
				schedule[index1][i] = (index2 + 1);
				schedule[index2][i] = -(index1 + 1);

			} else {
				tmp = schedule[index1][i];
				schedule[index1][i] = schedule[index2][i];
				schedule[index2][i] = tmp;
			}
		}

		next.setSchedule(schedule);

		// increment indices
		index2++;
		if (index2 == totalTeams) {
			// next team
			index1++;
			index2 = index1 + 1;
		}

		return next;
	}

}

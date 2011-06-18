package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class SwapMatchRoundNeighborhood extends TTPNeighborhoodBase {

	private int teamIndex;
	private int roundIndex2;
	private int roundIndex1;

	@Override
	public TTPSolution getNext() {
		//TODO: skipp if team plays against same opponent in round roundIndex1 and roundIndex2
		
		if (teamIndex < noTeams) {
			TTPSolution next = new TTPSolution(baseSolution);
			int[][] schedule = next.getSchedule();

			swapMatchRound(schedule, teamIndex, roundIndex1, roundIndex2);

			next.setSchedule(schedule);

			// update costs & penalties
			TtpSolutionHelper.updateAll(next);

			// increment indices
			roundIndex2++;
			if (roundIndex2 == noRounds) {
				// next round
				roundIndex1++;
				roundIndex2 = roundIndex1 + 1;
			}

			if (roundIndex1 == noRounds) {
				// next team
				teamIndex++;
				roundIndex1 = 0;
				roundIndex2 = roundIndex1 + 1;
			}

			return next;
		} else {
			return null;
		}
	}

	private void swapMatchRound(int[][] schedule, int teamIndex2,
			int roundIndex12, int roundIndex22) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasNext() {
		if (teamIndex < noTeams)
			return true;

		return false;
	}

	@Override
	public void init(TTPSolution solution) {
		super.init(solution);

		teamIndex = 0;
		roundIndex1 = 0;
		roundIndex2 = 1;
	}

}

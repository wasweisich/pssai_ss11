package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class SwapHomeVisitorNeighborhood extends TTPNeighborhoodBase {
	private int teamNo = 0;
	private int currentChangeTeam = 0;

	@Override
	public TTPSolution getNext() {
		TTPSolution next = new TTPSolution(baseSolution);

		/*
		 * if ((baseSolution.isLegal() && baseSolution.getScTotal() > 0) ||
		 * !TtpSolutionHelper.checkSolution(baseSolution)) { int wtf = 0; wtf++;
		 * }
		 */
		int[][] schedule = next.getSchedule();

		// change home/away game of team teamNo against team currentChangeTeam

		int homeGame = 0;
		int visitorGame = 0;
		for (int i = 0; i < noRounds; i++) {
			if (schedule[i][teamNo - 1] == currentChangeTeam)
				homeGame = i;

			if (schedule[i][teamNo - 1] == -currentChangeTeam)
				visitorGame = i;
		}

		// change games
		schedule[homeGame][teamNo - 1] = -currentChangeTeam;
		schedule[homeGame][currentChangeTeam - 1] = teamNo;

		schedule[visitorGame][teamNo - 1] = currentChangeTeam;
		schedule[visitorGame][currentChangeTeam - 1] = -teamNo;

		next.setSchedule(schedule);

		// update costs & penalties
		TtpSolutionHelper.updateAll(next);

		// increase counter
		currentChangeTeam++;
		if (currentChangeTeam > noTeams) {
			// next team
			teamNo++;
			currentChangeTeam = teamNo + 1;
		}

		return next;
	}

	@Override
	public boolean hasNext() {
		if (teamNo < noTeams)
			return true;

		return false;
	}

	@Override
	public void init(TTPSolution solution) {
		super.init(solution);
		teamNo = 1;
		currentChangeTeam = 2;
	}

}

package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;

public class SwapHomeVisitorNeighborhood extends TTPNeighborhoodBase {
	private int teamNo = 0;
	private int currentChangeTeam = 0;

	@Override
	public void init(TTPSolution solution) {
		super.init(solution);


		teamNo = 1;
		currentChangeTeam = 2;
	}

	@Override
	public boolean hasNext() {
		if (teamNo < (totalTeams - 1))
			return true;

		return false;
	}

	@Override
	public TTPSolution getNext() {
		TTPSolution next = new TTPSolution();
		int[][] schedule = copySchedule(baseSolution.getSchedule());

		// change home/away game of team teamNo against team currentChangeTeam

		int homeGame = 0;
		int visitorGame = 0;
		for (int i = 0; i < totalGames; i++) {
			if (schedule[teamNo - 1][i] == currentChangeTeam)
				homeGame = i;

			if (schedule[teamNo - 1][i] == -currentChangeTeam)
				visitorGame = i;
		}

		// change games
		schedule[teamNo - 1][homeGame] = -currentChangeTeam;
		schedule[currentChangeTeam - 1][homeGame] = teamNo;

		schedule[teamNo - 1][visitorGame] = currentChangeTeam;
		schedule[currentChangeTeam - 1][visitorGame] = -teamNo;

		next.setSchedule(schedule);

		// increase conter
		currentChangeTeam++;
		if (currentChangeTeam == totalTeams) {
			// next team
			teamNo++;
			currentChangeTeam = teamNo + 1;
		}

		return next;
	}

}

package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;

public class SwapHomeVisitorNeighborhood implements INeighborhood<TTPSolution> {

	private TTPSolution baseSolution;

	private int teamNo = 0;
	private int currentChangeTeam = 0;

	private int totalTeams;
	private int totalGames;

	@Override
	public void init(TTPSolution solution) {
		this.baseSolution = solution;

		teamNo = 1;
		currentChangeTeam = 2;
		totalTeams = solution.getSchedule().length;
		totalGames = solution.getSchedule()[0].length;
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
		int[][] schedule = baseSolution.getSchedule().clone();

		// change home/away game of team teamNo against team currentChangeTeam

		int homeGame = 0;
		int visitorGame = 0;
		for (int i = 0; i < totalGames; i++) {
			if (schedule[teamNo-1][i] == currentChangeTeam)
				homeGame = i;

			if (schedule[teamNo-1][i] == -currentChangeTeam)
				visitorGame = i;
		}

		// change games
		schedule[teamNo-1][homeGame] = -currentChangeTeam;
		schedule[currentChangeTeam-1][homeGame] = teamNo;

		schedule[teamNo-1][visitorGame] = currentChangeTeam;
		schedule[currentChangeTeam-1][visitorGame] = -teamNo;

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

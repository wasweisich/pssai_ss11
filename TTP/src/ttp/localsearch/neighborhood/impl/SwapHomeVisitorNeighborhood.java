package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class SwapHomeVisitorNeighborhood extends TTPNeighborhoodBase {
	private int teamNo = 0;
	private int currentChangeTeam = 0;

	@Override
	public TTPSolution getNext() {
		TTPSolution next = new TTPSolution(baseSolution);

		int[][] schedule = next.getSchedule();

		// change home/away game of team teamNo against team currentChangeTeam

		int homeGame = 0;
		int visitorGame = 0;
		for (int i = 0; i < totalGames; i++) {
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
		int delta = -next.getTeamCost()[teamNo - 1];
		delta -= next.getTeamCost()[currentChangeTeam - 1];
		TtpSolutionHelper.updateTeam(next, teamNo);
		TtpSolutionHelper.updateTeam(next, currentChangeTeam);

		delta += next.getTeamCost()[teamNo - 1];
		delta += next.getTeamCost()[currentChangeTeam - 1];
		next.setCost(next.getCost() + delta);

		// increase counter
		currentChangeTeam++;
		if (currentChangeTeam == totalTeams) {
			// next team
			teamNo++;
			currentChangeTeam = teamNo + 1;
		}

		return next;
	}

	@Override
	public boolean hasNext() {
		if (teamNo < (totalTeams - 1))
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

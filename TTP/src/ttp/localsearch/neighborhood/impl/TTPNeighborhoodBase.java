package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;

public abstract class TTPNeighborhoodBase implements INeighborhood<TTPSolution> {
	protected int totalTeams;
	protected int totalGames;

	protected TTPSolution baseSolution;

	@Override
	public void init(TTPSolution solution) {
		this.baseSolution = solution;

		totalTeams = solution.getSchedule().length;
		totalGames = solution.getSchedule()[0].length;
	}

	protected TTPNeighborhoodBase() {

	}

	protected int[][] copySchedule(int[][] schedule) {
		int[][] result = new int[schedule.length][schedule[0].length];

		for (int i = 0; i < schedule.length; i++) {
			for (int j = 0; j < schedule[0].length; j++) {
				result[i][j] = schedule[i][j];
			}
		}

		return result;
	}

}

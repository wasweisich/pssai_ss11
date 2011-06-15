package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;

public abstract class TTPNeighborhoodBase implements INeighborhood<TTPSolution> {
	protected int totalTeams;
	protected int totalGames;

	protected TTPSolution baseSolution;

	protected TTPNeighborhoodBase() {

	}

	@Override
	public void init(TTPSolution solution) {
		this.baseSolution = solution;

		totalGames = solution.getSchedule().length;
		totalTeams = solution.getSchedule()[0].length;
	}



}

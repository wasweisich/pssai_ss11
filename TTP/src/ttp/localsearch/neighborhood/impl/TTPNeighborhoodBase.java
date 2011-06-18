package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;

public abstract class TTPNeighborhoodBase implements INeighborhood<TTPSolution> {
	protected int noTeams;
	protected int noRounds;

	protected TTPSolution baseSolution;

	protected TTPNeighborhoodBase() {

	}

	@Override
	public void init(TTPSolution solution) {
		this.baseSolution = solution;

		noRounds = solution.getSchedule().length;
		noTeams = solution.getSchedule()[0].length;
	}



}

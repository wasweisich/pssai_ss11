package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;

public class TwoTtpNeighborhoodCombination
		extends
		NeighborhoodCombination<TwoOptSwapRoundsNeighborhood, TwoOptSwapTeamsNeighborhood, TTPSolution> {

	public TwoTtpNeighborhoodCombination() {
		this.neighborhood_one = new TwoOptSwapRoundsNeighborhood();
		this.neighborhood_Two = new TwoOptSwapTeamsNeighborhood();

	}
}

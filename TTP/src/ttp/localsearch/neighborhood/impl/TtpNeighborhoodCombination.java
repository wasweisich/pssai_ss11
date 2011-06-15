package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;

public class TtpNeighborhoodCombination
		extends
		NeighborhoodCombination<TwoOptSwapRoundsNeighborhood, TwoOptSwapTeamsNeighborhood, TTPSolution> {

	public TtpNeighborhoodCombination() {
		this.neighborhood_one = new TwoOptSwapRoundsNeighborhood();
		this.neighborhood_Two = new TwoOptSwapTeamsNeighborhood();

	}
}

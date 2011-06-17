package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;

public class ShiftRoundNeighborhood extends TTPNeighborhoodBase {
	private int index1 = 0;
	private int index2 = 1;

	@Override
	public TTPSolution getNext() {
		TTPSolution next = new TTPSolution(baseSolution);
		
		int[][] schedule = next.getSchedule();
		
		int[] saveRound = new int[schedule[index1].length];
		
		for(int i = 0; i < saveRound.length; i++)
		{
			saveRound[i] = schedule[index1][i];
		}
		
		//shift rounds		
		for(int i = index1 + 1; i <= index2; i++)
		{
			for(int j = 0; j < saveRound.length; j++)
			{
				schedule[i-1][j] = schedule[i][j];
			}
		}
		
		for(int i = 0; i < saveRound.length; i++)
		{
			schedule[index2][i] = saveRound[i];
		}
		
		next.setSchedule(schedule);
		
		// increment indices
		index2++;
		if (index2 == totalGames) {
			// next team
			index1++;
			index2 = index1 + 1;
		}

		
		return next;
	}

	@Override
	public boolean hasNext() {
		if (index1 < totalGames - 1)
			return true;

		return false;
	}

	@Override
	public void init(TTPSolution solution) {
		super.init(solution);

		index1 = 0;
		index2 = 1;
	}

}

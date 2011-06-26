package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class SwapMatchRoundNeighborhood extends TTPNeighborhoodBase {

	private int teamIndex;
	private int roundIndex2;
	private int roundIndex1;

	@Override
	public INeighborhood<TTPSolution> clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public TTPSolution getNext() {
		if (teamIndex < noTeams) {
			TTPSolution next = new TTPSolution(baseSolution);

			swapMatchRound(next.getSchedule(), baseSolution.getSchedule(),
					teamIndex, roundIndex1, roundIndex2);

			// update costs & penalties
			TtpSolutionHelper.updateAll(next);

			do {

				// increment indices
				roundIndex2++;
				if (roundIndex2 == noRounds) {
					// next round
					roundIndex1++;
					roundIndex2 = roundIndex1 + 1;
				}

				if (roundIndex1 == (noRounds - 1)) {
					// next team
					teamIndex++;
					roundIndex1 = 0;
					roundIndex2 = roundIndex1 + 1;
				}
			} while (teamIndex < noTeams
					&& baseSolution.getSchedule()[roundIndex1][teamIndex] == -baseSolution
							.getSchedule()[roundIndex2][teamIndex]);

			return next;
		} else {
			return null;
		}
	}

	/**
	 * @param schedule
	 * @param baseSchedule
	 * @param team
	 *            0-based
	 * @param round1
	 *            0-based
	 * @param round2
	 *            0-based
	 */
	private void swapMatchRound(int[][] schedule, int[][] baseSchedule,
			int team, int round1, int round2) {

		// get first game
		int opponent = schedule[round1][team];
		int repairChainEntry = Math.abs(opponent);
		// move game to round2
		schedule[round2][team] = opponent;
		schedule[round2][repairChainEntry - 1] = baseSchedule[round1][repairChainEntry - 1];

		// insert opponent in repair chain

		int baseRound = round2;
		int targetRount = round1;

		while (repairChainEntry != (team + 1)) {
			// search for game of repairChainEntry in round2

			opponent = baseSchedule[baseRound][repairChainEntry - 1];

			// move
			// repairChainEntry and opponent to targetRount
			if (opponent > 0) {
				// home match of repairChainEntry
				schedule[targetRount][repairChainEntry - 1] = opponent;
				schedule[targetRount][opponent - 1] = -repairChainEntry;
			} else {
				// visitor match of repairChainEntry
				schedule[targetRount][repairChainEntry - 1] = opponent;
				schedule[targetRount][-opponent - 1] = repairChainEntry;
			}

			repairChainEntry = Math.abs(opponent);
			if (targetRount == round1) {
				baseRound = round1;
				targetRount = round2;
			} else {
				baseRound = round2;
				targetRount = round1;
			}
		}
	}

	@Override
	public boolean hasNext() {
		while (teamIndex < noTeams
				&& baseSolution.getSchedule()[roundIndex1][teamIndex] == -baseSolution
						.getSchedule()[roundIndex2][teamIndex]) {
			// increment indices
			roundIndex2++;
			if (roundIndex2 == noRounds) {
				// next round
				roundIndex1++;
				roundIndex2 = roundIndex1 + 1;
			}

			if (roundIndex1 == (noRounds - 1)) {
				// next team
				teamIndex++;
				roundIndex1 = 0;
				roundIndex2 = roundIndex1 + 1;
			}
		}
		return teamIndex < noTeams;
	}

	@Override
	public void init(TTPSolution solution) {
		super.init(solution);

		teamIndex = 0;
		roundIndex1 = 0;
		roundIndex2 = 1;
	}

}

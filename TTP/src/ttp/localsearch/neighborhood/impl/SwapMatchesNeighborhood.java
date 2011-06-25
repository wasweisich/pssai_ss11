package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class SwapMatchesNeighborhood extends TTPNeighborhoodBase {
	int round = 0;
	int team1 = 0;
	int team2 = 1;

	@Override
	public INeighborhood<TTPSolution> clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public TTPSolution getNext() {
		if (!hasNext())
			return null;

		TTPSolution nextSolution = new TTPSolution(baseSolution);
		int[][] schedule = nextSolution.getSchedule();

		// find next possible move, might return null if no more moves are
		// possible
		while (true) {
			if (!hasNext())
				return null;

			boolean swapped = swapMatches(schedule, round, team1, team2, true);

			if (!swapped)
				moveNext();
			else
				break;
		}

		int repairRoundNo = round;

		for (int i = 0; i < noRounds; i++) {
			if (i == round)
				continue;

			if (schedule[i][team1] == schedule[round][team1]) {
				repairRoundNo = i;
				break;
			}
		}
		System.out.println("Round: " + round);

		int newElement = 0;
		while (newElement != schedule[round][team2]) {

	//		System.out.println(repairRoundNo);
			
			swapMatchesSpecial(schedule, repairRoundNo, team1, team2);
			newElement = schedule[repairRoundNo][team1];
			
			for (int i = 0; i < noRounds; i++) {
				if (i == repairRoundNo)
					continue;

				if (schedule[i][team1] == schedule[repairRoundNo][team1]) {
					repairRoundNo = i;
					break;
				}
			}

		}
		/*
		 * Set<Integer> roundsRepaired = new HashSet<Integer>(); Queue<Integer>
		 * repairNeeded = new LinkedList<Integer>(); repairNeeded.offer(round);
		 * 
		 * while (!repairNeeded.isEmpty()) { int roundToRepair =
		 * repairNeeded.remove();
		 * 
		 * boolean team1Repaired = false; boolean team2Repaired = false;
		 * 
		 * for (int repairRound = 0; repairRound < noRounds && !(team1Repaired
		 * && team2Repaired); repairRound++) { if (roundToRepair == repairRound
		 * || roundsRepaired.contains(repairRound)) continue;
		 * 
		 * if (schedule[repairRound][team1] == schedule[roundToRepair][team1]) {
		 * swapMatches(schedule, repairRound, team1, team2, false); if
		 * (!repairNeeded.contains(repairRound))
		 * repairNeeded.offer(repairRound); roundsRepaired.add(repairRound);
		 * team1Repaired = true; } else if (schedule[repairRound][team2] ==
		 * schedule[roundToRepair][team2]) { swapMatches(schedule, repairRound,
		 * team1, team2, false); if (!repairNeeded.contains(repairRound))
		 * repairNeeded.offer(repairRound); roundsRepaired.add(repairRound);
		 * team2Repaired = true; } } }
		 */
		TtpSolutionHelper.updateAll(nextSolution);

		moveNext();

		return nextSolution;
	}
	
	private boolean swapMatchesSpecial(int[][] schedule, int round, int team1,
			int team2) {
		int opponent1 = Math.abs(schedule[round][team1]);

		int opponent2 = Math.abs(schedule[round][team2]);
		
		int tmp = schedule[round][team2];
		schedule[round][team2] = schedule[round][team1];
		schedule[round][team1] = tmp;
		
		if(schedule[round][team1] > 0)
		{
			schedule[round][opponent2-1] = - (team1+1);
		}
		else
		{
			schedule[round][opponent2-1] = (team1+1);
		}
		
		if(schedule[round][team2] > 0)
		{
			schedule[round][opponent1-1] = - (team2+1);
		}
		else
		{
			schedule[round][opponent1-1] = (team2+1);
		}

		return true;
	}

	private boolean swapMatches(int[][] schedule, int round, int team1,
			int team2, boolean abortIfSame) {
		int opponent1 = Math.abs(schedule[round][team1]);

		if (abortIfSame && opponent1 - 1 == team2)
			return false;

		int opponent2 = Math.abs(schedule[round][team2]);
		boolean team1Home = schedule[round][team1] > 0;
		boolean team2Home = schedule[round][team2] > 0;

		schedule[round][team1] = team1Home ? opponent2 : -opponent2;
		schedule[round][team2] = team2Home ? opponent1 : -opponent1;
		opponent1--;
		opponent2--;
		team1++;
		team2++;
		schedule[round][opponent1] = team2Home ? -team2 : team2;
		schedule[round][opponent2] = team1Home ? -team1 : team1;

		return true;
	}

	private void moveNext() {
		team2++;

		if (team2 == noTeams) {
			team1++;
			team2 = team1 + 1;
		}

		if (team1 == noTeams - 1) {
			round++;
			team1 = 0;
			team2 = 1;
		}
	}

	@Override
	public boolean hasNext() {
		return !(round >= noRounds - 1 && team1 >= noTeams - 2 && team2 >= noTeams - 1);
	}

	@Override
	public void init(TTPSolution solution) {
		super.init(solution);

		round = 0;
		team1 = 0;
		team2 = 1;
	}
}

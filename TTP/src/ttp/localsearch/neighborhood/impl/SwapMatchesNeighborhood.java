package ttp.localsearch.neighborhood.impl;

import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

import java.util.LinkedList;
import java.util.Queue;

public class SwapMatchesNeighborhood extends TTPNeighborhoodBase {
    int round = 0;
    int team1 = 0;
    int team2 = 1;

    @Override
    public TTPSolution getNext() {
        if (!hasNext()) return null;

        TTPSolution nextSolution = new TTPSolution(baseSolution);
        int[][] schedule = nextSolution.getSchedule();

        // find next possible move, might return null if no more moves are possible
        while (true) {
            if (!hasNext()) return null;

            boolean swapped = swapMatches(schedule, round, team1, team2, true);

            if (!swapped)
                moveNext();
            else
                break;
        }

        //Set<Integer> roundsRepaired = new HashSet<Integer>();
        Queue<Integer> repairNeeded = new LinkedList<Integer>();
        repairNeeded.offer(round);

        while (!repairNeeded.isEmpty()) {
            int roundToRepair = repairNeeded.remove();

            boolean team1Repaired = false;
            boolean team2Repaired = false;

            for (int repairRound = 0; repairRound < noRounds && !(team1Repaired && team2Repaired); repairRound++) {
                if (roundToRepair == repairRound) continue;

                if (schedule[repairRound][team1] == schedule[roundToRepair][team1]) {
                    swapMatches(schedule, repairRound, team1, team2, false);
                    if (!repairNeeded.contains(repairRound))
                        repairNeeded.offer(repairRound);
                    team1Repaired = true;
                } else if (schedule[repairRound][team2] == schedule[roundToRepair][team2]) {
                    swapMatches(schedule, repairRound, team1, team2, false);
                    if (!repairNeeded.contains(repairRound))
                        repairNeeded.offer(repairRound);
                    team2Repaired = true;
                }
            }
        }

        TtpSolutionHelper.updateAll(nextSolution);

        moveNext();

        return nextSolution;
    }

    private boolean swapMatches(int[][] schedule, int round, int team1, int team2, boolean abortIfSame) {
        int opponent1 = Math.abs(schedule[round][team1]);

        if (abortIfSame && opponent1 - 1 == team2) return false;

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

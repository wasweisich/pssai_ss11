package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class TwoOptSwapTeamsNeighborhood extends TTPNeighborhoodBase {
    private int index1 = 0;
    private int index2 = 1;

    @Override
    public INeighborhood<TTPSolution> clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public TTPSolution getNext() {
        TTPSolution next = new TTPSolution(baseSolution);
        int[][] schedule = next.getSchedule();

        int tmp;

        for (int i = 0; i < noRounds; i++) {
            if (schedule[i][index1] == (index2 + 1)) {
                // change home to visitor
                schedule[i][index1] = -(index2 + 1);
                schedule[i][index2] = (index1 + 1);
            } else if (schedule[i][index1] == -(index2 + 1)) {
                // change visitor to home
                schedule[i][index1] = (index2 + 1);
                schedule[i][index2] = -(index1 + 1);
            } else {
                tmp = schedule[i][index1];
                schedule[i][index1] = schedule[i][index2];
                schedule[i][index2] = tmp;

                // change other team infos
                if (schedule[i][index1] < 0)
                    schedule[i][-schedule[i][index1] - 1] = index1 + 1;
                else
                    schedule[i][schedule[i][index1] - 1] = -(index1 + 1);

                if (schedule[i][index2] < 0)
                    schedule[i][-schedule[i][index2] - 1] = index2 + 1;
                else
                    schedule[i][schedule[i][index2] - 1] = -(index2 + 1);
            }
        }

        next.setSchedule(schedule);

        // update costs & penalties
        TtpSolutionHelper.updateAll(next);

        // increment indices
        index2++;
        if (index2 == noTeams) {
            // next team
            index1++;
            index2 = index1 + 1;
        }

        return next;
    }

    @Override
    public boolean hasNext() {
        return index1 < noTeams - 1;
    }

    @Override
    public void init(TTPSolution solution) {
        super.init(solution);

        index1 = 0;
        index2 = 1;
    }

}

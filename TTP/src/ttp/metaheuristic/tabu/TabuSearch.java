package ttp.metaheuristic.tabu;

import org.apache.log4j.Logger;
import ttp.localsearch.neighborhood.ILocalSearch;
import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;

public class TabuSearch implements ILocalSearch<TTPSolution> {

    private int tabuListLength = 50;
    private INeighborhood<TTPSolution> neighborhood;
    private int maxNoImprovement = 250;

    private static Logger logger = Logger.getLogger(TabuSearch.class);

    @Override
    public TabuSearch clone() throws CloneNotSupportedException {
        TabuSearch clone = (TabuSearch) super.clone();
        clone.setTabuListLength(tabuListLength);
        clone.setMaxNoImprovement(maxNoImprovement);
        clone.setNeighborhood(neighborhood.clone());

        return clone;
    }

    @Override
    public TTPSolution doLocalSearch(TTPSolution initialSolution) {
        int lastImprovementIterNo = 1;
        int iterNo = 1;

        IPenalty<TTPSolution> penaltyFunction = new AdaptiveTTPPenaltyFunction();

        // TTPSolution bestTabuSol = null;
        TTPSolution bestNonTabuSol = null;
        ITabuList<TTPSolution> tabuList = new SimpleTTPTabuList(tabuListLength);

        TTPSolution currentSolution = initialSolution;
        TTPSolution bestFoundLegalSolution = null;

        if (initialSolution.isLegal())
            bestFoundLegalSolution = initialSolution;

        do {
            bestNonTabuSol = null;

            neighborhood.init(currentSolution);

            while (neighborhood.hasNext()) {
                TTPSolution sol = neighborhood.getNext();

                if (sol == null) continue;

                penaltyFunction.doPenalty(sol);

                // check if sol in tabu list
                if (!tabuList.contains(sol)) {
                    if (bestNonTabuSol == null || sol.getCostWithPenalty() < bestNonTabuSol.getCostWithPenalty()) {
                        bestNonTabuSol = sol;
                    }
                }
                /*
                     * else { if (bestTabuSol == null || sol.getCost() <
                     * bestTabuSol.getCost()) { bestTabuSol = sol; } }
                     */

                // save best legal solution
                if (sol.isLegal() &&
                        (bestFoundLegalSolution == null || sol.getCost() < bestFoundLegalSolution.getCost())) {
                    bestFoundLegalSolution = sol;
                    lastImprovementIterNo = iterNo;
                }
            }

            if (bestNonTabuSol != null) {
                // take new solution
                currentSolution = bestNonTabuSol;

                // add current solution to tabuList
                tabuList.add(bestNonTabuSol);

                // update penalty
                penaltyFunction.updatePenalty(bestNonTabuSol);

                /*	logger.info("Iter: " + iterNo + " Best NTBS: "
                            + bestNonTabuSol.getCost() + " ScT: "
                            + bestNonTabuSol.getScTotal() + " PenaltyFactor: "
                            + penaltyFunction.toString());*/

            } else {
                break;
            }

            iterNo++;
        } while (iterNo < (lastImprovementIterNo + maxNoImprovement));
        return bestFoundLegalSolution;
    }

    public int getMaxNoImprovement() {
        return maxNoImprovement;
    }

    public INeighborhood<TTPSolution> getNeighborhood() {
        return neighborhood;
    }

    public int getTabuListLength() {
        return tabuListLength;
    }

    public void setMaxNoImprovement(int maxNoImprovement) {
        this.maxNoImprovement = maxNoImprovement;
    }

    public void setNeighborhood(INeighborhood<TTPSolution> neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setTabuListLength(int tabuListLength) {
        this.tabuListLength = tabuListLength;
    }
}

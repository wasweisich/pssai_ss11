package ttp.metaheuristic;

import java.util.Date;
import java.util.LinkedList;

public class LocalSearchStatistics implements Cloneable {
    private int neighborhoodsExplored = 0;
    private int nonTabuNeighborhoodsExplored = 0;
    private int betterNonTabuNeighborhoodsExplored = 0;
    private int tabuNeighborhoodsExplored = 0;
    private int legalNeighborhoodsExplored = 0;
    private int betterLegalNeighborhoodsExplored = 0;
    private LinkedList<SolutionCostAtIteration> solutionCosts = new LinkedList<SolutionCostAtIteration>();
    private Date start;
    private Date end;

    public static class SolutionCostAtIteration {
        private int iteration;
        private int cost;
        private double penalty;
        private int softConstraintViolations;

        public SolutionCostAtIteration(int iteration, int cost, double penalty, int softConstraintViolations) {
            this.iteration = iteration;
            this.cost = cost;
            this.penalty = penalty;
            this.softConstraintViolations = softConstraintViolations;
        }

        public int getIteration() {
            return iteration;
        }

        public int getCost() {
            return cost;
        }

        public double getPenalty() {
            return penalty;
        }

        public int getSoftConstraintViolations() {
            return softConstraintViolations;
        }
    }

    @Override
    protected LocalSearchStatistics clone() throws CloneNotSupportedException {
        LocalSearchStatistics clone = (LocalSearchStatistics) super.clone();
        return clone;
    }

    public void solutionCostAtIteration(int iteration, int cost, double penalty, int softConstrationViolations) {
        SolutionCostAtIteration solutionCostAtIteration = new SolutionCostAtIteration(iteration, cost, penalty, softConstrationViolations);
        solutionCosts.add(solutionCostAtIteration);
    }

    public void start() {
        start = new Date();
    }

    public void end() {
        end = new Date();
    }

    public void neighborhoodExplored() {
        neighborhoodsExplored++;
    }

    public void nonTabuNeighborhoodExplored() {
        nonTabuNeighborhoodsExplored++;
    }

    public void betterNonTabuNeighborhoodExplored() {
        betterNonTabuNeighborhoodsExplored++;
    }

    public void tabuNeighborhoodExplored() {
        tabuNeighborhoodsExplored++;
    }

    public void legalNeighborhoodExplored() {
        legalNeighborhoodsExplored++;
    }

    public void betterLegalNeighborhoodExplored() {
        betterLegalNeighborhoodsExplored++;
    }

    public int getNeighborhoodsExplored() {
        return neighborhoodsExplored;
    }

    public int getNonTabuNeighborhoodsExplored() {
        return nonTabuNeighborhoodsExplored;
    }

    public int getBetterNonTabuNeighborhoodsExplored() {
        return betterNonTabuNeighborhoodsExplored;
    }

    public int getWorseNonTabuNeighborhoodsExplored() {
        return nonTabuNeighborhoodsExplored - betterNonTabuNeighborhoodsExplored;
    }

    public int getTabuNeighborhoodsExplored() {
        return tabuNeighborhoodsExplored;
    }

    public int getLegalNeighborhoodsExplored() {
        return legalNeighborhoodsExplored;
    }

    public int getBetterLegalNeighborhoodsExplored() {
        return betterLegalNeighborhoodsExplored;
    }

    public int getWorseLegalNeighborhoodsExplored() {
        return legalNeighborhoodsExplored - betterLegalNeighborhoodsExplored;
    }

    public LinkedList<SolutionCostAtIteration> getSolutionCosts() {
        return solutionCosts;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public long getDurationInMilliseconds() {
        return end.getTime() - start.getTime();
    }
}

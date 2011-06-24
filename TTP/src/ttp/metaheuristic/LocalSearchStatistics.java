package ttp.metaheuristic;

import java.io.PrintWriter;
import java.util.*;

public class LocalSearchStatistics implements Cloneable {
    private int neighborhoodsExplored = 0;
    private int nonTabuNeighborhoodsExplored = 0;
    private int betterNonTabuNeighborhoodsExplored = 0;
    private int tabuNeighborhoodsExplored = 0;
    private int legalNeighborhoodsExplored = 0;
    private int betterLegalNeighborhoodsExplored = 0;
    private List<SolutionCostAtIteration> solutionCosts = new LinkedList<SolutionCostAtIteration>();
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
        SolutionCostAtIteration solutionCostAtIteration =
                new SolutionCostAtIteration(iteration, cost, penalty, softConstrationViolations);
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

    public List<SolutionCostAtIteration> getSolutionCosts() {
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


    public void writeInformationHeader(PrintWriter writer) {
        writer.println("neighborhoodsExplored," +
                "nonTabuNeighborhoodsExplored,betterNonTabuNeighborhoodsExplored,worseNonTabuNeighborhoodsExplored," +
                "tabuNeighborhoodsExplored," +
                "legalNeighborhoodsExplored,betterLegalNeighborhoodsExplored,worseLegalNeighborhoodsExplored," +
                "start,end,duration");
    }

    public void writeInformation(PrintWriter writer) {
        writer.print(getNeighborhoodsExplored());
        writer.print(',');
        writer.print(getNonTabuNeighborhoodsExplored());
        writer.print(',');
        writer.print(getBetterNonTabuNeighborhoodsExplored());
        writer.print(',');
        writer.print(getWorseNonTabuNeighborhoodsExplored());
        writer.print(',');
        writer.print(getTabuNeighborhoodsExplored());
        writer.print(',');
        writer.print(getLegalNeighborhoodsExplored());
        writer.print(',');
        writer.print(getBetterLegalNeighborhoodsExplored());
        writer.print(',');
        writer.print(getWorseLegalNeighborhoodsExplored());
        writer.print(',');
        writer.print(getStart());
        writer.print(',');
        writer.print(getEnd());
        writer.print(',');
        writer.print(getDurationInMilliseconds());
        writer.println();
    }

    public void writeIterationsHeader(PrintWriter writer) {
        writer.print("iteration,");
        writer.print("cost,");
        writer.print("penalty,");
        writer.print("softConstraintViolations");
        writer.println();
    }

    public void writeIterations(PrintWriter writer) {
        solutionCosts = new ArrayList<SolutionCostAtIteration>(solutionCosts);
        Collections.sort(solutionCosts, new Comparator<SolutionCostAtIteration>() {
            @Override
            public int compare(SolutionCostAtIteration o1,
                               SolutionCostAtIteration o2) {
                return o1.iteration - o2.iteration;
            }
        });

        for (SolutionCostAtIteration sortedSolution : solutionCosts) {
            writer.print(sortedSolution.getIteration());
            writer.print(',');
            writer.print(sortedSolution.getCost());
            writer.print(',');
            writer.print(sortedSolution.getPenalty());
            writer.print(',');
            writer.print(sortedSolution.getSoftConstraintViolations());
            writer.println();
        }
    }
}

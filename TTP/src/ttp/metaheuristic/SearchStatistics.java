package ttp.metaheuristic;

import ttp.model.TTPSolution;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchStatistics {
    private Map<Integer, LocalSearchStatistics> localSearchStatistics = new HashMap<Integer, LocalSearchStatistics>();
    private Date start;
    private Date end;
    private Exception exception;
    private TTPSolution solution;

    public void start() {
        start = new Date();
    }

    public void end() {
        end = new Date();
    }

    public long getDurationInMilliSeconds() {
        return end.getTime() - start.getTime();
    }

    public void addLocalSearchStatistic(int iteration, LocalSearchStatistics localSearchStatistics) {
        this.localSearchStatistics.put(iteration, localSearchStatistics);
    }

    public void setSolution(TTPSolution solution) {
        this.solution = solution;
    }

    public Map<Integer, LocalSearchStatistics> getLocalSearchStatistics() {
        return localSearchStatistics;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public TTPSolution getSolution() {
        return solution;
    }

    public void writeStatisticsHeader(PrintWriter writer) {
        localSearchStatistics.values().iterator().next().writeInformationHeader(writer);
    }

    public void writeStatisticSum(PrintWriter writer) {
        int sumNeighborhoodsExplored = 0;
        int sumNonTabuNeighborhoodsExplored = 0;
        int sumBetterNonTabuNeighborhoodsExplored = 0;
        int sumTabuNeighborhoodsExplored = 0;
        int sumLegalNeighborhoodsExplored = 0;
        int sumBetterLegalNeighborhoodsExplored = 0;

        for (LocalSearchStatistics searchStatistics : localSearchStatistics.values()) {
            sumNeighborhoodsExplored += searchStatistics.getNeighborhoodsExplored();
            sumNonTabuNeighborhoodsExplored += searchStatistics.getNonTabuNeighborhoodsExplored();
            sumBetterNonTabuNeighborhoodsExplored += searchStatistics.getBetterNonTabuNeighborhoodsExplored();
            sumTabuNeighborhoodsExplored += searchStatistics.getTabuNeighborhoodsExplored();
            sumLegalNeighborhoodsExplored += searchStatistics.getLegalNeighborhoodsExplored();
            sumBetterLegalNeighborhoodsExplored += searchStatistics.getBetterLegalNeighborhoodsExplored();
        }

        LocalSearchStatistics sum = new LocalSearchStatistics(sumNeighborhoodsExplored, sumNonTabuNeighborhoodsExplored,
                sumBetterNonTabuNeighborhoodsExplored, sumTabuNeighborhoodsExplored, sumLegalNeighborhoodsExplored,
                sumBetterLegalNeighborhoodsExplored, null, start, end, solution);

        sum.writeInformation(writer);
    }

    public void writeStatisticAverage(PrintWriter writer) {
        float avgNeighborhoodsExplored = 0;
        float avgNonTabuNeighborhoodsExplored = 0;
        float avgBetterNonTabuNeighborhoodsExplored = 0;
        float avgTabuNeighborhoodsExplored = 0;
        float avgLegalNeighborhoodsExplored = 0;
        float avgBetterLegalNeighborhoodsExplored = 0;

        for (LocalSearchStatistics searchStatistics : localSearchStatistics.values()) {
            avgNeighborhoodsExplored += searchStatistics.getNeighborhoodsExplored();
            avgNonTabuNeighborhoodsExplored += searchStatistics.getNonTabuNeighborhoodsExplored();
            avgBetterNonTabuNeighborhoodsExplored += searchStatistics.getBetterNonTabuNeighborhoodsExplored();
            avgTabuNeighborhoodsExplored += searchStatistics.getTabuNeighborhoodsExplored();
            avgLegalNeighborhoodsExplored += searchStatistics.getLegalNeighborhoodsExplored();
            avgBetterLegalNeighborhoodsExplored += searchStatistics.getBetterLegalNeighborhoodsExplored();
        }

        avgNeighborhoodsExplored /= localSearchStatistics.size();
        avgNonTabuNeighborhoodsExplored /= localSearchStatistics.size();
        avgBetterNonTabuNeighborhoodsExplored /= localSearchStatistics.size();
        avgTabuNeighborhoodsExplored /= localSearchStatistics.size();
        avgLegalNeighborhoodsExplored /= localSearchStatistics.size();
        avgBetterLegalNeighborhoodsExplored /= localSearchStatistics.size();

        LocalSearchStatistics avg =
                new LocalSearchStatistics((int) avgNeighborhoodsExplored, (int) avgNonTabuNeighborhoodsExplored,
                        (int) avgBetterNonTabuNeighborhoodsExplored, (int) avgTabuNeighborhoodsExplored,
                        (int) avgLegalNeighborhoodsExplored, (int) avgBetterLegalNeighborhoodsExplored, null, start,
                        end, solution);

        avg.writeInformation(writer);
    }
}

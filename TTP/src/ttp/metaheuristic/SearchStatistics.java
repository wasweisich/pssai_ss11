package ttp.metaheuristic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchStatistics {
    private Map<Integer, LocalSearchStatistics> localSearchStatistics = new HashMap<Integer, LocalSearchStatistics>();
    private Date start;
    private Date end;
    private Exception exception;

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
}

package ttp.main;

import ttp.constructionheuristics.VirtualScheduleConstructionMethod;

import java.io.File;
import java.util.List;

public class TTPParameters {
    private TravelingTournamentProblem.Method method;
    private List<TravelingTournamentProblem.Neighborhood> neighborhoods;
    private TravelingTournamentProblem.ConstructionHeuristic constructionHeuristic;
    private VirtualScheduleConstructionMethod virtualScheduleConstructionMethod;
    private int tabuListLength;
    private long maxDuration;
    private int graspTries;
    private int iterationsWithoutImprovement;
    private int threadCount;
    private File instanceFile;
    private File outputDirectory;
	private double alpha;

    public TTPParameters(TravelingTournamentProblem.Method method,
                         List<TravelingTournamentProblem.Neighborhood> neighborhoods,
                         TravelingTournamentProblem.ConstructionHeuristic constructionHeuristic,
                         VirtualScheduleConstructionMethod virtualScheduleConstructionMethod, int tabuListLength,
                         int graspTries, int iterationsWithoutImprovement, int threadCount, File instanceFile,
                         File outputDirectory, long maxDuration, double alpha) {
        this.method = method;
        this.neighborhoods = neighborhoods;
        this.constructionHeuristic = constructionHeuristic;
        this.virtualScheduleConstructionMethod = virtualScheduleConstructionMethod;
        this.tabuListLength = tabuListLength;
        this.graspTries = graspTries;
        this.iterationsWithoutImprovement = iterationsWithoutImprovement;
        this.threadCount = threadCount;
        this.instanceFile = instanceFile;
        this.outputDirectory = outputDirectory;
        this.maxDuration = maxDuration;
        this.alpha = alpha;
    }

    public TTPParameters(TTPParameters other) {
        this(other.getMethod(), other.getNeighborhoods(), other.getConstructionHeuristic(),
                other.getVirtualScheduleConstructionMethod(), other.getTabuListLength(), other.getGraspTries(),
                other.getIterationsWithoutImprovement(), other.getThreadCount(), other.getInstanceFile(),
                other.getOutputDirectory(), other.getMaxDuration(), other.getAlpha());
    }

    public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }

    public TravelingTournamentProblem.Method getMethod() {
        return method;
    }

    public List<TravelingTournamentProblem.Neighborhood> getNeighborhoods() {
        return neighborhoods;
    }

    public TravelingTournamentProblem.ConstructionHeuristic getConstructionHeuristic() {
        return constructionHeuristic;
    }

    public VirtualScheduleConstructionMethod getVirtualScheduleConstructionMethod() {
        return virtualScheduleConstructionMethod;
    }

    public int getTabuListLength() {
        return tabuListLength;
    }

    public int getGraspTries() {
        return graspTries;
    }

    public int getIterationsWithoutImprovement() {
        return iterationsWithoutImprovement;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public File getInstanceFile() {
        return instanceFile;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setMethod(TravelingTournamentProblem.Method method) {
        this.method = method;
    }

    public void setNeighborhoods(List<TravelingTournamentProblem.Neighborhood> neighborhoods) {
        this.neighborhoods = neighborhoods;
    }

    public void setConstructionHeuristic(TravelingTournamentProblem.ConstructionHeuristic constructionHeuristic) {
        this.constructionHeuristic = constructionHeuristic;
    }

    public void setVirtualScheduleConstructionMethod(
            VirtualScheduleConstructionMethod virtualScheduleConstructionMethod) {
        this.virtualScheduleConstructionMethod = virtualScheduleConstructionMethod;
    }

    public void setTabuListLength(int tabuListLength) {
        this.tabuListLength = tabuListLength;
    }

    public void setGraspTries(int graspTries) {
        this.graspTries = graspTries;
    }

    public void setIterationsWithoutImprovement(int iterationsWithoutImprovement) {
        this.iterationsWithoutImprovement = iterationsWithoutImprovement;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void setInstanceFile(File instanceFile) {
        this.instanceFile = instanceFile;
    }
}

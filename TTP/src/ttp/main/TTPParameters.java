package ttp.main;

import ttp.constructionheuristics.VirtualScheduleConstructionMethod;

import java.io.File;
import java.util.List;

/**
 * ${DESCRIPTION}
 * <p/>
 * <p><b>Company:</b>
 * SAT, Research Studios Austria</p>
 * <p/>
 * <p><b>Copyright:</b>
 * (c) 2011</p>
 * <p/>
 * <p><b>last modified:</b><br/>
 * $Author: $<br/>
 * $Date: $<br/>
 * $Revision: $</p>
 *
 * @author patrick
 */
public class TTPParameters {
    private TravelingTournamentProblem.Method method;
    private List<TravelingTournamentProblem.Neighborhood> neighborhoods;
    private TravelingTournamentProblem.ConstructionHeuristic constructionHeuristic;
    private VirtualScheduleConstructionMethod virtualScheduleConstructionMethod;
    private int tabuListLength;
    private int graspTries;
    private int iterationsWithoutImprovement;
    private int threadCount;
    private File instanceFile;
    private File outputDirectory;

    public TTPParameters(TravelingTournamentProblem.Method method,
                         List<TravelingTournamentProblem.Neighborhood> neighborhoods,
                         TravelingTournamentProblem.ConstructionHeuristic constructionHeuristic,
                         VirtualScheduleConstructionMethod virtualScheduleConstructionMethod, int tabuListLength,
                         int graspTries, int iterationsWithoutImprovement, int threadCount, File instanceFile,
                         File outputDirectory) {
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
    }

    public TTPParameters(TTPParameters other) {
        this(other.getMethod(), other.getNeighborhoods(), other.getConstructionHeuristic(),
                other.getVirtualScheduleConstructionMethod(), other.getTabuListLength(), other.getGraspTries(),
                other.getIterationsWithoutImprovement(), other.getThreadCount(), other.getInstanceFile(),
                other.getOutputDirectory());
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

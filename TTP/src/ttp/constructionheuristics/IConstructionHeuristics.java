package ttp.constructionheuristics;

public interface IConstructionHeuristics<I, S> extends Cloneable {
    S getInitialSolution();

    void setProblemInstance(I instance);

    IConstructionHeuristics<I, S> clone() throws CloneNotSupportedException;
}

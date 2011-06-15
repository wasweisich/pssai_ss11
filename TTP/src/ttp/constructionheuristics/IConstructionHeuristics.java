package ttp.constructionheuristics;

public interface IConstructionHeuristics<I,S> {
	S getInitialSolution();
	void setProblemInstance(I instance);
}

package ttp.metaheuristic.tabu;

public interface IPenalty<T> {

	double doPenalty(T sol);

	void updatePenalty(T bestNonTabuSol);

}

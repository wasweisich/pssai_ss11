package ttp.localsearch.neighborhood;


public interface ILocalSearch<T> {
	T doSearch(T initialSolution);
}

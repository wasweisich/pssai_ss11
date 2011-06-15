package ttp.localsearch.neighborhood;


public interface ILocalSearch<T> {
	T doLocalSearch(T initialSolution);
}

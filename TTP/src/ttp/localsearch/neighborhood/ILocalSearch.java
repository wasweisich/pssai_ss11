package ttp.localsearch.neighborhood;

public interface ILocalSearch<T> extends Cloneable {
    T doLocalSearch(T initialSolution);

    ILocalSearch<T> clone() throws CloneNotSupportedException;
}

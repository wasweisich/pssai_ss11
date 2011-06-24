package ttp.metaheuristic;

public interface ILocalSearch<T> extends Cloneable {
    T doLocalSearch(T initialSolution);

    ILocalSearch<T> clone() throws CloneNotSupportedException;

    void setLocalSearchStatistics(LocalSearchStatistics localSearchStatistics);
    LocalSearchStatistics getLocalSearchStatistics();
}

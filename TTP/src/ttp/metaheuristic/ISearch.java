package ttp.metaheuristic;

public interface ISearch<I, S> {
    S doSearch(I instance);

    void setSearchStatistics(SearchStatistics searchStatistics);

    SearchStatistics getSearchStatistics();
}

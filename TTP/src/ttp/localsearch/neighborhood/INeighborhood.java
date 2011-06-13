package ttp.localsearch.neighborhood;

public interface INeighborhood<S> {
	void init(S solution);
	boolean hasNext();
	S getNext();
}

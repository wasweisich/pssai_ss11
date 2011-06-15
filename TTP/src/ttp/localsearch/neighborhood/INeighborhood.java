package ttp.localsearch.neighborhood;

public interface INeighborhood<S> {
	S getNext();
	boolean hasNext();
	void init(S solution);
}

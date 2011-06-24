package ttp.localsearch.neighborhood;

public interface INeighborhood<S extends Cloneable> extends Cloneable {
	S getNext();
	boolean hasNext();
	void init(S solution);
    INeighborhood<S> clone() throws CloneNotSupportedException;
}

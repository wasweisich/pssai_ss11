package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class NeighborhoodUnion<T extends Cloneable> implements INeighborhood<T> {

	private Collection<INeighborhood<T>> neighborhoods = new ArrayList<INeighborhood<T>>();
	private Iterator<INeighborhood<T>> neighborIt;
	private INeighborhood<T> currentNeighborhood;
	private T baseSolution;

    @SuppressWarnings({"unchecked"})
    @Override
    public INeighborhood<T> clone() throws CloneNotSupportedException {
        NeighborhoodUnion<T> clone = (NeighborhoodUnion<T>) super.clone();

        clone.neighborhoods = new ArrayList<INeighborhood<T>>(neighborhoods.size());
        for (INeighborhood<T> neighborhood : neighborhoods)
            clone.neighborhoods.add(neighborhood.clone());

        return clone;
    }

	@Override
	public T getNext() {

		if (currentNeighborhood.hasNext())
			return currentNeighborhood.getNext();
		else {
			while (!currentNeighborhood.hasNext() && neighborIt.hasNext()) {
				currentNeighborhood = neighborIt.next();
				currentNeighborhood.init(baseSolution);
			}

			if (currentNeighborhood.hasNext())
				return currentNeighborhood.getNext();
			else
				return null;
		}
	}

	@Override
	public boolean hasNext() {

		while (!currentNeighborhood.hasNext() && neighborIt.hasNext()) {
			currentNeighborhood = neighborIt.next();
			currentNeighborhood.init(baseSolution);
		}

		if (currentNeighborhood.hasNext())
			return true;

		return false;
	}

	@Override
	public void init(T solution) {
		baseSolution = solution;

		neighborIt = neighborhoods.iterator();
		currentNeighborhood = neighborIt.next();
		currentNeighborhood.init(baseSolution);
	}

    public void addNeighborhood(INeighborhood<T> n) {
		neighborhoods.add(n);
	}

}

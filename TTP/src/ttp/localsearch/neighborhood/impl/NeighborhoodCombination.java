package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;

public abstract class NeighborhoodCombination<K extends INeighborhood<T>, J extends INeighborhood<T>, T>
		implements INeighborhood<T> {

	protected K neighborhood_one;
	protected J neighborhood_Two;
	protected T currentBase = null;
	protected T baseSolution = null;

	@Override
	public T getNext() {

		if (currentBase == null) {
			if (neighborhood_one.hasNext()) {
				currentBase = neighborhood_one.getNext();
				neighborhood_Two.init(currentBase);
				return currentBase;
			} else
				return null;
		} else {
			if (neighborhood_Two.hasNext()) {
				return neighborhood_Two.getNext();
			} else {
				if (neighborhood_one.hasNext()) {
					currentBase = neighborhood_one.getNext();

					neighborhood_Two.init(currentBase);
					return currentBase;
				} else
					return null;
			}
		}
	}

	@Override
	public boolean hasNext() {
		if (currentBase == null) {
			if (neighborhood_one.hasNext()) {
				return true;
			} else
				return false;
		} else {
			if (neighborhood_Two.hasNext()) {
				return true;
			} else {
				if (neighborhood_one.hasNext()) {
					return true;
				} else
					return false;
			}
		}
	}

	@Override
	public void init(T solution) {
		this.baseSolution = solution;
		neighborhood_one.init(baseSolution);
		currentBase = null;
	}

}

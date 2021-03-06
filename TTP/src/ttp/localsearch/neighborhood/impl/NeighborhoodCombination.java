package ttp.localsearch.neighborhood.impl;

import ttp.localsearch.neighborhood.INeighborhood;

import java.util.ArrayList;

public class NeighborhoodCombination<T extends Cloneable> implements INeighborhood<T> {

    // private Stack<INeighborhood<TTPSolution>> stack = new
    // Stack<INeighborhood<TTPSolution>>();

    private ArrayList<INeighborhood<T>> neighborhoods = new ArrayList<INeighborhood<T>>();
    private INeighborhood<T> currentNeighborhood = null;
    private int nIndex = 0;
    private boolean newNeighborhood = true;
    private T lastSolution;

    @SuppressWarnings({"unchecked"})
    @Override
    public INeighborhood<T> clone() throws CloneNotSupportedException {
        NeighborhoodCombination<T> clone = (NeighborhoodCombination<T>) super.clone();

        clone.nIndex = nIndex;
        clone.newNeighborhood = newNeighborhood;
        clone.lastSolution= lastSolution;
        clone.currentNeighborhood = currentNeighborhood.clone();
        clone.neighborhoods = new ArrayList<INeighborhood<T>>(neighborhoods.size());

        for (INeighborhood<T> neighborhood : neighborhoods)
            clone.neighborhoods.add(neighborhood.clone());

        return clone;
    }

    public void addNeighborhood(INeighborhood<T> n) {
        neighborhoods.add(n);
    }

    @Override
    public T getNext() {
        if (newNeighborhood) {
            currentNeighborhood = neighborhoods.get(nIndex);
            currentNeighborhood.init(lastSolution);

            if ((nIndex + 1) < neighborhoods.size()) {
                nIndex++;
                newNeighborhood = true;
                return getNext();
            } else {
                newNeighborhood = false;
                lastSolution = currentNeighborhood.getNext();

                return lastSolution;
            }
        } else {
            while (!currentNeighborhood.hasNext()) {
                nIndex--;
                newNeighborhood = false;
                if (nIndex < 0)
                    break;
                currentNeighborhood = neighborhoods.get(nIndex);
            }

            if (nIndex >= 0) {
                lastSolution = currentNeighborhood.getNext();
                if ((nIndex + 1) < neighborhoods.size()) {
                    nIndex++;
                    newNeighborhood = true;
                }

                return lastSolution;
            } else
                return null;
        }
    }

    @Override
    public boolean hasNext() {
        if (currentNeighborhood != null) {
            while (!currentNeighborhood.hasNext() && !newNeighborhood) {
                nIndex--;
                newNeighborhood = false;
                if (nIndex < 0)
                    break;
                currentNeighborhood = neighborhoods.get(nIndex);
            }
        }
        if (nIndex >= 0)
            return true;

        return false;
    }

    @Override
    public void init(T solution) {
        lastSolution = solution;

        newNeighborhood = true;
        nIndex = 0;
    }

}

package ttp.localsearch.neighborhood.impl;

import java.util.ArrayList;

import ttp.localsearch.neighborhood.INeighborhood;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class TtpNeighborhoodCombination implements INeighborhood<TTPSolution> {

	// private Stack<INeighborhood<TTPSolution>> stack = new
	// Stack<INeighborhood<TTPSolution>>();

	private ArrayList<INeighborhood<TTPSolution>> neighborhoods = new ArrayList<INeighborhood<TTPSolution>>();
	private INeighborhood<TTPSolution> currentNeighborhood = null;
	private int nIndex = 0;
	private boolean newNeighborhood = true;
	private TTPSolution lastSolution;

	public void addNeighborhood(INeighborhood<TTPSolution> n) {
		neighborhoods.add(n);
	}

	@Override
	public TTPSolution getNext() {
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
	public void init(TTPSolution solution) {
		lastSolution = solution;

		newNeighborhood = true;
		nIndex = 0;
	}

}

package ttp.metaheuristic.tabu;

import java.util.LinkedList;

import ttp.model.TTPSolution;

public class SimpleTTPTabuList implements ITabuList<TTPSolution> {

	private LinkedList<TTPSolution> tabuList;
	private int maxSize = 5;

	public SimpleTTPTabuList() {
		tabuList = new LinkedList<TTPSolution>();
	}

	public SimpleTTPTabuList(int tabuListLength) {
		this();
		maxSize = tabuListLength;
	}

	@Override
	public void add(TTPSolution object) {
		tabuList.addLast(object);

		// check for tabuList length
		if (tabuList.size() > maxSize) {
			while (tabuList.size() > maxSize) {
				tabuList.removeFirst();
			}
		}

	}

	@Override
	public boolean contains(TTPSolution sol) {
		for (TTPSolution tabu : tabuList) {
			boolean equals = true;
			for (int round = 0; round < sol.getSchedule().length && equals; round++) {
				for (int team = 0; team < sol.getSchedule()[0].length && equals; team++) {
					if (sol.getSchedule()[round][team] != tabu.getSchedule()[round][team]) {
						equals = false;
					}
				}
			}

			if (equals)
				return true;
		}
		return false;
	}

	@Override
	public int getMaxSize() {
		return maxSize;
	}

	@Override
	public void setMaxSize(int size) {
		this.maxSize = size;
	}

}

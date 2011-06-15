package ttp.constructionheuristics;

import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

public class TtpRandomConstructionHeuristic implements
		IConstructionHeuristics<TTPInstance, TTPSolution> {

	private TTPInstance problemInstance;

	@Override
	public TTPSolution getInitialSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public TTPInstance getProblemInstance() {
		return problemInstance;
	}

	@Override
	public void setProblemInstance(TTPInstance instance) {
		this.problemInstance = instance;
	}

}

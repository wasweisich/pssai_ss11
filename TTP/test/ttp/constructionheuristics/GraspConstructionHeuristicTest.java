package ttp.constructionheuristics;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

import ttp.io.TTPProblemInstanceReader;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class GraspConstructionHeuristicTest {

	private TTPInstance instance;

	private GraspConstructionHeuristic cs;

	@Before
	public void setUp() throws Exception {
		File file = new File("testinstances" + File.separator + "data6.txt");

		instance = TTPProblemInstanceReader.readProblemInstance(file);
		cs = new GraspConstructionHeuristic();
		cs.setProblemInstance(instance);
	}

	@Test
	public void testConstructionHeuristics_shouldReturnNewSolution() {
		TTPSolution initialSol = cs.getInitialSolution();

		assertThat(initialSol, notNullValue());
	}

	@Test
	public void testConstructionHeuristics_shouldFullfillHardConstraints() {
		TTPSolution initialSol = cs.getInitialSolution();

		assertThat(TtpSolutionHelper.checkSolution(initialSol), is(true));
	}

	@Test
	public void testConstructionHeuristics_shouldReturnDifferentSolution() {
		TTPSolution initialSol = cs.getInitialSolution();

		assertThat(initialSol, notNullValue());

		assertThat(TtpSolutionHelper.checkSolution(initialSol), is(true));

		TTPSolution nextSol = cs.getInitialSolution();

		assertThat(nextSol.getSchedule(), is(not(initialSol.getSchedule())));

		assertThat(TtpSolutionHelper.checkSolution(nextSol), is(true));
	}

	@Test
	public void testConstructionHeuristics_shouldReturnDifferentSolution_2() {

		ArrayList<TTPSolution> solutions = new ArrayList<TTPSolution>();

		for (int i = 0; i < 20; i++) {
			TTPSolution sol = cs.getInitialSolution();

			for (TTPSolution existingSolutions : solutions) {
				assertThat(existingSolutions.getSchedule(),
						not(sol.getSchedule()));
			}

			solutions.add(sol);
		}

	}

}

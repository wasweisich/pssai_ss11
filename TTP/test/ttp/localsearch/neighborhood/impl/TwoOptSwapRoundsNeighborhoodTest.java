package ttp.localsearch.neighborhood.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ttp.constructionheuristics.SimpleConstruction;
import ttp.model.TTPSolution;

public class TwoOptSwapRoundsNeighborhoodTest {
	private TwoOptSwapRoundsNeighborhood swapRounds;

	private SimpleConstruction sc;

	@Before
	public void setUp() {
		swapRounds = new TwoOptSwapRoundsNeighborhood();

		sc = new SimpleConstruction();
	}

	@Test
	public void testGetNext_shouldReturnNextSolution_1() {
		sc.setNoTeams(4);
		swapRounds.init(sc.getInitialSolution());

		TTPSolution sol = swapRounds.getNext();
		assertThat(sol, notNullValue());
	}

	@Test
	public void testGetNext_shouldReturnSwitchedRounds_1() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapRounds.init(initSol);

		TTPSolution sol = swapRounds.getNext();
		assertThat(sol.getSchedule()[0][0], is(initSol.getSchedule()[1][0]));
		assertThat(sol.getSchedule()[0][1], is(initSol.getSchedule()[1][1]));
		assertThat(sol.getSchedule()[0][2], is(initSol.getSchedule()[1][2]));
		assertThat(sol.getSchedule()[0][3], is(initSol.getSchedule()[1][3]));
	}

	@Test
	public void testHasNext_shouldReturnTrue_1() {
		sc.setNoTeams(4);
		swapRounds.init(sc.getInitialSolution());

		assertThat(swapRounds.hasNext(), is(true));
	}

	@Test
	public void testNoNeighbours_shouldReturnDifferentSolutions() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapRounds.init(initSol);

		ArrayList<TTPSolution> solutions = new ArrayList<TTPSolution>();

		for (int i = 0; i < 6; i++) {
			TTPSolution sol = swapRounds.getNext();

			for (TTPSolution existingSolutions : solutions) {
				assertThat(existingSolutions.getSchedule(),
						not(sol.getSchedule()));
			}

			solutions.add(sol);
		}
	}

	@Test
	public void testNoNeighbours_shouldReturnSix() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapRounds.init(initSol);

		for (int i = 0; i < 6; i++) {
			assertThat(swapRounds.hasNext(), is(true));
			swapRounds.getNext();
		}

		assertThat(swapRounds.hasNext(), is(false));
	}

}

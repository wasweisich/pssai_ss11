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

public class TwoOptSwapTeamsNeighborhoodTest {
	private TwoOptSwapTeamsNeighborhood swapTeams;

	private SimpleConstruction sc;

	@Before
	public void setUp() {
		swapTeams = new TwoOptSwapTeamsNeighborhood();

		sc = new SimpleConstruction();
	}

	@Test
	public void testHasNext_shouldReturnTrue_1() {
		sc.setNoTeams(4);
		swapTeams.init(sc.getInitialSolution());

		assertThat(swapTeams.hasNext(), is(true));
	}

	@Test
	public void testGetNext_shouldReturnNextSolution_1() {
		sc.setNoTeams(4);
		swapTeams.init(sc.getInitialSolution());

		TTPSolution sol = swapTeams.getNext();
		assertThat(sol, notNullValue());
	}

	@Test
	public void testGetNext_shouldReturnSwitchedHomeMatch_1() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapTeams.init(initSol);

		int[] shouldBeFirstTeam = { -2, 4, 3, 2, -4, -3 };
		int[] shouldBeSecondTeam = { 1, 3, 4, -1, -3, -4 };

		TTPSolution sol = swapTeams.getNext();
		assertThat(sol.getSchedule()[0], is(shouldBeFirstTeam));
		assertThat(sol.getSchedule()[1], is(shouldBeSecondTeam));
	}

	@Test
	public void testNoNeighbours_shouldReturnSix() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapTeams.init(initSol);

		for (int i = 0; i < 6; i++) {
			assertThat(swapTeams.hasNext(), is(true));
			swapTeams.getNext();
		}

		assertThat(swapTeams.hasNext(), is(false));
	}

	@Test
	public void testNoNeighbours_shouldReturnDifferentSolutions() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapTeams.init(initSol);

		ArrayList<TTPSolution> solutions = new ArrayList<TTPSolution>();

		for (int i = 0; i < 6; i++) {
			TTPSolution sol = swapTeams.getNext();

			for (TTPSolution existingSolutions : solutions) {
				assertThat(existingSolutions.getSchedule(),
						not(sol.getSchedule()));
			}

			solutions.add(sol);
		}
	}

}

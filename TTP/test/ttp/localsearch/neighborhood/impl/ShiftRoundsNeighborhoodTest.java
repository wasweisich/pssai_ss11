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

public class ShiftRoundsNeighborhoodTest {
	private ShiftRoundNeighborhood shiftRound;

	private SimpleConstruction sc;

	@Before
	public void setUp() {
		shiftRound = new ShiftRoundNeighborhood();

		sc = new SimpleConstruction();
	}

	@Test
	public void testGetNext_shouldReturnNextSolution_1() {
		sc.setNoTeams(4);
		shiftRound.init(sc.getInitialSolution());

		TTPSolution sol = shiftRound.getNext();
		assertThat(sol, notNullValue());
	}

	@Test
	public void testGetNext_shouldReturnSwitchedRounds_1() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		shiftRound.init(initSol);

		TTPSolution sol = shiftRound.getNext();
		assertThat(sol.getSchedule()[0][0], is(initSol.getSchedule()[1][0]));
		assertThat(sol.getSchedule()[0][1], is(initSol.getSchedule()[1][1]));
		assertThat(sol.getSchedule()[0][2], is(initSol.getSchedule()[1][2]));
		assertThat(sol.getSchedule()[0][3], is(initSol.getSchedule()[1][3]));
		
		assertThat(sol.getSchedule()[1][0], is(initSol.getSchedule()[0][0]));
		assertThat(sol.getSchedule()[1][1], is(initSol.getSchedule()[0][1]));
		assertThat(sol.getSchedule()[1][2], is(initSol.getSchedule()[0][2]));
		assertThat(sol.getSchedule()[1][3], is(initSol.getSchedule()[0][3]));
		
		
		sol = shiftRound.getNext();
		assertThat(sol.getSchedule()[0][0], is(initSol.getSchedule()[1][0]));
		assertThat(sol.getSchedule()[0][1], is(initSol.getSchedule()[1][1]));
		assertThat(sol.getSchedule()[0][2], is(initSol.getSchedule()[1][2]));
		assertThat(sol.getSchedule()[0][3], is(initSol.getSchedule()[1][3]));
		
		assertThat(sol.getSchedule()[1][0], is(initSol.getSchedule()[2][0]));
		assertThat(sol.getSchedule()[1][1], is(initSol.getSchedule()[2][1]));
		assertThat(sol.getSchedule()[1][2], is(initSol.getSchedule()[2][2]));
		assertThat(sol.getSchedule()[1][3], is(initSol.getSchedule()[2][3]));
	}

	@Test
	public void testHasNext_shouldReturnTrue_1() {
		sc.setNoTeams(4);
		shiftRound.init(sc.getInitialSolution());

		assertThat(shiftRound.hasNext(), is(true));
	}

	@Test
	public void testNoNeighbours_shouldReturnDifferentSolutions() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		shiftRound.init(initSol);

		ArrayList<TTPSolution> solutions = new ArrayList<TTPSolution>();

		for (int i = 0; i < 15; i++) {
			TTPSolution sol = shiftRound.getNext();

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
		shiftRound.init(initSol);

		for (int i = 0; i < 15; i++) {
			assertThat(shiftRound.hasNext(), is(true));
			shiftRound.getNext();
		}

		assertThat(shiftRound.hasNext(), is(false));
	}

}

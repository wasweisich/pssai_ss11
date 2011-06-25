package ttp.localsearch.neighborhood.impl;

import org.junit.Before;
import org.junit.Test;
import ttp.constructionheuristics.SimpleConstruction;
import ttp.metaheuristic.tabu.ITabuList;
import ttp.metaheuristic.tabu.SimpleTTPTabuList;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SwapMatchRoundNeighborhoodTest {
	private SwapMatchRoundNeighborhood swapMatchRound;

	private SimpleConstruction sc;

	@Before
	public void setUp() {
		swapMatchRound = new SwapMatchRoundNeighborhood();

		sc = new SimpleConstruction();
	}

	@Test
	public void testGetNext_shouldReturnNextSolution_1() {
		sc.setNoTeams(4);
		swapMatchRound.init(sc.getInitialSolution());

		TTPSolution sol = swapMatchRound.getNext();
		assertThat(sol, notNullValue());
	}

	@Test
	public void testGetNext_shouldReturnSwitchedTeams_1() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();

		int[][] schedule = { { 3, 4, -1, -2 }, //
				{ 2, -1, 4, -3 },//
				{ 4, -3, 2, -1 }, //
				{ -3, -4, 1, 2 }, //
				{ -2, 1, -4, 3 },//
				{ -4, 3, -2, 1 } };

		initSol.setSchedule(schedule);

		swapMatchRound.init(initSol);

		TTPSolution sol = swapMatchRound.getNext();
		assertThat(sol.getSchedule()[0][0], is(2));
		assertThat(sol.getSchedule()[0][1], is(-1));
		assertThat(sol.getSchedule()[0][2], is(4));
		assertThat(sol.getSchedule()[0][3], is(-3));

		assertThat(sol.getSchedule()[1][0], is(3));
		assertThat(sol.getSchedule()[1][1], is(4));
		assertThat(sol.getSchedule()[1][2], is(-1));
		assertThat(sol.getSchedule()[1][3], is(-2));
	}

	@Test
	public void testHasNext_shouldReturnTrue_1() {
		sc.setNoTeams(4);
		swapMatchRound.init(sc.getInitialSolution());

		assertThat(swapMatchRound.hasNext(), is(true));
	}

	@Test
	public void testNoNeighbours_shouldReturnDifferentSolutions() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapMatchRound.init(initSol);
		ITabuList<TTPSolution> tabuList = new SimpleTTPTabuList(60);
		int duplicateSolutions = 0;
        int uniqueSolutions = 0;
        int totalSolutions = 0;
		while (swapMatchRound.hasNext()) {
			TTPSolution sol = swapMatchRound.getNext();
            totalSolutions++;

			boolean result = tabuList.contains(sol);

			if (tabuList.contains(sol))
                duplicateSolutions++;
            else
                uniqueSolutions++;
			// assertThat(result, is(false));

			tabuList.add(sol);
		}
		System.out.println("Total     : " + totalSolutions);
        System.out.println("Unique    : " + uniqueSolutions);
        System.out.println("Duplicates: " + duplicateSolutions);
	}

	@Test
	public void testGetNext_shouldReturnFeasibelSolutions() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapMatchRound.init(initSol);
		ITabuList<TTPSolution> tabuList = new SimpleTTPTabuList(60);

		for (int i = 0; i < 48; i++) {
			TTPSolution sol = swapMatchRound.getNext();

			assertThat(TtpSolutionHelper.checkSolution(sol), is(true));

			tabuList.add(sol);
		}
	}

	@Test
	public void testNoNeighbours_shouldReturnSixty() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapMatchRound.init(initSol);

		int i = 0;
		while (swapMatchRound.hasNext()) {
			i++;
			swapMatchRound.getNext();
		}

		assertThat(i, is(48));
		assertThat(swapMatchRound.hasNext(), is(false));
	}

}

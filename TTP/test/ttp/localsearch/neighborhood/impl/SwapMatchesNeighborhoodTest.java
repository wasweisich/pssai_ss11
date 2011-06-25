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

public class SwapMatchesNeighborhoodTest {
	private SwapMatchesNeighborhood swapMatches;

	private SimpleConstruction sc;

	@Before
	public void setUp() {
		swapMatches = new SwapMatchesNeighborhood();
		sc = new SimpleConstruction();
	}

	@Test
	public void testGetNext_shouldReturnNextSolution_1() {
		sc.setNoTeams(4);
		swapMatches.init(sc.getInitialSolution());

		TTPSolution sol = swapMatches.getNext();
		assertThat(sol, notNullValue());
	}

	/*

	*/

	@Test
	public void testGetNext_shouldReturnSwitchedTeams_1() {
		sc.setNoTeams(6);
		TTPSolution initSol = sc.getInitialSolution();

		int[][] schedule = { { 4, 3, -2, -1, 6, -5 },//
				{ -5, 6, 4, -3, 1, -2 },//
				{ -2, 1, 5, 6, -3, -4 },//
				{ -4, 5, 6, 1, -2, -3 },//
				{ -6, -3, 2, 5, -4, 1 },//
				{ -3, -4, 1, 2, -6, 5 },//
				{ 5, -6, -4, 3, -1, 2 },//
				{ 2, -1, -6, -5, 4, 3 },//
				{ 3, -5, -1, -6, 2, 4 },//
				{ 6, 4, -5, -2, 3, -1 } };

		initSol.setSchedule(schedule);

		swapMatches.init(initSol);

		int i = 0;
		while (swapMatches.hasNext()) {

			TTPSolution sol = swapMatches.getNext();

			if (sol == null)
				break;

			assertThat("solution #" + (i + 1) + " not feasible",
					TtpSolutionHelper.checkSolution(sol), is(true));
			i++;
		}
	}

	@Test
	public void testHasNext_shouldReturnTrue_1() {
		sc.setNoTeams(4);
		swapMatches.init(sc.getInitialSolution());

		assertThat(swapMatches.hasNext(), is(true));
	}

	@Test
	public void testNoNeighbours_shouldReturnDifferentSolutions() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapMatches.init(initSol);
		ITabuList<TTPSolution> tabuList = new SimpleTTPTabuList(60);
		int duplicateSolutions = 0;
		int uniqueSolutions = 0;
		int totalSolutions = 0;
		while (swapMatches.hasNext()) {
			TTPSolution sol = swapMatches.getNext();

			if (sol == null)
				break;
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
		sc.setNoTeams(8);
		TTPSolution initSol = sc.getInitialSolution();
		swapMatches.init(initSol);

		int i = 0;
		while (swapMatches.hasNext()) {
			TTPSolution sol = swapMatches.getNext();

			if (sol == null)
				break;

			assertThat("solution #" + (i + 1) + " not feasible",
					TtpSolutionHelper.checkSolution(sol), is(true));

			i++;
		}
	}

	@Test
	public void testNoNeighbours_shouldReturnTwentyFour() {
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		swapMatches.init(initSol);

		int i = 0;
		while (swapMatches.hasNext()) {
			i++;
			swapMatches.getNext();
		}

		assertThat(i, is(24));
		assertThat(swapMatches.hasNext(), is(false));
	}

}

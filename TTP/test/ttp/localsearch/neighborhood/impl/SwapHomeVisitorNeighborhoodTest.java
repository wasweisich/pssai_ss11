package ttp.localsearch.neighborhood.impl;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import ttp.constructionheuristics.SimpleConstruction;
import ttp.model.TTPSolution;

public class SwapHomeVisitorNeighborhoodTest {

	private SwapHomeVisitorNeighborhood swapHome;

	private SimpleConstruction sc;

	@Before
	public void setUp() {
		swapHome = new SwapHomeVisitorNeighborhood();

		sc = new SimpleConstruction();
	}

	@Test
	public void testHasNext_shouldReturnTrue_1() {
		sc.setNoTeams(4);
		swapHome.init(sc.getInitialSolution());

		assertThat(swapHome.hasNext(), is(true));
	}

	@Test
	public void testGetNext_shouldReturnNextSolution_1() {
		sc.setNoTeams(4);
		swapHome.init(sc.getInitialSolution());

		TTPSolution sol = swapHome.getNext();
		assertThat(sol, notNullValue());
	}
	
	@Test
	public void testGetNext_shouldReturnSwitchedHomeMatch_1() {
		sc.setNoTeams(4);
		TTPSolution initSol =sc.getInitialSolution();
		swapHome.init(initSol);
		
		int firstMatch =initSol.getSchedule()[0][0];
		

		TTPSolution sol = swapHome.getNext();
		assertThat(sol.getSchedule()[0][0], is(-firstMatch));
		assertThat(sol.getSchedule()[firstMatch-1][0], is(1));
	}

}

package ttp.localsearch.neighborhood.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import ttp.constructionheuristics.SimpleConstruction;
import ttp.model.TTPSolution;

public class TtpNeighborhoodCombinationTest {

	private TtpNeighborhoodCombination neighborhood;
	private SimpleConstruction sc;

	@Before
	public void setUp() {
		neighborhood = new TtpNeighborhoodCombination();


		sc = new SimpleConstruction();
	}

	@Test
	public void testNoNeighbours_shouldReturnProduct() {
		
		neighborhood.addNeighborhood(new TwoOptSwapRoundsNeighborhood());
		neighborhood.addNeighborhood(new TwoOptSwapTeamsNeighborhood());
		neighborhood.addNeighborhood(new SwapHomeVisitorNeighborhood());

		
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		neighborhood.init(initSol);
		int i = 0;
		while (neighborhood.hasNext()) {
			i++;
			neighborhood.getNext();
		}
		assertThat(i, is(16 * 7 * 7-1));
		assertThat(neighborhood.hasNext(), is(false));
	}
	
	@Test
	public void testNoNeighbours_shouldReturn15() {
		
		neighborhood.addNeighborhood(new TwoOptSwapRoundsNeighborhood());

		
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		neighborhood.init(initSol);
		int i = 0;
		while (neighborhood.hasNext()) {
			i++;
			neighborhood.getNext();
		}
		assertThat(i, is(15));
		assertThat(neighborhood.hasNext(), is(false));
	}
	
	@Test
	public void testNoNeighbours_shouldReturnProduct49() {

		neighborhood.addNeighborhood(new SwapHomeVisitorNeighborhood());
		neighborhood.addNeighborhood(new SwapHomeVisitorNeighborhood());

		
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		neighborhood.init(initSol);
		int i = 0;
		while (neighborhood.hasNext()) {
			i++;
			neighborhood.getNext();
		}
		assertThat(i, is(7*7-1));
		assertThat(neighborhood.hasNext(), is(false));
	}
	
	@Test
	public void testNoNeighbours_shouldReturnProduct948_1() {

		neighborhood.addNeighborhood(new SwapHomeVisitorNeighborhood());
		neighborhood.addNeighborhood(new TwoOptSwapRoundsNeighborhood());

		
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		neighborhood.init(initSol);
		int i = 0;
		while (neighborhood.hasNext()) {
			i++;
			neighborhood.getNext();
		}
		assertThat(i, is(7*16-1));
		assertThat(neighborhood.hasNext(), is(false));
	}
	
	@Test
	public void testNoNeighbours_shouldReturnProduct48_2() {

		neighborhood.addNeighborhood(new TwoOptSwapTeamsNeighborhood());
		neighborhood.addNeighborhood(new TwoOptSwapTeamsNeighborhood());

		
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		neighborhood.init(initSol);
		int i = 0;
		while (neighborhood.hasNext()) {
			i++;
			neighborhood.getNext();
		}
		assertThat(i, is(7*7-1));
		assertThat(neighborhood.hasNext(), is(false));
	}
	
	@Test
	public void testNoNeighbours_shouldReturnProduct240() {

		neighborhood.addNeighborhood(new TwoOptSwapRoundsNeighborhood());
		neighborhood.addNeighborhood(new TwoOptSwapRoundsNeighborhood());

		
		sc.setNoTeams(4);
		TTPSolution initSol = sc.getInitialSolution();
		neighborhood.init(initSol);
		int i = 0;
		while (neighborhood.hasNext()) {
			i++;
			neighborhood.getNext();
		}
		assertThat(i, is(16*16-1));
		assertThat(neighborhood.hasNext(), is(false));
	}

}

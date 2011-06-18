package ttp.metaheuristic.tabu;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ttp.constructionheuristics.SimpleConstruction;
import ttp.io.TTPProblemInstanceReader;
import ttp.localsearch.neighborhood.impl.NeighborhoodUnion;
import ttp.localsearch.neighborhood.impl.SwapHomeVisitorNeighborhood;
import ttp.localsearch.neighborhood.impl.TwoOptSwapRoundsNeighborhood;
import ttp.localsearch.neighborhood.impl.TwoOptSwapTeamsNeighborhood;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class TabuSearchTest {

	private TTPInstance instance;
	private SimpleConstruction sc;
	private TabuSearch ts;

	@Before
	public void setUp() throws Exception {
		File file = new File("testinstances" + File.separator + "data6.txt");

		instance = TTPProblemInstanceReader.readProblemInstance(file);

		sc = new SimpleConstruction();

		ts = new TabuSearch();
		// ts.setNeighborhood(new TwoOptSwapRoundsNeighborhood());
		NeighborhoodUnion<TTPSolution> neighborhood = new NeighborhoodUnion<TTPSolution>();

		neighborhood.addNeighborhood(new TwoOptSwapRoundsNeighborhood());
		neighborhood.addNeighborhood(new TwoOptSwapTeamsNeighborhood());
		neighborhood.addNeighborhood(new SwapHomeVisitorNeighborhood());
		ts.setNeighborhood(neighborhood);
		// ts.setNeighborhood(new TwoTtpNeighborhoodCombination());

	}

	@Test
	public void testTabuSearch_ShouldReturnSolution_1() {
		sc.setProblemInstance(instance);
		TTPSolution sol = sc.getInitialSolution();

		int[][] schedule = { { -5, -3, 2, 6, 1, -4 },// round 1
				{ -3, 6, 1, -5, 4, -2 },// round 2

				{ 2, -1, -4, 3, -6, 5 },// round 4
				{ 4, -5, 6, -1, 2, -3 },// round 3
				{ -6, 3, -2, 5, -4, 1 },// round 5
				{ -4, -6, 5, 1, -3, 2 },// round 6
				{ -2, 1, 4, -3, 6, -5 },// round 7
				{ 3, 5, -1, -6, -2, 4 },// round 9
				{ 6, 4, -5, -2, 3, -1 },// round 8
				{ 5, -4, -6, 2, -1, 3 } // round 10
		};

		sol.setSchedule(schedule);

		TtpSolutionHelper.initializeSolution(sol, instance);

		System.out.println("Start solution - Cost: " + sol.getCost()
				+ " ; Legal: " + sol.isLegal());

		TTPSolution tabuSol = ts.doLocalSearch(sol);

		assertThat(tabuSol, notNullValue());

		assertThat(TtpSolutionHelper.checkSolution(tabuSol), is(true));

		System.out.println("Result - Cost: " + tabuSol.getCost() + " ; Legal: "
				+ tabuSol.isLegal());

	}
}

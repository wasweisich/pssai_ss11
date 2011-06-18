package ttp.util;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import ttp.constructionheuristics.SimpleConstruction;
import ttp.io.TTPProblemInstanceReader;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

public class TtpSolutionHelperTest {
	private TTPInstance instance;
	private SimpleConstruction sc;

	@Before
	public void setUp() throws Exception {
		File file = new File("testinstances" + File.separator + "data4.txt");

		instance = TTPProblemInstanceReader.readProblemInstance(file);

		sc = new SimpleConstruction();
	}

	@Test
	public void testCalculateCosts_shouldReturn23916() throws Exception {

		File file = new File("testinstances" + File.separator + "data6.txt");

		instance = TTPProblemInstanceReader.readProblemInstance(file);

		int[][] schedule = { { 5, -6, -4, 3, -1, 2 }, { 2, -1, 5, -6, -3, 4 },
				{ 6, -5, 4, -3, 2, -1 }, { -3, 4, 1, -2, 6, -5 },
				{ -4, 5, -6, 1, -2, 3 }, { -6, -3, 2, 5, -4, 1 },
				{ 3, -4, -1, 2, -6, 5 }, { 4, 6, -5, -1, 3, -2 },
				{ -2, 1, 6, -5, 4, -3 }, { -5, 3, -2, 6, 1, -4 } };

		int result = TtpSolutionHelper.calculateCosts(schedule, instance);
		assertThat(result, is(23916));
	}

	@Test
	public void testCalculateCosts_shouldReturn23978() throws Exception {

		File file = new File("testinstances" + File.separator + "data6.txt");

		instance = TTPProblemInstanceReader.readProblemInstance(file);

		int[][] schedule = { { -5, -3, 2, 6, 1, -4 }, { -3, 6, 1, -5, 4, -2 },
				{ 4, -5, 6, -1, 2, -3 }, { 2, -1, -4, 3, -6, 5 },
				{ -6, 3, -2, 5, -4, 1 }, { -4, -6, 5, 1, -3, 2 },
				{ -2, 1, 4, -3, 6, -5 }, { 6, 4, -5, -2, 3, -1 },
				{ 3, 5, -1, -6, -2, 4 }, { 5, -4, -6, 2, -1, 3 } };

		int result = TtpSolutionHelper.calculateCosts(schedule, instance);
		assertThat(result, is(23978));
	}

	@Test
	public void testCalculateCosts_shouldReturn8276() {
		int[][] schedule = { { 3, 4, -1, -2 }, { 2, -1, 4, -3 },
				{ 4, -3, 2, -1 }, { -3, -4, 1, 2 }, { -2, 1, -4, 3 },
				{ -4, 3, -2, 1 } };

		int result = TtpSolutionHelper.calculateCosts(schedule, instance);
		assertThat(result, is(8276));
	}

	@Test
	public void testCalculateTeamCosts_shouldReturn2() {
		int[][] d = {//
		{ 0, 1, 1, 1 },//
				{ 1, 0, 1, 1 },//
				{ 1, 1, 0, 1 },//
				{ 1, 1, 1, 0 } //
		};
		instance.setD(d);

		int[][] schedule = { { -4, 4, -1, -2 },//
				{ -4, -1, 4, -3 },//
				{ -4, -3, 2, -1 }, //
				{ -4, -4, 1, 2 },//
				{ -4, 1, -4, 3 }, //
				{ -4, 3, -2, 1 } };

		int result = TtpSolutionHelper
				.calculateTeamCosts(schedule, 1, instance);
		assertThat(result, is(2));
	}

	@Test
	public void testCalculateTeamCosts_shouldReturn4() {
		int[][] d = {//
		{ 0, 1, 1, 1 },//
				{ 1, 0, 1, 1 },//
				{ 1, 1, 0, 1 },//
				{ 1, 1, 1, 0 } //
		};
		instance.setD(d);

		int[][] schedule = { { -4, 4, -1, -2 },//
				{ -4, -1, 4, -3 },//
				{ -4, -3, 2, -1 }, //
				{ 4, -4, 1, 2 },//
				{ -4, 1, -4, 3 }, //
				{ -4, 3, -2, 1 } };

		int result = TtpSolutionHelper
				.calculateTeamCosts(schedule, 1, instance);
		assertThat(result, is(4));
	}

	@Test
	public void testCalculateTeamCosts_shouldReturn8() {
		int[][] d = {//
		{ 0, 2, 1, 1 },//
				{ 2, 0, 2, 2 },//
				{ 1, 2, 0, 1 },//
				{ 1, 2, 1, 0 } //
		};
		instance.setD(d);

		int[][] schedule = { { -4, -1, -1, -2 },//
				{ -4, -1, 4, -3 },//
				{ -4, 3, 2, -1 }, //
				{ 4, 3, 1, 2 },//
				{ -4, -4, -4, 3 }, //
				{ -4, -4, -2, 1 } };

		int result = TtpSolutionHelper
				.calculateTeamCosts(schedule, 2, instance);
		assertThat(result, is(8));
	}

	@Test
	public void testCalculateTeamCosts_shouldReturn0() {
		int[][] d = {//
		{ 0, 1, 1, 1 },//
				{ 1, 0, 1, 1 },//
				{ 1, 1, 0, 1 },//
				{ 1, 1, 1, 0 } //
		};
		instance.setD(d);

		int[][] schedule = { { 3, 4, -1, -2 },//
				{ 3, -1, 4, -3 },//
				{ 3, -3, 2, -1 }, //
				{ 3, -4, 1, 2 },//
				{ 3, 1, -4, 3 }, //
				{ 3, 3, -2, 1 } };

		int result = TtpSolutionHelper
				.calculateTeamCosts(schedule, 1, instance);
		assertThat(result, is(0));
	}

	@Test
	public void testCalculateTeamCosts_shouldReturn() {
		sc.setProblemInstance(instance);
		TTPSolution sol = sc.getInitialSolution();

		int result = TtpSolutionHelper.calculateTeamCosts(sol.getSchedule(), 1,
				instance);
		assertThat(result, is(2127));
	}

}

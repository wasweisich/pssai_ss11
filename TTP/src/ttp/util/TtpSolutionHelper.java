package ttp.util;

import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

public class TtpSolutionHelper {

	public static int calculateCosts(int[][] schedule, TTPInstance instance) {
		int costs = 0;

		for (int teamNo = 0; teamNo < schedule[0].length; teamNo++) {
			costs += calculateTeamCosts(schedule, teamNo + 1, instance);
		}

		return costs;
	}

	private static int calculateTeamCosts(int teamNo, TTPSolution sol) {
		return calculateTeamCosts(sol.getSchedule(), teamNo,
				sol.getProblemInstance());
	}

	public static int calculateTeamCosts(int[][] teamSchedule, int teamNo,
			TTPInstance instance) {
		int costs = 0;

		int currentLocation = teamNo;

		for (int i = 0; i < instance.getNoRounds(); i++) {
			if (teamSchedule[i][teamNo - 1] < 0) {
				// add costs from currentLocation to other team
				int otherTeam = -teamSchedule[i][teamNo - 1];
				costs += instance.getD()[currentLocation - 1][otherTeam - 1];

				currentLocation = otherTeam;
			} else {
				// home match
				if (currentLocation != teamNo) {
					// add costs to drive home
					costs += instance.getD()[currentLocation - 1][teamNo - 1];

					currentLocation = teamNo;
				}
			}
		}

		// last way home
		// add costs to drive home
		costs += instance.getD()[currentLocation - 1][teamNo - 1];

		return costs;
	}

	private static int calculateWeakViolations(int teamNo, TTPSolution sol) {
		return calculateWeakViolations(sol.getSchedule(), teamNo,
				sol.getProblemInstance());
	}

	public static int calculateWeakViolations(int[][] teamSchedule, int teamNo,
			TTPInstance instance) {
		int violationCounter = 0;

		int counter = 0;
		boolean home = true;
		int lastEnemy = 0;

		for (int i = 0; i < instance.getNoRounds(); i++) {

			if (home) {
				if (teamSchedule[i][teamNo - 1] > 0) {
					counter++;
				} else {
					home = false;
					counter = 1;
				}
			} else {
				if (teamSchedule[i][teamNo - 1] < 0) {
					counter++;
				} else {
					home = true;
					counter = 1;
				}
			}

			if (counter > instance.getU())
				violationCounter++;

			// it is not allowed to play against a team in two consecutive games
			if (Math.abs(teamSchedule[i][teamNo - 1]) == lastEnemy)
				violationCounter++;

			lastEnemy = Math.abs(teamSchedule[i][teamNo - 1]);

		}

		return violationCounter;
	}

	public static double[] copyArray(double[] array) {
		if (array != null) {
			double[] result = new double[array.length];

			for (int i = 0; i < array.length; i++) {
				result[i] = array[i];
			}

			return result;
		} else
			return null;
	}

	public static int[] copyArray(int[] array) {
		if (array != null) {
			int[] result = new int[array.length];

			for (int i = 0; i < array.length; i++) {
				result[i] = array[i];
			}

			return result;
		} else
			return null;
	}

	public static int[][] copyArray(int[][] schedule) {
		int[][] result = new int[schedule.length][schedule[0].length];

		for (int i = 0; i < schedule.length; i++) {
			for (int j = 0; j < schedule[0].length; j++) {
				result[i][j] = schedule[i][j];
			}
		}

		return result;
	}

	public static void initializeSolution(TTPSolution sol, TTPInstance instance) {

		sol.setProblemInstance(instance);

		// calculate costs
		sol.setCost(calculateCosts(sol.getSchedule(), instance));
		int[] teamCost = new int[instance.getNoTeams()];
		int[] teamPenalty = new int[instance.getNoTeams()];

		for (int i = 0; i < instance.getNoTeams(); i++) {
			teamCost[i] = calculateTeamCosts(sol.getSchedule(), i + 1, instance);
			teamPenalty[i] = calculateWeakViolations(sol.getSchedule(), i + 1,
					instance);
		}

		sol.setTeamCost(teamCost);
		sol.setSoftConstraintsViolations(teamPenalty);
	}

	public static void updateAll(TTPSolution next) {
		for (int i = 0; i < next.getSchedule()[0].length; i++) {
			updateTeam(next, i + 1);
		}
	}

	public static void updateTeam(TTPSolution sol, int teamNo) {
		// upgrade costs
		int costs = sol.getCost() - sol.getTeamCost()[teamNo - 1];
		sol.getTeamCost()[teamNo - 1] = calculateTeamCosts(teamNo, sol);
		costs += sol.getTeamCost()[teamNo - 1];
		sol.setCost(costs);

		// upgrade weak-constraints violation
		sol.updateSoftConstraintsViolations(teamNo - 1,
				calculateWeakViolations(teamNo, sol));

	}

}

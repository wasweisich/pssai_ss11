package ttp.util;

import org.apache.log4j.Logger;

import ttp.metaheuristic.grasp.GRASP;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;

public class TtpSolutionHelper {
	private static Logger logger = Logger.getLogger(TtpSolutionHelper.class);

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

	public static void updateAll(TTPSolution sol) {
		int costs = 0;
		for (int i = 0; i < sol.getSchedule()[0].length; i++) {

			sol.getTeamCost()[i] = calculateTeamCosts(i + 1, sol);
			costs += sol.getTeamCost()[i];

			// upgrade weak-constraints violation
			sol.updateSoftConstraintsViolations(i,
					calculateWeakViolations(i + 1, sol));
		}

		sol.setCost(costs);
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

	public static boolean checkSolution(TTPSolution next) {
		return checkSolution(next, false);
	}

	public static boolean checkSolution(TTPSolution next, boolean explain) {

		// check if in each round, each teams plays only once
		for (int i = 0; i < next.getSchedule().length; i++) {
			for (int j = 0; j < next.getSchedule()[i].length; j++) {
				int enemy = next.getSchedule()[i][j];
				if (enemy < 0) {
					if (next.getSchedule()[i][-enemy - 1] != j + 1) {
						if (explain)
							logger.info("Different opponent entries in rount "
									+ i + ", Teams " + -enemy + " and "
									+ (j + 1));
						return false;
					}

					if (-enemy == j + 1) {
						if (explain)
							logger.info("Different opponent entries in rount "
									+ i + ", Teams " + -enemy + " and "
									+ (j + 1));
						return false;
					}
				} else {
					if (next.getSchedule()[i][enemy - 1] != -(j + 1)) {
						if (explain)
							logger.info("Different opponent entries in rount "
									+ i + ", Teams " + enemy + " and "
									+ -(j + 1));
						return false;
					}

					if (enemy == j + 1) {
						if (explain)
							logger.info("Different opponent entries in rount "
									+ i + ", Teams " + enemy + " and "
									+ -(j + 1));
						return false;
					}
				}
			}
		}

		// check if each team plays against each other twice

		for (int i = 0; i < next.getSchedule()[0].length; i++) {
			boolean[] homeGames = new boolean[next.getSchedule()[i].length];
			boolean[] visitorGames = new boolean[next.getSchedule()[i].length];

			for (int j = 0; j < next.getSchedule().length; j++) {
				if (next.getSchedule()[j][i] > 0) {
					homeGames[next.getSchedule()[j][i] - 1] = true;
				} else {
					visitorGames[-next.getSchedule()[j][i] - 1] = true;
				}
			}

			for (int k = 0; k < next.getSchedule()[i].length - 1; k++) {
				if (k == i)
					continue;

				if (!homeGames[k]) {
					if (explain)
						logger.info("Wrong amount of homeGames: " + k);
					return false;
				}

				if (!visitorGames[k]) {
					if (explain)
						logger.info("Wrong amount of visitorGames: " + k);
					return false;
				}

			}
		}

		return true;
	}

	public static boolean checkSchedule(int[][] schedule) {
		// check if in each round, each teams plays only once
		for (int i = 0; i < schedule.length; i++) {
			for (int j = 0; j < schedule[i].length; j++) {
				int enemy = schedule[i][j];
				if (enemy < 0) {
					if (schedule[i][-enemy - 1] != j + 1) {

						return false;
					}

					if (-enemy == j + 1) {

						return false;
					}
				} else {
					if (schedule[i][enemy - 1] != -(j + 1)) {

						return false;
					}

					if (enemy == j + 1) {

						return false;
					}
				}
			}
		}

		// check if each team plays against each other twice

		for (int i = 0; i < schedule[0].length; i++) {
			boolean[] homeGames = new boolean[schedule[i].length];
			boolean[] visitorGames = new boolean[schedule[i].length];

			for (int j = 0; j < schedule.length; j++) {
				if (schedule[j][i] > 0) {
					homeGames[schedule[j][i] - 1] = true;
				} else {
					visitorGames[-schedule[j][i] - 1] = true;
				}
			}

			for (int k = 0; k < schedule[i].length - 1; k++) {
				if (k == i)
					continue;

				if (!homeGames[k]) {

					return false;
				}

				if (!visitorGames[k]) {

					return false;
				}

			}
		}

		return true;
	}

}

package ttp.constructionheuristics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import ttp.metaheuristic.tabu.ITabuList;
import ttp.metaheuristic.tabu.SimpleTTPTabuList;
import ttp.model.TTPInstance;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

public class GraspConstructionHeuristic implements
		IConstructionHeuristics<TTPInstance, TTPSolution> {

	private static final double PERCENTAGE = 1.4;
	private static final int TABUSIZE = 100;
	private static final int NEWSCHEDULE_NOTRIES = 10;
	private static final int MAX_NO_TRIES = 50;
	private int noTeams;
	private TTPInstance problemInstance;
	private int upperBound = 3;

	private static Logger logger = Logger
			.getLogger(GraspConstructionHeuristic.class);

	private ITabuList<TTPSolution> tabuList;

	public GraspConstructionHeuristic() {
		tabuList = new SimpleTTPTabuList();
		tabuList.setMaxSize(TABUSIZE);
	}

	public GraspConstructionHeuristic(int noTeams) {
		this.noTeams = noTeams;
	}

	private boolean initialized;
	private int[][] virtualSchedule;
	private TreeSet<IntTripple> virtualValues;
	private ArrayList<SortedSet<IntTripple>> realDistances;
	private VirtualScheduleConstructionMethod method = VirtualScheduleConstructionMethod.FIRSTPOLYGONTHENGREEK;

	private int[][] createSchedule() {
		if (!initialized) {
			createNewVirtualSchedule();
		}

		return getGraspSol();
	}

	private void createNewVirtualSchedule() {

		switch (method) {
		case POLYGON:
			this.virtualSchedule = polygonMethod();
			break;
		case GREEK:
			this.virtualSchedule = greekMethod();
			break;
		case FIRSTPOLYGONTHENGREEK:
		default:
			if (!initialized) {
				this.virtualSchedule = polygonMethod();
			} else {
				this.virtualSchedule = greekMethod();
			}
			break;
		}

		// create matrix
		int[][] matrix = new int[problemInstance.getNoTeams()][problemInstance
				.getNoTeams()];
		int lastVisited = 0;
		for (int teamNo = 0; teamNo < problemInstance.getNoTeams(); teamNo++) {
			for (int w = 0; w < problemInstance.getNoRounds(); w++) {
				if (virtualSchedule[w][teamNo] < 0) {
					if (lastVisited < 0) {
						// increment matrix entry
						matrix[-lastVisited - 1][-virtualSchedule[w][teamNo] - 1]++;
						matrix[-virtualSchedule[w][teamNo] - 1][-lastVisited - 1]++;
					}
					lastVisited = virtualSchedule[w][teamNo];
				} else {
					lastVisited = teamNo;
				}
			}
		}

		// sort
		virtualValues = new TreeSet<IntTripple>();

		for (int i = 0; i < problemInstance.getNoTeams(); i++) {
			for (int j = 0; j < problemInstance.getNoTeams(); j++) {
				if (i == j)
					continue;

				virtualValues.add(new IntTripple(i, j, matrix[i][j]));
			}
		}

		realDistances = new ArrayList<SortedSet<IntTripple>>();

		for (int i = 0; i < problemInstance.getNoTeams(); i++) {
			TreeSet<IntTripple> tmpSS = new TreeSet<IntTripple>(
					new Comparator<IntTripple>() {

						@Override
						public int compare(IntTripple o1, IntTripple o2) {
							return o2.compareTo(o1);
						}
					});

			for (int j = 0; j < problemInstance.getNoTeams(); j++) {
				if (i == j)
					continue;

				tmpSS.add(new IntTripple(i, j, problemInstance.getD()[i][j]));
			}

			realDistances.add(tmpSS);
		}

		initialized = true;
	}

	private int[][] greekMethod() {
		return TtpRandomConstructionHeuristic.genSchedule(
				problemInstance.getNoTeams(), problemInstance.getNoRounds());
	}

	private int[][] polygonMethod() {
		int u = upperBound;
		int noT = noTeams;
		int days = (noTeams - 1) * 2;
		if (problemInstance != null) {
			u = problemInstance.getU();
			noT = problemInstance.getNoTeams();
			days = problemInstance.getNoRounds();
		}
		// neu
		int[][] schedule = new int[days][noT];
		// ende neu
		int half = days / 2;

		int n = noT;
		int[] position = new int[n];

		for (int i = 0; i < n; i++) {
			position[i] = i + 1;
		}

		for (int k = 0; k <= (n - 2); k++) {
			for (int l = 2; l <= (n / 2); l++) {
				// l plays agains n +1 - l
				int oponent = position[n + 1 - l - 1];

				schedule[k][position[l - 1] - 1] = oponent;
				schedule[k][oponent - 1] = -(position[l - 1]);

				schedule[k + half][position[l - 1] - 1] = -(oponent);
				schedule[k + half][oponent - 1] = (position[l - 1]);
			}

			// 1 plays against 6
			schedule[k][position[0] - 1] = position[n - 1];
			schedule[k][position[n - 1] - 1] = -position[0];

			schedule[k + half][position[0] - 1] = -(position[n - 1]);
			schedule[k + half][position[n - 1] - 1] = position[0];

			// shift teams

			for (int i = 0; i < (n - 1); i++) {
				position[i] = (position[i] - 1) % (n - 1);
				if (position[i] == 0)
					position[i] = n - 1;
			}

		}

		return schedule;
	}

	private int[][] getGraspSol() {

		boolean[] real_used = new boolean[problemInstance.getNoTeams()];
		boolean[] vitrual_used = new boolean[problemInstance.getNoTeams()];
		int[] mapping = new int[problemInstance.getNoTeams()];

		TreeSet<IntTripple> virtualValuesCopy = new TreeSet<IntTripple>(
				virtualValues);

		while (!virtualValuesCopy.isEmpty()) {
			// get next entry
			IntTripple first = virtualValuesCopy.first();
			if (vitrual_used[first.getI()] || vitrual_used[first.getJ()]) {
				virtualValuesCopy.remove(first);
				continue;
			}

			// assign teams

			// get candidate list
			LinkedList<IntTripple> candidateList = new LinkedList<IntTripple>();
			int best = Integer.MAX_VALUE;

			// get nearest distance
			for (int i = 0; i < problemInstance.getNoTeams(); i++) {
				if (real_used[i])
					continue;

				SortedSet<IntTripple> tmp = realDistances.get(i);

				for (IntTripple trip : tmp) {
					if (trip.getK() > best)
						break;
					if (!real_used[trip.getI()] && !real_used[trip.getJ()]) {
						best = trip.getK();
						break;
					}
				}
			}

			int limit = (int) (best * PERCENTAGE);

			for (int i = 0; i < problemInstance.getNoTeams(); i++) {
				if (real_used[i])
					continue;

				SortedSet<IntTripple> tmp = realDistances.get(i);

				for (IntTripple trip : tmp) {
					if (trip.getK() > limit)
						break;
					if (!real_used[trip.getI()] && !real_used[trip.getJ()]) {
						candidateList.add(trip);
					}
				}
			}

			// get elem form candidate list
			Random rand = new Random();
			IntTripple choosenOne = candidateList.get(rand
					.nextInt(candidateList.size()));

			// save entry
			real_used[choosenOne.getI()] = true;
			real_used[choosenOne.getJ()] = true;
			vitrual_used[first.getI()] = true;
			vitrual_used[first.getJ()] = true;

			mapping[first.getI()] = choosenOne.getI();
			mapping[first.getJ()] = choosenOne.getJ();

			virtualValuesCopy.remove(first);
		}

		int[][] result = new int[virtualSchedule.length][virtualSchedule[0].length];

		for (int i = 0; i < virtualSchedule.length; i++) {
			for (int j = 0; j < virtualSchedule[0].length; j++) {
				if (virtualSchedule[i][j] > 0) {
					result[i][mapping[j]] = mapping[virtualSchedule[i][j] - 1] + 1;
				} else {
					result[i][mapping[j]] = -(mapping[-virtualSchedule[i][j] - 1] + 1);
				}
			}
		}
		return result;
	}

	@Override
	public TTPSolution getInitialSolution() {
		TTPSolution sol = new TTPSolution();

		int noTries = 0;
		do {
			sol.setSchedule(createSchedule());

			if (!tabuList.contains(sol))
				break;

			noTries++;

			if (noTries % NEWSCHEDULE_NOTRIES == 0) {
				createNewVirtualSchedule();
			}
		} while (noTries < MAX_NO_TRIES);

		if (noTries > MAX_NO_TRIES) {
			logger.info("no new grasp-startsolution found");
		}

		tabuList.add(new TTPSolution(sol));

		if (problemInstance != null)
			sol.setProblemInstance(problemInstance);
		else {
			TTPInstance inst = new TTPInstance();
			inst.setNoTeams(noTeams);
			inst.setL(1);
			inst.setU(3);
			inst.setD(new int[(noTeams - 1) * 2][noTeams]);

			sol.setProblemInstance(inst);
		}

		TtpSolutionHelper.initializeSolution(sol, sol.getProblemInstance());

		return sol;
	}

	public int getNoTeams() {
		return noTeams;
	}

	public void setNoTeams(int noTeams) {
		this.noTeams = noTeams;
		initialized = false;
	}

	@Override
	public void setProblemInstance(TTPInstance instance) {
		this.problemInstance = instance;

		initialized = false;
	}

}

package ttp.constructionheuristics;

import ttp.model.TTPInstance;
import ttp.model.TTPSolution;
import ttp.util.TtpSolutionHelper;

import java.util.*;

public class TtpRandomConstructionHeuristic implements
		IConstructionHeuristics<TTPInstance, TTPSolution> {

	private static class IntPair {
		private int first;
		private int second;

		private IntPair(int first, int second) {
			this.first = first;
			this.second = second;
		}

		public int getFirst() {
			return first;
		}

		public int getSecond() {
			return second;
		}

		public void setFirst(int first) {
			this.first = first;
		}

		public void setSecond(int second) {
			this.second = second;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			IntPair teamPair = (IntPair) o;

			return first == teamPair.first && second == teamPair.second;
		}

		@Override
		public int hashCode() {
			int result = first;
			result = 31 * result + second;
			return result;
		}

		@Override
		public String toString() {
			return "(" + first + ", " + second + ")";
		}
	}

	private TTPInstance problemInstance;

	private static boolean generateSchedule(final List<Integer> possibleChoices,
			Set<IntPair> positions, int[][] schedule) {
		if (positions == null || positions.size() == 0)
			return true;

		// select lexicographically smallest position
		IntPair minPosition = positions.iterator().next();
		for (IntPair position : positions)
			if (position.getFirst() < minPosition.getFirst()
					|| (position.getFirst() == minPosition.getFirst() && position
							.getSecond() < minPosition.getSecond()))
				minPosition = position;

		int t = minPosition.getFirst();
		int w = minPosition.getSecond();

		List<Integer> choices = new ArrayList<Integer>(possibleChoices);
		int indexToRemove = (Math.abs(t) - 1) * 2;
		choices.remove(indexToRemove);
		choices.remove(indexToRemove);
		Collections.shuffle(choices);

		for (Integer o : choices) {
		//	IntPair randomChoice = new IntPair(o, w);
			IntPair absoluteRandomChoice = new IntPair(Math.abs(o), w);

			if (!positions.contains(absoluteRandomChoice))
				continue;

			// test
			boolean found = false;
			for (int i = 0; i < w; i++) {
				if (schedule[i][t - 1] == o.intValue()) {
					found = true;
					break;
				}

			}
			if (found)
				continue;
			// test ende

			schedule[w][t - 1] = o;

			if (o > 0)
				schedule[w][o - 1] = -t;
			else
				schedule[w][-o - 1] = t;

			Set<IntPair> newPositions = new HashSet<IntPair>(positions);

			newPositions.remove(absoluteRandomChoice);
			newPositions.remove(minPosition);

			if (generateSchedule(possibleChoices, newPositions, schedule))
				return true;
		}

		return false;
	}

	@Override
	public TTPSolution getInitialSolution() {
		assert problemInstance != null;

		int noTeams = problemInstance.getNoTeams();
		int weeks = problemInstance.getNoRounds();

		int[][] schedule = genSchedule(noTeams, weeks);

		TTPSolution solution = new TTPSolution();
		solution.setSchedule(schedule);
		solution.setProblemInstance(problemInstance);
		TtpSolutionHelper.initializeSolution(solution, problemInstance);
		
		
		return solution;
	}

	public static int[][] genSchedule(int noTeams, int weeks) {
		Set<IntPair> possiblePositions = new HashSet<IntPair>(noTeams * weeks
				* 2);
		List<Integer> possibleChoices = new ArrayList<Integer>(noTeams);

		for (int t = 1; t < noTeams + 1; t++) {
			for (int w = 0; w < weeks; w++)
				possiblePositions.add(new IntPair(t, w));
			possibleChoices.add(t);
			possibleChoices.add(-t);
		}
		// Collections.sort(possibleChoices);

		int[][] schedule = new int[weeks][noTeams];

		generateSchedule(Collections.unmodifiableList(possibleChoices),
				possiblePositions, schedule);
		return schedule;
	}

	public TTPInstance getProblemInstance() {
		return problemInstance;
	}

	@Override
	public void setProblemInstance(TTPInstance instance) {
		this.problemInstance = instance;
	}

}

package ttp.constructionheuristics;

import ttp.model.TTPSolution;

public class SimpleConstruction implements IConstructionHeuristics<TTPSolution> {

	private int noTeams;

	public SimpleConstruction(int noTeams) {
		this.noTeams = noTeams;
	}

	public int getNoTeams() {
		return noTeams;
	}

	public void setNoTeams(int noTeams) {
		this.noTeams = noTeams;
	}

	public SimpleConstruction() {
	}

	@Override
	public TTPSolution getInitialSolution() {
		TTPSolution sol = new TTPSolution();

		sol.setSchedule(createSchedule());

		return sol;
	}

	private int[][] createSchedule() {
		int days = (noTeams - 1) * 2;
		int half = (noTeams - 1);
		int[][] schedule = new int[noTeams][days];

		for (int i = 0; i < (noTeams - 1); i++) {
			boolean[] attached = new boolean[noTeams];
			for (int j = 0; j < half; j++) {
				if (schedule[i][j] == 0) {
					
					//find free team
					int unassignedTeam = 0;
					for(int k = i+1; k < noTeams; k++)
					{					
						if(schedule[k][j] == 0 && !attached[k])
						{
							unassignedTeam = k;
							attached[k] = true;
							break;
						}
					}
					schedule[i][j] = unassignedTeam+1;
					schedule[unassignedTeam][j] = -(i + 1);

					schedule[i][j + half] = - (unassignedTeam+1);
					schedule[unassignedTeam][j + half] = (i + 1);

				}
			}
		}

		return schedule;
	}

}

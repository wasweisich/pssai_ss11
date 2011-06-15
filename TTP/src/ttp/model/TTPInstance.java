package ttp.model;

public class TTPInstance {

	private int noTeams;

	private int L;// lower bound of consecutive home(away) games
	private int U;// upper bound of consecutive home(away) games

	private int[][] D;// distance matrix

	public int[][] getD() {
		return D;
	}

	public int getL() {
		return L;
	}

	public int getNoRounds() {
		return (noTeams - 1) * 2;
	}

	public int getNoTeams() {
		return noTeams;
	}

	public int getU() {
		return U;
	}

	public void setD(int[][] d) {
		D = d;
	}

	public void setL(int l) {
		L = l;
	}

	public void setNoTeams(int noTeams) {
		this.noTeams = noTeams;
	}

	public void setU(int u) {
		U = u;
	}


}

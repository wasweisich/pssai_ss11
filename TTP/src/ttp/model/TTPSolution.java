package ttp.model;

public class TTPSolution {

	//[teamNo][gameNo]
	private int[][] schedule;
	
	private double cost;
	
	
	private boolean legal;
	private double penalty;
	public int[][] getSchedule() {
		return schedule;
	}
	public void setSchedule(int[][] schedule) {
		this.schedule = schedule;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public boolean isLegal() {
		return legal;
	}
	public void setLegal(boolean legal) {
		this.legal = legal;
	}
	public double getPenalty() {
		return penalty;
	}
	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}
	
	
	
}

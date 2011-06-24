package ttp.constructionheuristics;

public class IntTripple implements Comparable<IntTripple> {

	private int i;
	private int j;
	private int k;

	public IntTripple(int i, int j, int k) {
		this.i = i;
		this.j = j;
		this.k = k;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	@Override
	public int compareTo(IntTripple o) {
		int tmp = o.k - k;
		if (tmp != 0)
			return tmp;

		tmp = o.i - i;
		if (tmp != 0)
			return tmp;

		tmp = o.j - j;

		return tmp;
	}

	@Override
	public String toString() {
		return "(" + i + "," + j + ": " + k + ")";
	}

}

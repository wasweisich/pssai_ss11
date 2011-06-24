package ttp.model;

public class TTPInstance implements Cloneable {

    private int noTeams;

    private int L;// lower bound of consecutive home(away) games
    private int U;// upper bound of consecutive home(away) games

    private int[][] D;// distance matrix

    @Override
    public TTPInstance clone() throws CloneNotSupportedException {
        TTPInstance clone = (TTPInstance) super.clone();
        clone.D = new int[D.length][];

        // multi-dim array's clone is shallow ... so do it manually
        for(int i=0;i<clone.D.length;i++)
            clone.D[i] = D[i].clone();

        return clone;
    }

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

package ttp.metaheuristic.tabu;

public interface ITabuList<T> {
	public void add(T object);
	public boolean contains(T object);
	public int getMaxSize();
	public void setMaxSize(int size);

}

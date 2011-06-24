package ttp.metaheuristic.tabu;

public interface ITabuList<T> extends Cloneable {
    public void add(T object);

    public boolean contains(T object);

    public int getMaxSize();

    public void setMaxSize(int size);

    public ITabuList<T> clone() throws CloneNotSupportedException;
}

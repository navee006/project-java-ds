package bank.ds;

import java.util.LinkedList;

public class Queue<T> {
    private LinkedList<T> list = new LinkedList<>();

    public void enqueue(T item) { list.addLast(item); }
    public T dequeue() { return list.isEmpty() ? null : list.removeFirst(); }
    public boolean isEmpty() { return list.isEmpty(); }
}

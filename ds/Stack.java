package bank.ds;

import java.util.ArrayList;

public class Stack<T> {
    private ArrayList<T> list = new ArrayList<>();

    public void push(T item) { list.add(item); }
    public T pop() { return list.isEmpty() ? null : list.remove(list.size() - 1); }
    public boolean isEmpty() { return list.isEmpty(); }
}

package bank.ds;

import java.util.LinkedList;

public class MyPriorityQueue<T> {
    private LinkedList<T> queue = new LinkedList<>();

    public void add(T item) {
        queue.addFirst(item); // latest at top
    }

    public T poll() {
        return queue.isEmpty() ? null : queue.removeFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void clear() {
        queue.clear();
    }

    public String display() {
        if (queue.isEmpty()) return "No pending transactions.";
        StringBuilder sb = new StringBuilder("🧾 Pending Transactions:\n");
        for (T t : queue) sb.append("• ").append(t).append("\n");
        return sb.toString();
    }
}

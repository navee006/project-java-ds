package bank.ds;

import java.util.HashMap;

public class HashTable<K, V> {
    private HashMap<K, V> map;

    public HashTable(int capacity) {
        map = new HashMap<>(capacity);
    }

    public void put(K key, V value) { map.put(key, value); }
    public V get(K key) { return map.get(key); }
    public boolean containsKey(K key) { return map.containsKey(key); }
}

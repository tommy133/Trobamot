package com.example.plantillatrobamot;

import java.util.Iterator;

public class UnsortedArrayMapping<K, V> {

    private K[] keys;
    private V[] values;
    private int size;

    public UnsortedArrayMapping(int capacity) {
        this.keys = (K[]) new Object[capacity];
        this.values = (V[]) new Object[capacity];
        this.size = 0;
    }

    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if (keys[i].equals(key)) {
                return values[i];
            }
        }
        return null;
    }

    public V put(K key, V value) {
        for (int i = 0; i < size; i++) {
            if (keys[i].equals(key)) {
                V oldValue = values[i];
                values[i] = value;
                return oldValue;
            }
        }
        keys[size] = key;
        values[size] = value;
        size++;
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public Iterator<K> iterator() {
        return new Iterator<K>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size && keys[currentIndex] != null;
            }

            @Override
            public K next() {
                return keys[currentIndex++];
            }
        };
    }
}

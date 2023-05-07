package com.example.plantillatrobamot;

import java.util.Iterator;

public class UnsortedArrayMapping<K, V> {

    private K[] keys;
    private V[] values;
    private int size;

    protected class Pair {
        private K key;
        private V value;
        private Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() {return key;}
        public V getValue() {return value;}
    }

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
        if (size < keys.length){
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
        }

        return null;
    }

    public V remove(K key) {
        for (int i = 0; i < size; i++) {
            if (keys[i].equals(key)) {
                V removedValue = values[i];
                // Move the last element to fill the gap
                keys[i] = keys[size-1];
                values[i] = values[size-1];
                keys[size-1] = null;
                values[size-1] = null;
                size--;
                return removedValue;
            }
        }
        return null;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public Iterator iterator() {return new UnsortedArrayMapping.IteratorUnsortedLinkedListMapping();}

    private class IteratorUnsortedLinkedListMapping implements Iterator{
            private int currentIndex = 0;


            public boolean hasNext() {
                return currentIndex < size && keys[currentIndex] != null;
            }


            public Object next() {
                Pair p = new Pair(keys[currentIndex], values[currentIndex]);
                currentIndex++;
                return p;
            }

    }
}

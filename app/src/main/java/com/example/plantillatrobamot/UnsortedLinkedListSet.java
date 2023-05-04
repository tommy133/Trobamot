package com.example.plantillatrobamot;

import java.util.Iterator;

public class UnsortedLinkedListSet <E> {
    private class Node {
        public Node(E elem, Node next) {
            this.elem = elem;
            this.next = next;
        }

        private E elem;
        private Node next;
    }
    private Node first;
    public UnsortedLinkedListSet() {
        first = null;
    }
    public boolean isEmpty() {
        return first == null;
    }
    public boolean contains(E elem) {
        Node p = first;
        boolean trobat = false;
        while (p != null && !trobat) {
            trobat = p.elem.equals(elem);
            p = p.next;
        }
        return trobat;
    }

    public boolean add(E elem) {
        boolean trobat = contains(elem);
        if (!trobat) {
            Node n = new Node(elem, first);
            first = n;
        }
        return !trobat;
    }
    public Iterator iterator() {return new IteratorUnsortedLinkedListSet();}
    private class IteratorUnsortedLinkedListSet implements Iterator {
        private Node idxIterator;
        private IteratorUnsortedLinkedListSet() {
            idxIterator = first;
        }
        public boolean hasNext() {
            return idxIterator != null;
        }
        public Object next() {
            E elem = idxIterator.elem;
            idxIterator = idxIterator.next;
            return elem;
        }
    }
}
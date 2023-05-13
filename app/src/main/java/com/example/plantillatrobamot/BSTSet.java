package com.example.plantillatrobamot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class BSTSet<E extends Comparable> implements Set<E> {

    private class Node {

        public E elem;
        public Node left, right;

        public Node(E elem, Node left, Node right) {
            this.elem = elem;
            this.left = left;
            this.right = right;
        }

    }
    private Node root;

    private class Cerca {

        boolean trobat;

        public Cerca(boolean trobat) {
            this.trobat = trobat;
        }

    }

    /**
     * Constructor
     */
    public BSTSet() {
        this.root = null;
    }

    @Override
    public int size() {
        return 0;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return contains((E) o, root);
    }



    private boolean contains(E elem, Node current) {
        // Si l’arbre és buit: no trobat
        // Si el node conté l’element: trobat
        // Si l’element és inferior a l’element del node: cercar al fill esquerra
        // Si l’element és superior a l’element del node: cercar al fill dret
        if (current == null) { // Si l’arbre és buit: no trobat
            return false;
        } else {
            if (current.elem.equals(elem)) {// Si el node conté l’element: trobat
                return true;
            }
            // Si l’element és inferior a l’element del node:
            if (elem.compareTo(current.elem) < 0) {
                return contains(elem, current.left); // cercar al fill esquerra
            } else {
                return contains(elem, current.right); // cercar al fill dret
            }
        }
    }

    private Node add(E elem, Node current, Cerca cerca) {
        if (current == null) {// Si l’arbre és buit: retornam un node nou
            return new Node(elem, null, null);
        } else {
            if (current.elem.equals(elem)) {// Si el node conté l’element
                cerca.trobat = true;
                return current; // retornam el node (sense modificar)
            }
            if (elem.compareTo(current.elem) < 0) {// Si l’element és inferior
                // hem d’afegir al subarbre esquerre
                current.left = add(elem, current.left, cerca);
            } else {
                // hem d’afegir al subarbre dret
                current.right = add(elem, current.right, cerca);
            }
            return current;
        }
    }



    /**
     *
     * @param elem
     * @return
     */
    @Override
    public boolean add(E elem) {
        Cerca cerca = new Cerca(false);
        this.root = add(elem, root, cerca);
        return !cerca.trobat;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        Cerca cerca = new Cerca(false);
        this.root = remove((E) o, root, cerca);
        return !cerca.trobat;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    private Node remove(E elem, Node current, Cerca cerca) {
        if (current == null) { // Element no trobat
            return null;
        }
        if (current.elem.equals(elem)) { // Element trobat
            cerca.trobat = true;
            // Eliminar node
            if (current.left == null && current.right == null) {
                return null;
            } else if (current.left == null && current.right != null) {
                return current.right;
            } else if (current.left != null && current.right == null) {
                return current.left;
            } else {
                Node plowest = current.right;
                Node parent = current;
                while (plowest.left != null) {
                    parent = plowest;
                    plowest = plowest.left;
                }
                plowest.left = current.left;
                if (plowest != current.right) {
                    parent.left = plowest.right;
                    plowest.right = current.right;
                }
                return plowest;
            }
        }
        if (elem.compareTo(current.elem) < 0) { // Subarbre esquerra
            current.left = remove(elem, current.left, cerca);
        } else {// Subarbre dret
            current.right = remove(elem, current.right, cerca);
        }
        return current;
    }

    /**
     *
     * @return BST iterator
     */
    @Override
    public Iterator iterator() {
        return new IteratorBSTSet();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return null;
    }

    private class IteratorBSTSet implements Iterator {

        // La implementació de l’iterador serà
        // una pila de nodes
        private Stack<Node> iterator;

        // Quin és el primer node a visitar
        public IteratorBSTSet() {
            Node p;
            iterator = new Stack();
            if (root != null) {
                p = root;
                while (p.left != null) {
                    iterator.push(p);
                    p = p.left;
                }
                iterator.push(p);
            }
        }

        // Tenim més nodes per visitar?
        @Override
        public boolean hasNext() {
            return !iterator.isEmpty();
        }

        // Quin és el següent node a visitar
        @Override
        public Object next() {
            Node p = iterator.pop();
            E elem = p.elem;
            if (p.right != null) {
                p = p.right;
                while (p.left != null) {
                    iterator.push(p);
                    p = p.left;
                }
                iterator.push(p);
            }
            return elem;
        }
    }
}
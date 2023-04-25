package com.example.plantillatrobamot;


import java.util.Iterator;

public class UnsortedArraySet<E> {

    private E[] array;
    private int n;

    /**
    * Constructor de la classe UnsortedArraySet que s'encarrega d'inicialitzar totes les variables
    * @param max   Quantitat màxima d'elements que hi haurà a l'array
    */
    public UnsortedArraySet(int max) {
        array = (E[]) new Object[max];
        n = 0;
    }

    /**
    * Funció que s'encarrega de comprovar si un element ja es troba a l'array
    * @param elem  Element que volem buscar a l'array
    * @return  Retorna true si s'ha trobat l'element i false si no s'ha trobat
    */
    public boolean contains(E elem) {
        int i = 0;
        boolean existeix = false;
        // mirem tots els elements fins que no trobem l'element que busquem
        while (!existeix && i < n) {
            // si l'element passat per paràmetre és igual al de l'array assignem el valor
            existeix = array[i].equals(elem);
            i++;
        }
        return existeix;
    }

    /**
    * Funció que s'encarrega d'afegir un nou element a l'array
    * @param elem  Element que volem afegir
    * @return  Retorna true si s'ha afegit correctament i false en cas contrari
    */
    public boolean add(E elem) {
        // mirem si hi cap i si l'element no està a l'array
        if (n < array.length && !contains(elem)) {
            array[n] = elem;
            n++;
            return true;
        } else {
            return false;
        }
    }

    /**
    * Funció que s'encarrega d'eliminar un element de l'array (NO utilitzat a la pràctica)
    * @param elem  Element que volem eliminar
    * @return  Retorna true si s'ha eliminat correctament i false en cas contrari
    */
    public boolean remove(E elem) {
        int i = 0;
        boolean existeix = false;
        while (!existeix && i < n) {
            existeix = elem.equals(array[i]);
            i++;
        }
        if (existeix) {
            array[i - 1] = array[n - 1];
            n--;
        }
        return existeix;
    }

    /**
    * Funció que s'encarrega de comprovar que l'array no estigui buida
    * @return  Retorna true si l'array està buida i false en cas contrari
    */
    public boolean isEmpty() {
        return n == 0;
    }

    public Iterator iterator() {
        Iterator it = new IteratorUnsortedArraySet();
        return it;
    }

    private class IteratorUnsortedArraySet implements Iterator {
        private int idxIterator;
        private IteratorUnsortedArraySet() {
            idxIterator = 0;
        }
        @Override
        public boolean hasNext() {
            return idxIterator < n;
        }
        @Override
        public Object next() {
            idxIterator++;
            return array[idxIterator - 1];
        }
    }


    /**
     * Funció que s'encarrega de convertir les dades que utiltzam en la clase en un String
     * @return Retorna un String amb les dades utilitzades
     */
    @Override
    public String toString(){
        String res = "";
        for (int i =0;i<array.length;i++){
            res+=array[i];
        }
        return res;
    }
}
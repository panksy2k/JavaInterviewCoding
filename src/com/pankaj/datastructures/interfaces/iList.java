package com.pankaj.datastructures.interfaces;

/**
 * Created by pankajpardasani on 25/01/2017.
 */
public interface iList<T> {
    abstract boolean add(T element);
    abstract boolean add(int index, T element);
    abstract T remove(int index);
    abstract boolean remove(T value);
    abstract int size();
    abstract T get(int index);
    abstract boolean contains(T element);
    abstract void clear();
}
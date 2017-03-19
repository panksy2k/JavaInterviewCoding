package com.pankaj.datastructures;

import com.pankaj.datastructures.interfaces.iList;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * Created by pankajpardasani on 25/01/2017.
 */
public class ArrayList<T> implements iList<T> {
    private int CAPACITY = 25;
    private T[] objectArray = null;
    private int size = 0;

    private BiPredicate<Integer, Integer> invalidAddIndex = (index, size) -> (index > size || index < 0)? true : false;
    private BiPredicate<Integer, Integer> invalidRemoveIndex = (index, size) -> (index >= size || index < 0)? true : false;
    private Supplier<Integer> halfSize = () -> objectArray.length >> 1;


    public ArrayList(int initialCapacity) {
        objectArray = (T[]) new Object[initialCapacity];
    }

    public ArrayList() {
        objectArray = (T[]) new Object[CAPACITY];
    }

    @Override
    public boolean add(T element) {
        if(size == CAPACITY) {
            explode();
        }

        objectArray[size++] = element;
        return true;
    }

    @Override
    public boolean add(int index, T element) {
        if(invalidAddIndex.test(index, size)) throw new IllegalArgumentException("Invalid index used to add element inside the List");

        if(index < size) {
            System.arraycopy(objectArray, index, objectArray, 1+index, size-index);
        }

        objectArray[index] = element;
        return true;
    }

    @Override
    public T remove(int index) {
        if(invalidRemoveIndex.test(index, size)) throw new IllegalArgumentException("Invalid index used to add element inside the List");

        T deletedElement = objectArray[index];
        System.arraycopy(objectArray, index+1, objectArray, index, size-index);
        objectArray[index] = null;
        --size;

        if(halfSize.get() > this.CAPACITY && halfSize.get() > size) {
            collapse();
        }

        return deletedElement;
    }

    @Override
    public boolean remove(T value) {
        for(int i = 0; i < this.objectArray.length; ++i) {
            if(this.objectArray[i].equals(value)) {
                T deletedElement = remove(i);
                if(null != deletedElement) return true;
                break;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public T get(int index) {
        if(invalidRemoveIndex.test(index, size)) throw new IllegalArgumentException("Invalid index -- cannot fetch");

        return this.objectArray[index];
    }

    @Override
    public boolean contains(T element) {
        for(int i = 0; i < this.objectArray.length; ++i) {
            if(this.objectArray[i].equals(element)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        this.size = 0;
    }

    private void explode() {
        this.CAPACITY = this.CAPACITY << 1;
        this.objectArray = Arrays.copyOf(this.objectArray, CAPACITY);
    }

    private void collapse() {
        this.objectArray = Arrays.copyOf(this.objectArray, halfSize.get());
    }
}

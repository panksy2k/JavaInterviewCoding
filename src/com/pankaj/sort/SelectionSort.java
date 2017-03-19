package com.pankaj.sort;

/**
 * Created by pankajpardasani on 18/02/2017.
 */
public class SelectionSort {

    private SelectionSort() {}

    public static <T extends Comparable> T[] sort(T[] arrayOfData) {
        int min;
        for(int i = 0; i < arrayOfData.length - 1; ++i) {
            min = i; //Re-starting the minimum for next turn as the traversing of elements is O(N)

            for(int j = i + 1; j <= arrayOfData.length - 1; j++) {
                if(arrayOfData[min].compareTo(arrayOfData[j]) > 0) {
                    min = j; //Move the pointer to the new index which has minimum in the current iteration
                    swap(i, min, arrayOfData);
                }
            }
        }

        return arrayOfData;
    }

    private static <T extends Comparable> void swap(int indexFrom, int indexTo, T[] sourceData) {
        T tempValue = sourceData[indexFrom];
        sourceData[indexFrom] = sourceData[indexTo];
        sourceData[indexTo] = tempValue;
    }
}

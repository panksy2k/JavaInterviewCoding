package com.pankaj.sort;

/**
 * Created by pankajpardasani on 14/02/2017.
 */
public class BubbleSortOther {

    private BubbleSortOther() {
    }

    public static <T extends Comparable> T[] sort(T[] unsortedArray) {
        for (int i = unsortedArray.length - 1; i >= 1; i--) {
            for (int j = 0; j < i; j++) {
                if(unsortedArray[j].compareTo(unsortedArray[j+1]) > 0) {
                    swap(j, j+1, unsortedArray);
                }
            }
        }

        return unsortedArray;
    }

    private static <T extends Comparable> void swap(int lower, int higher, T[] unsortedArray) {
        T temp = unsortedArray[lower];
        unsortedArray[lower] = unsortedArray[higher];
        unsortedArray[higher] = temp;
    }
}

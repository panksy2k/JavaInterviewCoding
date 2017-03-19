package com.pankaj.sort;

/**
 * Bubble sort - simplest sorting algorithm
 * In general, where N is the number of items in the array, there are N-1 comparisons on the first pass, N-2 on the second, and so on.
 * The formula for the sum of such a series is (N–1) + (N–2) + (N–3) + ... + 1 = N*(N–1)/2 N*(N–1)/2 is 45 (10*9/2) when N is 10.
 * Thus, the algorithm makes about N^2⁄2 comparisons (ignoring the –1, which doesn’t make much difference, especially if N is large).
 *
 * If the data is random, a swap is necessary about half the time, so there will be about N^2⁄4 swaps. (Although in the worst case, with the
 * initial data inversely sorted, a swap is necessary with every comparison.)
 * Both swaps and comparisons are proportional to N^2. Because constants don’t count in Big O notation, we can ignore the 2 and the 4 and say
 * that the bubble sort runs in O(N^2) time.
 * <p>
 * Average case = O(N^2) Worst case = O(N^2) Best case = O(N)
 * <p>
 * Created by pankajpardasani (pankaj.d.p@gmail.com) on 27/01/2017.
 */
public class BubbleSort {
    private BubbleSort() {
    }

    public static <T extends Comparable> T[] sort(T[] unsortedArray) {
        boolean swap = true;
        int length = unsortedArray.length;

        while (swap) {
            swap = false;

            for (int i = 1; i < length; ++i) {
                if (unsortedArray[i].compareTo(unsortedArray[i - 1]) < 0) {
                    swapElements(i, i - 1, unsortedArray);
                    swap = true;
                }
            }

            if (swap) --length;
        }

        return unsortedArray;
    }

    private static <T extends Comparable> void swapElements(int iTh, int iThMinus1, T[] unsortedArray) {
        T thElementMinus1 = unsortedArray[iThMinus1];
        unsortedArray[iThMinus1] = unsortedArray[iTh];
        unsortedArray[iTh] = thElementMinus1;
    }
}

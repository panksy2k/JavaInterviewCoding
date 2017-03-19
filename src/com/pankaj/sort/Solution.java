package com.pankaj.sort;

// you can also use imports, for example:
// import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class Solution {

    private Function<List<Integer>, Integer> consequitiveCounter = (L) -> L.size();

    public int solution(int[] A) {
        // write your code in Java SE 8
        List<Integer> contiguousIndexLocations = getContiguousIndexReferences(A);
        return getContiguousCount(contiguousIndexLocations);
    }

    private List<Integer> getContiguousIndexReferences(int[] mixHeightedArray) {
        int indexCounter = 0;
        List<Integer> consequtiveIndex = new ArrayList<>();

        boolean swap = true;
        int length = mixHeightedArray.length;

        while (swap) {
            swap = false;

            for (int i = 1; i < length; ++i) {
                if (mixHeightedArray[i] < mixHeightedArray[i - 1]) {
                    swapElements(i, i - 1, mixHeightedArray);
                    swap = true;

                    consequtiveIndex.add(i);
                    indexCounter++;
                }
            }

            if (swap) --length;
        }

        return consequtiveIndex;
    }

    private void swapElements(int iTh, int iThMinus1, int[] unsortedArray) {
        int thElementMinus1 = unsortedArray[iThMinus1];
        unsortedArray[iThMinus1] = unsortedArray[iTh];
        unsortedArray[iTh] = thElementMinus1;
    }

    private int getContiguousCount(List<Integer> sortedHeightIndexes) {
        return consequitiveCounter.apply(sortedHeightIndexes) + 1;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.solution(new int[]{1, 2, 6, 5, 5, 4, 8, 9}));
    }
}
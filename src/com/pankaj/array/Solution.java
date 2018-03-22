package com.pankaj.array;

import java.util.Arrays;

class Solution {
    public int solution(int[] A) {

        Arrays.parallelSort(A);

        int lowestNonAvailableElement = -1;

        for (int i = 0; i < A.length - 1; i++) {
            if (A[i + 1] - A[i] > 1) {
                lowestNonAvailableElement = A[i] + 1;
                break;
            }

            if (i == A.length - 2) {
                lowestNonAvailableElement = A[i + 1] + 1;
                break;
            }
        }

        if(lowestNonAvailableElement < 0) return 1;

        return lowestNonAvailableElement;
    }

    public static void main(String... args) {
        System.out.println(new Solution().solution(new int[]{1, 3, 6, 4, 1, 2}));



    }
}
package com.pankaj.array;

import java.util.Arrays;
import java.util.stream.IntStream;

public class FindNumberArray {

    static String findNumber(int arr[], int k) {
        Arrays.sort(arr);

        boolean isNumberPresent = IntStream.of(arr).anyMatch(i -> i == k);
        return isNumberPresent? "YES" : "NO";
    }

    public static void main(String[] args) {

        System.out.println(findNumber(new int[] {2, 3, 1}, 0));

    }
}

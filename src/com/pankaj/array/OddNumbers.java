package com.pankaj.array;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OddNumbers {

    static int[] oddNumbers(int l, int r) {

        List<Integer> allOddNumbers = IntStream.rangeClosed(l, r)
                .filter(number -> number % 2 != 0).boxed().collect(Collectors.toList());

        return allOddNumbers.stream().mapToInt(i -> i).toArray();

    }

    public static void main(String[] args) {

    }
}

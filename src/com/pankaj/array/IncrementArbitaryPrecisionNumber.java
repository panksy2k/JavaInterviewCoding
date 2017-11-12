package com.pankaj.array;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This program shows how to add an integer by 1 -- where there might be a limitation of storing precision values in a datatype and hence
 * simply addimng of a number is not an option.
 *
 * Created by pankajpardasani on 11/11/2017.
 */
public class IncrementArbitaryPrecisionNumber {

    public static void main(String[] args) {
        List<Integer> sourceNumber = new ArrayList<>();
        sourceNumber.add(5); sourceNumber.add(8); sourceNumber.add(8);
        System.out.println(plusOne(sourceNumber));
        sourceNumber.clear();
        sourceNumber.add(9); sourceNumber.add(9); sourceNumber.add(9);
        System.out.println(plusOne(sourceNumber));
    }

    public static List<Integer> plusOne(List<Integer> sourceNumber) {
        int lastIndex = sourceNumber.size() - 1;
        sourceNumber.set(lastIndex, 1 + sourceNumber.get(lastIndex));

        for(int n = lastIndex; n > 0 && sourceNumber.get(n) == 10; n--) {
            sourceNumber.set(n, 0);
            sourceNumber.set(n - 1, 1 + sourceNumber.get(n - 1));
        }

        if(sourceNumber.get(0) == 10) {
            sourceNumber.set(0, 0);
            sourceNumber.add(0, 1);
        }

        return sourceNumber;
    }
}

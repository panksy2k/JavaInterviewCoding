package com.pankaj.array;

/**
 * Created by pankajpardasani on 24/02/2017.
 */

import java.util.stream.IntStream;

/**
 * The idea is to accept an integer array and arrange it in odrer where even numbers appear first and then odd numbers apear latyer inside the
 * same array without using any other extra space.
 */
public class EvenOddArrangement {

    public static void main(String[] args) {
        IntStream.of(arrange(new int[] {3, 4, 6, 7, 8, 0, 1, 126})).forEach(System.out :: println);
    }

    private static int[] arrange(int[] unarrangedArray) {
        int evenIndex = 0, oddIndex = unarrangedArray.length - 1;
        while(evenIndex < oddIndex) {
            if(unarrangedArray[evenIndex] % 2 == 0) {
                ++evenIndex;
            }
            else {
                int temp = unarrangedArray[evenIndex];
                unarrangedArray[evenIndex] = unarrangedArray[oddIndex];
                unarrangedArray[oddIndex] = temp;
                --oddIndex;
            }
        }

        return unarrangedArray;
    }
}

package com.pankaj.primitives;

/**
 * Created by pankajpardasani on 24/02/2017.
 */
public class CountBitsToOne {
    public static void main(String[] args) {
        System.out.println(countBits(100));
    }

    public static short countBits(int x) {
        short numberOfBits = 0;
        while(x != 0) {
            System.out.println(String.format("Printing %d with &1 being applied to become %d", x, x & 1));
            numberOfBits += (x & 1);
            x >>>= 1; //right shift operator so will half it each time
        }

        return numberOfBits;
    }
}

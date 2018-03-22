package com.pankaj.array;

public class PascalTrainagle {

    public static void main(String[] args) {
        pascalTraingle(4);
    }

    static void pascalTraingle(int k) {
        for (int i = 0; i < k; i++) {
            for (int j = 0; j <= i; j++) {
                System.out.print(pascal(i, j) + " ");
            }

            System.out.println();
        }
    }

    private static int pascal(int i, int j) {
        if (j == 0 || j == i) {
            return 1;
        } else {
            return pascal(i - 1, j - 1) + pascal(i - 1, j);
        }
    }
}

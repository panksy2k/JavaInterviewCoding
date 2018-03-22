package com.pankaj.functional;

public class Shipping {


    public static int minimalNumberOfPackages(int items, int availableLargePackages, int availableSmallPackages) {
        int numberOfPacketsRequired = 0;

        if ((availableLargePackages * 5) + (availableSmallPackages * 1) < items) {
            return -1;
        }

        for(; items > 0; items--) {
            while(availableLargePackages > 0) {
                items -= 5;
                ++numberOfPacketsRequired;
                --availableLargePackages;
            }
            while(availableSmallPackages > 0) {
                items -= 1;
                ++numberOfPacketsRequired;
                --availableSmallPackages;
            }
        }

        return numberOfPacketsRequired;
    }

    public static void main(String[] args) {
        System.out.println(minimalNumberOfPackages(16, 2, 10));
    }
}
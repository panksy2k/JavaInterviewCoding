package com.pankaj.functional;

import java.util.function.Function;

/**
 * Created by pankajpardasani on 28/03/2017.
 */
public class Functions {

    private static Function<Integer, Function<Integer, Integer>> add = x -> y -> x + y;
    private static Function<Integer, Integer> twice = x -> 2 * x;
    private static Function<Integer, Integer> thrice = x -> 3 * x;
    private static Function<Function<Integer, Integer>, Function<Function<Integer, Integer>, Function<Integer, Integer>>> composeTwiceThrice =
            x -> y -> z -> x.apply(y.apply(z));


    private static Function<Function<Double, Double>,
            Function<Function<Double, Double>,
                    Function<Double, Double>>> compose =
            x -> y -> z -> x.apply(y.apply(z));



    public static void main(String[] args) {
        System.out.println("Addition of numbers " + add.apply(6).apply(10));
        System.out.println(composeTwiceThrice.apply(twice).apply(thrice).apply(2)); //Thrice and then Twice

        Function<Double, Double> sin = x -> Math.sin(x);
        System.out.println(compose.apply(x -> Math.PI / 2 - x).apply(sin).apply(2.0));


    }
}

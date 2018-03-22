package com.pankaj.array;


public class Tester {

    static String str;
    public void Tester() {
        System.out.println("In Constructor");
        str = "Hello World";
    }

    public static void main(String[] args) {
        Tester t = new Tester();
        System.out.println(str);
    }
}

package com.pankaj.array;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CountDuplicates {


    public static void main(String[] args) {
       /* System.out.println(countDuplicates(new int[]{1, 3, 1, 4, 5, 6, 3, 2}));
        try {
            doStuff(new String[] {"x"});
        }
        catch (Exception e) {
            System.out.println("an error");
        }*/

      /* SampleDemo A = new SampleDemo("A");
       SampleDemo B = new SampleDemo("B");

       B.start();
       A.start();*/
    }



    private static int numofLetterNeedtoChange(String a, String b) {
        int count = 0;

        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            if (map.containsKey(c))
                map.put(c, map.get(c) + 1);
            else {
                map.put(c, 1);
            }
        }
        for (int i = 0; i < b.length(); i++) {
            char c = b.charAt(i);
            if (map.containsKey(c)) {
                if (map.get(c) != 0)
                    map.put(c, map.get(c) - 1);
                else
                    count++;
            } else
                count++;
        }

        return count;
    }

}

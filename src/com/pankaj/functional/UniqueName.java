package com.pankaj.functional;

public class UniqueName {
    private static final String EMPTY_STRING = "";

    public static String firstUniqueName(String[] names) {
        if (names == null || names.length == 0) return EMPTY_STRING;

        for (int i = 0; i < names.length; i++) {
            int j = i + 1;
            for (; j < names.length; j++) {
                if(names[i].equals(EMPTY_STRING)) {
                    break;
                }

                if (names[i].equals(names[j])) {
                    names[j] = EMPTY_STRING;
                    break;
                }
            }

            if (j == names.length && !names[i].equals(EMPTY_STRING)) {
                return names[i];
            }
        }

        return EMPTY_STRING;
    }

    public static void main(String[] args) {
        System.out.println(firstUniqueName(new String[]{"Abbie", "Adeline", "Abbie", "Adelin", "Hello"}));
    }
}
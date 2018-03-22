package com.pankaj.array;

public class RomanNUmber {
    private final static java.util.NavigableMap<Integer, String> map = new java.util.TreeMap<>();

    static {
        map.put(1, "I");
        map.put(4, "IV");
        map.put(5, "V");
        map.put(9, "IX");
        map.put(10, "X");
        map.put(40, "XL");
        map.put(50, "L");
        map.put(90, "XC");
        map.put(100, "C");
        map.put(400, "CD");
        map.put(500, "D");
        map.put(900, "CM");
        map.put(1000, "M");
    }


    static String[] romanizer(int[] numbers) {
        int[] numbersForRomainzation = new int[numbers.length];
        System.arraycopy(numbers, 1, numbersForRomainzation, 0, numbersForRomainzation.length - 1);

        validateNumbers(numbersForRomainzation);

        int valueIndex = 0;
        String[] romanizedValues = new String[numbersForRomainzation.length];
        for (; valueIndex < romanizedValues.length; ) {
            romanizedValues[valueIndex] = toRoman(numbersForRomainzation[valueIndex]);
            ++valueIndex;
        }

        return romanizedValues;
    }

    private static void validateNumbers(int[] n) {
        boolean isRangeInValid = java.util.stream.IntStream.of(n).anyMatch(n1 -> n1 < 1 || n1 > 1000);

        if (isRangeInValid)
            throw new IllegalArgumentException("The range of number to validate is invalid -- should be (1 <= number <= 1000)");
    }

    private final static String toRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static void main(String[] args) {
        String[] s = romanizer(new int[]{5, 1, 2, 3, 4, 5});

        java.util.Arrays.stream(s).forEach(System.out::println);
    }
}

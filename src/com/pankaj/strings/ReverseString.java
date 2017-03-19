package com.pankaj.strings;

/**
 * Created by pankajpardasani on 09/03/2017.
 */
public class ReverseString {
    private final char[] sampleString;

    public ReverseString(String aString) {
        this.sampleString = aString.toCharArray();
    }

    public String reverseOrder() {
        reverseStringCharacters(0, sampleString.length - 1);
        reverseWords();

        return new String(sampleString);
    }

    private void reverseStringCharacters(int startIndex, int endIndex) {
        for(; startIndex<endIndex; startIndex++, endIndex--) {
            char temp = sampleString[startIndex];
            sampleString[startIndex] = sampleString[endIndex];
            sampleString[endIndex] = temp;
        }
    }

    private void reverseWords() {
        int startIndex = 0, endIndexOfWord = 0;

        while((endIndexOfWord = getWordEndIndexOnDelimeter(' ', startIndex)) != -1) {
            reverseStringCharacters(startIndex, endIndexOfWord - 1);
            startIndex = endIndexOfWord + 1;
        }

        reverseStringCharacters(startIndex, sampleString.length - 1);
    }

    private int getWordEndIndexOnDelimeter(char delimeter, int startIndex) {
        int wordStartIndex = -1;

        for(; startIndex <= sampleString.length-1; startIndex++) {
            if(sampleString[startIndex] == delimeter) {
                wordStartIndex = startIndex;

                break;
            }
        }

        return wordStartIndex;
    }

    public static void main(String[] args) {
        System.out.println(new ReverseString("Alice Loves Wonderland").reverseOrder());
    }
}

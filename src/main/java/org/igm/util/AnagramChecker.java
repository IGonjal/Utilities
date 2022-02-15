package org.igm.util;

import java.text.Normalizer;

public class AnagramChecker {

    private static boolean isPunctuationConsidered = true;
    private AnagramChecker() {
    }
    public static AnagramChecker getInstance() {
        return new AnagramChecker();
    }

    public AnagramChecker consideringPunctuation(boolean considerPunctuation) {
        this.isPunctuationConsidered = considerPunctuation;
        return this;
    }


    /**
     * This algorithm compares if two strings are anagrams ignoring the spaces and
     * comparing the special characters. The accentuated and capital characters will
     * be considered as the same letter for example:
     *
     * "CämìÓn", "cÂMÍoN", "ca mi on" and "camion" are considered anagrams, but
     * "ca-mi-on", "*camion", "1 camion", "cam.ion" and "camion" are not
     *
     * @param one the first string to compare
     * @param two the second string to compare
     * @return true if both strings are anagrams
     */
    public boolean isAnagram(String one, String two) {
        /*
          The idea  behind this algorithm is to fill a list with the letters of the first phrase and
          remove one to one the letters of the second phrase. If, reached the end, the list contains
          letters, it means that the considered strings aren't anagrams
       */

        //Normalizing the strings, to lowercase, remove spaces and remove accents
        one = normalize(one);
        two = normalize(two);

        //If, after normalizing, the strings have different letters, they arent anagrams
        if (one.length() != two.length()) {
            return false;
        }

        char[] oneChar = one.toCharArray();
        char[] twoChar = two.toCharArray();
        OptimizedUnsortedList<Character> listOne = new OptimizedUnsortedList<>();
        /*
         I fill a list and a map in order to fill and delete with cost O(1) as
         the map can access the element with o(1) and so does the list if you
         know the order, that's why the map is for
        */
        for (int i = 0; i < oneChar.length; i++) {
            listOne.add(oneChar[i]);
        }

        int index = 0;
        while (!listOne.isEmpty() && index < twoChar.length) {

            if (listOne.contains(twoChar[index])) {
                listOne.remove(twoChar[index]);
            } else {
                return false;
            }
            index++;
        }

        return listOne.isEmpty();
    }


    /**
     * This method removes spaces, transforms to lowercase and removes
     *
     * @param original
     * @return
     */
    private String normalize(String original) {
        String ret = original.replace(" ", "");
        /*
         For practical reasons i've decided to consider special characters
         as equals to their counterpart:
         c == ç and ñ == n
         If desired otherwise, an ad-hoc normalizer would have to be implemented
        */

        ret = Normalizer.normalize(ret, Normalizer.Form.NFKD); //When normalizing, the accents are separated from letters
        ret = ret.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        if(!isPunctuationConsidered) {
            ret = ret.replaceAll("[^a-zA-Z0-9]", "");
        }

        return ret.toLowerCase();
    }
}

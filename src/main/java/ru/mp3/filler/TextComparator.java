package ru.mp3.filler;

public class TextComparator {
    public static float compareStrings(String entrance, String source) {
        int total = entrance.length();
        int enter = 0;
        int lastCoincidence = -1;
        for (int i = 0; i < total; i++){
            for (int j = lastCoincidence + 1; j < source.length(); j++){
               // System.out.println("en = " + entrance.charAt(i) + " src = " + source.charAt(j));
                if(entrance.charAt(i) == source.charAt(j)){
                    enter++;
                    lastCoincidence = j;
                    break;
                }
            }
        }
        return (float)enter / (float)total;
    }
}

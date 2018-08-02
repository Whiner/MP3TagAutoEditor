package ru.mp3.filler;

public class NameSeparator {
    private static char SEPARATOR = '-';

    public static char getSEPARATOR() {
        return SEPARATOR;
    }

    public static void setSEPARATOR(char SEPARATOR) {
        NameSeparator.SEPARATOR = SEPARATOR;
    }
}

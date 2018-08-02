package ru.mp3.filler.parser;

import org.springframework.stereotype.Component;

@Component
public class Parser {
    private enum P {SINGER, SONG_NAME }
    private static String parse(String mp3Name, char separator, P p) throws Exception {
        if(mp3Name == null){
            throw new NullPointerException();
        }
        if(!mp3Name.contains(String.valueOf(separator))){
            throw new Exception("Mp3 name is not contains this separator");
        }
        final String regex = "(" + String.valueOf(separator) + "|\\.)";
        final String[] split = mp3Name.split(regex);
        if(p == P.SINGER) {
            return split[0].trim();
        } else {
            return split[1].trim();
        }
    }
    public static String parseSinger(String mp3Name, char separator) throws Exception {
        return parse(mp3Name, separator, P.SINGER);
    }

    public static String parseSongName(String mp3Name, char separator) throws Exception {
        return parse(mp3Name, separator, P.SONG_NAME);
    }
}

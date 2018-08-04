package ru.mp3.filler.parser;

import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class Parser {
    private enum P {ARTIST, SONG_NAME }
    private static String parse(String mp3Name, char separator, P p) throws Exception {
        if(mp3Name == null){
            throw new NullPointerException();
        }
        if(!mp3Name.contains(String.valueOf(separator))){
            throw new ParseException("Mp3 name is not contains this separator", 0);
        }
        final String regex = "(" + String.valueOf(separator) + "|\\.)";
        final String[] split = mp3Name.split(regex);
        if(p == P.ARTIST) {
            return split[0].trim();
        } else {
            return split[1].trim();
        }
    }
    public static String parseArtist(String mp3Name, char separator) throws Exception {
        return parse(mp3Name, separator, P.ARTIST);
    }

    public static String parseTitle(String mp3Name, char separator) throws Exception {
        return parse(mp3Name, separator, P.SONG_NAME);
    }

}

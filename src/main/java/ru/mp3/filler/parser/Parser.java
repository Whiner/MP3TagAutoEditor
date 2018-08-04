package ru.mp3.filler.parser;

import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class Parser {
    private enum P {ARTIST, SONG_NAME }
    private static String parse(String mp3Name, char separator, P p) throws Exception { //би-2 не хотит(((
        if(mp3Name == null){
            throw new NullPointerException();
        }
        if(!mp3Name.contains(String.valueOf(separator))){
            throw new ParseException("Mp3 name is not contains this separator", 0);
        }
        if(mp3Name.toLowerCase().contains("би-2")){
            mp3Name = mp3Name.toLowerCase().replace("би-2", "БИ—2");
        }
        final String regex = "(" + String.valueOf(separator) + "|\\.mp3)";
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

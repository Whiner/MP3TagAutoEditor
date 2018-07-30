package ru.mp3.filler.parser;

import org.springframework.stereotype.Component;

@Component
public class Parser {
    private enum P {SINGER, SONG_NAME }
    private String parse(String mp3Name, char separator, P p) throws Exception {
        if(mp3Name == null){
            throw new NullPointerException();
        }
        if(!mp3Name.contains(String.valueOf(separator))){
            throw new Exception("Mp3 name is not contains this separator");
        }
        if(p == P.SINGER) {
            return mp3Name.split(String.valueOf(separator))[1].trim();
        } else {
            return mp3Name.split(String.valueOf(separator))[2].trim();
        }
    }
    public String parseSinger(String mp3Name, char separator) throws Exception {
        return parse(mp3Name, separator, P.SINGER);
    }

    public String parseSongName(String mp3Name, char separator) throws Exception {
        return parse(mp3Name, separator, P.SONG_NAME);
    }
}

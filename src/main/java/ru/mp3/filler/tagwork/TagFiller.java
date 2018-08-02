package ru.mp3.filler.tagwork;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v22Tag;
import com.mpatric.mp3agic.ID3v2TagFactory;
import com.mpatric.mp3agic.Mp3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mp3.filler.NameSeparator;
import ru.mp3.filler.parser.Parser;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TagFiller {
    private Mp3File mp3File;

    public Mp3File getMp3File() {
        return mp3File;
    }

    public void setMp3File(Mp3File mp3File) {
        this.mp3File = mp3File;
        fill();
    }

    private void fill(){
        try {
            File file = new File(mp3File.getFilename());
            String filename = file.getName();

            if(mp3File.hasId3v1Tag()) {
                mp3File.removeId3v1Tag();
            }
            if(mp3File.hasId3v2Tag()) {
                mp3File.removeId3v2Tag();
            }
            if(mp3File.hasCustomTag()) {
                mp3File.removeCustomTag();
            }

            final String singer = Parser.parseSinger(filename, NameSeparator.getSEPARATOR());
            final String songName = Parser.parseSongName(filename, NameSeparator.getSEPARATOR());
            final ID3v22Tag tag = new ID3v22Tag();
            tag.setArtist(singer);
            tag.setTitle(songName);
            mp3File.setId3v2Tag(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

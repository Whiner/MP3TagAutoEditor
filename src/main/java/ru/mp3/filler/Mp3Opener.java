package ru.mp3.filler;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Mp3Opener {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Bean
    @Qualifier("mp3")
    public Mp3File getMp3File() throws InvalidDataException, IOException, UnsupportedTagException {
        return open();
    }


    public Mp3File open() throws InvalidDataException, IOException, UnsupportedTagException {
        return new Mp3File(path);
    }

    public static Mp3File open(String path) throws InvalidDataException, IOException, UnsupportedTagException {
        return new Mp3File(path);
    }

    public static List<Mp3File> openAll(String folderPath) throws InvalidDataException, IOException, UnsupportedTagException {
        File file = new File(folderPath);
        List<Mp3File> list = new ArrayList<Mp3File>();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if (files != null) {
                for (File f: files){
                    list.add(new Mp3File(f.getPath()));
                }
            } else {
                throw new NullPointerException("folder is empty");
            }
        } else {
            throw new InvalidDataException("its path to file, not to folder");
        }
        return list;
    }
}

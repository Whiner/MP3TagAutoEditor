package ru.mp3.filler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    public static String[] getFilenames(String folderDirectory){
        File folder = new File(folderDirectory);
        if(folder.isDirectory()){
           return folder.list();
        } else {
            return new String[]{folder.getName()};
        }
    }
}

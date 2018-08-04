package ru.mp3.filler;

import com.mpatric.mp3agic.*;
import ru.mp3.filler.parser.FilenameValidator;
import ru.mp3.filler.tagwork.TagFiller;
import ru.mp3.filler.tagwork.TagFinder;
import ru.mp3.filler.tagwork.exceptions.PageNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        final TagFiller test = CustomBeanFactory.getInstance().getBean("tagFillerTest", TagFiller.class);
//
//        Mp3File song = test.getMp3File();
//        final ID3v2 tag = song.getId3v2Tag();
//        System.out.println("Track: " + tag.getTrack());
//        System.out.println("Artist: " + tag.getArtist());
//        System.out.println("Title: " + tag.getTitle());
//        System.out.println("Album: " + tag.getAlbum());
//        System.out.println("Year: " + tag.getYear());
//        System.out.println("Genre: " + tag.getGenre() + " (" + tag.getGenreDescription() + ")");
//        System.out.println("Comment: " + tag.getComment());
//        final String[] music = FileReader.getFilenames("music");
//        for (String song: music){
//            System.out.println("Original: " + song);
//            System.out.println("Validate: " +FilenameValidator.validate(song));
//
//        }
        int all = 1;
        int success = 0;

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("log.txt"))) {
            TagFiller.setTagFiller((TagFinder) CustomBeanFactory.getInstance().getBean("tagFinder"));
            final List<Mp3File> files = Mp3Opener.openAll("music/m");
            all = files.size();
            for (Mp3File mp3: files){
                try {
                    Mp3File filled = TagFiller.fill(mp3);
                    ID3v2 tag = filled.getId3v2Tag();
                    bufferedWriter.write(tag.getArtist() + " - "
                            + tag.getTitle()
                            + " (" + tag.getGenreDescription()
                            + ") Track№" + tag.getTrack()
                            + " Year " + tag.getYear() + "\n");
                    success++;
                } catch (PageNotFoundException e){
                    bufferedWriter.write("Ошибка(Page) " + new File(mp3.getFilename()).getName() + " ------------------------------------\n");
                } catch (ParseException e) {
                    bufferedWriter.write("Ошибка(Prse) " + new File(mp3.getFilename()).getName() + " ------------------------------------\n");

                }

            }
        } catch (InvalidDataException | IOException | UnsupportedTagException e) {
            e.printStackTrace();
        }
        System.out.println("Успех " + (float)success/(float)all*100.0);
        //FilenameValidator.validate("Childish Gambino - This Is America[#NR](feat. Моу лол).mp3");
    }
}

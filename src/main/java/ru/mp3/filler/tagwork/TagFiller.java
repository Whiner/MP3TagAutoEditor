package ru.mp3.filler.tagwork;

import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mp3.filler.CustomBeanFactory;
import ru.mp3.filler.NameSeparator;
import ru.mp3.filler.ReleaseDate;
import ru.mp3.filler.parser.FilenameValidator;
import ru.mp3.filler.parser.Parser;
import ru.mp3.filler.tagwork.exceptions.PageNotFoundException;

import javax.swing.filechooser.FileView;
import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

@Component
public class TagFiller {
    private static TagFinder tagFinder;

    public static void setTagFiller(TagFinder tagFinder){
        TagFiller.tagFinder = tagFinder;
    }


    public static Mp3File fill(Mp3File mp3File) throws PageNotFoundException, ParseException {
        try {

            File file = new File(mp3File.getFilename());
            String filename = FilenameValidator.validate(file.getName());


            Map<String, String> tags;
            try {
                tags = tagFinder.findTags(
                        Parser.parseArtist(filename, NameSeparator.getSEPARATOR()),
                        Parser.parseTitle(filename, NameSeparator.getSEPARATOR()));
            } catch (ParseException e){
                String artist = null;
                if (mp3File.hasId3v2Tag()) {
                    artist = mp3File.getId3v2Tag().getArtist();
                } else if (mp3File.hasId3v1Tag()){
                    artist = mp3File.getId3v1Tag().getArtist();
                }
                if(artist == null){
                    artist = "";
                }
                tags = tagFinder.findTags(artist, FilenameValidator.getWithout(".mp3", filename));
            }
            if (mp3File.hasId3v1Tag()) {
                mp3File.removeId3v1Tag();
            }
            if (mp3File.hasId3v2Tag()) {
                mp3File.removeId3v2Tag();
            }
            if (mp3File.hasCustomTag()) {
                mp3File.removeCustomTag();
            }
            ID3v24Tag tag = fillByMap(tags);

            mp3File.setId3v2Tag(tag);

        } catch (PageNotFoundException|ParseException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mp3File;
    }
    private static ID3v24Tag fillByMap(Map<String, String> tags){
        ID3v24Tag tag = (ID3v24Tag) CustomBeanFactory.getInstance().getBean("id3v24Tag");

        tag.setGenreDescription(tags.get("genre"));
        if(tags.get("date") != null){
            ReleaseDate releaseDate = CustomBeanFactory.getInstance().getBean("googleMusicDateFormat", ReleaseDate.class);
            try {
                releaseDate.fromString(tags.get("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tag.setYear(String.valueOf(releaseDate.get(Calendar.YEAR)));
        }

        tag.setTrack(tags.get("track"));
        tag.setAlbum(tags.get("album"));
        tag.setArtist(tags.get("artist"));
        tag.setTitle(tags.get("title"));

        return tag;
    }

}

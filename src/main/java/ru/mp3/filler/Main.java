package ru.mp3.filler;

import com.mpatric.mp3agic.ID3v2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.mp3.filler.tagwork.TagFiller;
import ru.mp3.filler.tagwork.TagFinder;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("mainconfig.xml");
        final TagFiller tagFiller = context.getBean("tagFillerTest", TagFiller.class);
        final ID3v2 tag = tagFiller.getMp3File().getId3v2Tag();

        TagFinder finder = new TagFinder();
        Map<String, String> tags = finder.findSong(tag.getArtist(), tag.getTitle());
        System.out.println(tags);
    }
}

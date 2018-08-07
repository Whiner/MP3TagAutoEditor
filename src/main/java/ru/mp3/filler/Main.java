package ru.mp3.filler;

import com.mpatric.mp3agic.*;
import org.jsoup.HttpStatusException;
import ru.mp3.filler.parser.FilenameValidator;
import ru.mp3.filler.tagwork.TagFiller;
import ru.mp3.filler.tagwork.TagFinder;
import ru.mp3.filler.tagwork.exceptions.PageNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/*
* надо алгоритм сравнивания слов для поиска трека!!!
* надо защиту от защиты гугла)))) шоб мог подключаться норм (рекурсия не подходит). отличать "не найдено" от защиты
* когда чисто название трека - попробовать парсить с помощью тега. если его нету - то пробовать уже как есть сейчас и переименовывать трек норм
* парсить картинку альбома
* предусмотреть когда альбом смешанный. и пишет Various Artists. тогда нужно парсить из трек листа альбома
* SocketTimeoutException - учесть когда интернет пропадает
* */
public class Main {
    public static void main(String[] args) throws NotSupportedException {
//        final TagFiller test = CustomBeanFactory.getInstance().getBean("tagFillerTest", TagFiller.class);
////
////        Mp3File song = test.getMp3File();
////        final ID3v2 tag = song.getId3v2Tag();
////        System.out.println("Track: " + tag.getTrack());
////        System.out.println("Artist: " + tag.getArtist());
////        System.out.println("Title: " + tag.getTitle());
////        System.out.println("Album: " + tag.getAlbum());
////        System.out.println("Year: " + tag.getYear());
////        System.out.println("Genre: " + tag.getGenre() + " (" + tag.getGenreDescription() + ")");
////        System.out.println("Comment: " + tag.getComment());
////        final String[] music = FileReader.getFilenames("music");
////        for (String song: music){
////            System.out.println("Original: " + song);
////            System.out.println("Validate: " +FilenameValidator.validate(song));
////
////        }





        int all = 1;
        int success = 0;

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("log.txt"))) {
            TagFiller.setTagFiller((TagFinder) CustomBeanFactory.getInstance().getBean("tagFinder"));
            final List<Mp3File> files = Mp3Opener.openAll("music/m");
            all = files.size();
            System.out.println("------------------------------------------");
            for (Mp3File mp3: files){
                File file = new File(mp3.getFilename());
                try {
                    Mp3File filled = TagFiller.fill(mp3);
                    ID3v2 tag = filled.getId3v2Tag();
                    System.out.println("+ " + file.getName());
                    bufferedWriter.write(tag.getArtist() + " - "
                            + tag.getTitle()
                            + " (" + tag.getGenreDescription()
                            + ") Track№" + tag.getTrack()
                            + " Year " + tag.getYear() + "\n");
                    success++;
                    //file.delete();
                } catch (PageNotFoundException e){
                    System.out.println("- " + file.getName());
                    bufferedWriter.write("Ошибка(Page) " + file.getName() + " ------------------------------------\n");
                    //Files.copy(file.toPath(), new File("music/m/" + file.getName()).toPath());
                } catch (ParseException e) {
                    System.out.println("- " + file.getName());
                    bufferedWriter.write("Ошибка(Prse) " + file.getName() + " ------------------------------------\n");
                    //Files.copy(file.toPath(), new File("music/m/" + file.getName()).toPath());
                }
            }
        } catch (InvalidDataException | IOException | UnsupportedTagException e) {
            e.printStackTrace();
        }
        System.out.println("Успех " + (float)success/(float)all*100.0);
        //FilenameValidator.validate("Childish Gambino - This Is America[#NR](feat. Моу лол).mp3");
    }
}

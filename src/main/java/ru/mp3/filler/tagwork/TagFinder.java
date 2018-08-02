package ru.mp3.filler.tagwork;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.mp3.filler.UrlOpener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TagFinder {
    private static final String GOOGLE_MUSIC_SEARCH_URL = "https://play.google.com/store/search?c=music";
    private static final String GOOGLE_MUSIC_HOME_URL = "https://play.google.com";

    public TagFinder() {

    }

    public Map<String, String> findSong(String artist, String title){

        Map<String, String> tags = new HashMap<String, String>();

        artist = artist.trim();
        title = title.trim();
        artist = artist.replace(" ", "%20");
        title = title.replace(" ", "%20");
        String searchAddress = GOOGLE_MUSIC_SEARCH_URL + "&q=" + artist + "%20" + title;

        try {
            Document searchPage = Jsoup.connect(searchAddress).get();
            Elements elements = searchPage.select("a[href]");
            String albumUrl = GOOGLE_MUSIC_HOME_URL;
            for (Element element: elements){
                if(element.attr("title").equals(title)){
                    albumUrl = albumUrl.concat(element.attr("href"));
                    break;
                }
            }
            Document albumPage = Jsoup.connect(albumUrl).get();
            elements = albumPage.select("h1.document-title");
            tags.put("album", elements.first().child(0).text());

            Elements left_info = albumPage.select("div.left-info");
            tags.put("date", left_info.first().child(0).ownText());
            tags.put("genre", left_info.first().child(1).child(0).text());




        } catch (IOException e) {
            e.printStackTrace();
        }
        return tags;
    }

}

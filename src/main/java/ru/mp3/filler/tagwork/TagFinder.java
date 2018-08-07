package ru.mp3.filler.tagwork;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.mp3.filler.TextComparator;
import ru.mp3.filler.tagwork.exceptions.GoogleDdosSecurityException;
import ru.mp3.filler.tagwork.exceptions.PageNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TagFinder { //парсить еще название песни и исполнителя. чтобы в итоге вышло все без ошибок в названии
    private static final String GOOGLE_MUSIC_SEARCH_URL = "https://play.google.com/store/search?c=music";
    private static final String GOOGLE_MUSIC_HOME_URL = "https://play.google.com";
    private static int waitSec = 10;
    public TagFinder() {

    }

    public Map<String, String> findTags(String artist, String title) throws Exception {
        Map<String, String> tags = new HashMap<>();
        boolean found = false;
        waitSec = 10;
        while(!found) {
            try {
                Document albumPage = getAlbumPage(getSearchPage(artist + " " + title), title);
                try {
                    tags.put("album", getAlbum(albumPage));
                } catch (Exception e1) {
                    throw new PageNotFoundException();
                }
                Element leftInfo = getLeftInfo(albumPage);
                tags.put("date", getReleaseDate(leftInfo));
                tags.put("genre", getGenre(leftInfo));
                tags.put("artist", getArtist(leftInfo));

                Elements trackList = getAlbumTrackList(albumPage);
                final Element trackListElement = getTrackListElement(trackList, title);
                if (trackListElement != null) {
                    tags.put("track", getTrackNumber(trackListElement)); //несколько дисков может быть
                    tags.put("title", getTitle(trackListElement));
                }
                found = true;
            } catch (PageNotFoundException e) {
                throw new PageNotFoundException("Не получилось найти этот трек. Попробуйте переименовать его в формате \"Исполнитель - Название\"." +
                        " Если данное условие выполнено - и ошибка повторилась - ничем не могу помочь ¯ \\ _ (ツ) _ / ¯");
            } catch (GoogleDdosSecurityException e) {
                synchronized (this) {
                    System.out.println("waiting... Error: " + e.getMessage());
                    this.wait(1000 * waitSec);
                    waitSec += 5;
                }
            }
        }
        return tags;
    }

    private String getTitle(Element trackListElement) {
        try {
            return trackListElement.select("td.title-cell").first().select("div.title").first().text().trim();
        } catch (NullPointerException e){
            return "";
        }
    }

    private String getArtist(Element leftInfo) {
        try {
            return leftInfo.child(0).child(0).ownText().trim();
        } catch (NullPointerException e){
            return "";
        }
    }

    private Element getTrackListElement(Elements trackList, String title){
        title = title.toLowerCase();
        for (Element el: trackList){
            String s = el
                    .select("td.title-cell")
                    .first()
                    .select("div.title")
                    .first()
                    .text()
                    .toLowerCase()
                    .trim();
            if(TextComparator.compareStrings(title, s) >= 0.5){
                return el;
            }
        }
        return null;
    }
    private String getTrackNumber(Element trackListElement){
        try {
            return trackListElement.child(0).child(0).text().trim();
        } catch (NullPointerException e){
            return "";
        }
    }

    private Element getLeftInfo(Document albumPage){ // дата и жанр находятся в этом блоке
        return albumPage.select("div.left-info").first();
    }
    private String getReleaseDate(Element leftInfo) {
        try {
            return leftInfo.child(0).ownText().trim();
        } catch (NullPointerException e) {
            return "";
        }
    }

    private String getGenre(Element leftInfo){
        try {
            return leftInfo.select("span[itemprop=genre]").first().ownText();
        } catch (NullPointerException e){
            return "";
        }
    }

    private Elements getAlbumTrackList(Document albumPage){
        return albumPage.select("tr.track-list-row");
    }

    private Document getSearchPage(String query) throws GoogleDdosSecurityException {
        query = query.trim();
        query = query.replace(" ", "%20");
        String searchAddress = GOOGLE_MUSIC_SEARCH_URL + "&q=" + query;
        Document doc;
        try {
            doc = Jsoup.connect(searchAddress).get();
        } catch (Exception e){
            throw new GoogleDdosSecurityException("Wait 20-30 sec and return");
        }
        return doc;
    }


    private Document getAlbumPage(Document searchPage, String title) throws PageNotFoundException {
        Elements el = searchPage.select("div.id-cluster-container");

        String albumUrl = GOOGLE_MUSIC_HOME_URL;
        boolean found = false;
        for (Element element: el) {
            if (element.child(0).child(0).child(0).child(0).ownText().equals("Треки")) {
                Element tracksBlock = element.child(0).child(1);

                title = title.toLowerCase();
                Elements tracks = tracksBlock.select("div.card.no-rationale.square-cover.music");
                for (Element track: tracks){
                    String s = track.child(0).child(2).child(1).text().toLowerCase();
                    if(TextComparator.compareStrings(title, s) >= 0.5){
                        Element price = track.select("span.display-price").first();
                        if(price != null && Integer.valueOf(price.ownText().split(",")[0]) < 50){
                            albumUrl = albumUrl.concat(track.child(0).child(2).child(0).attr("href"));
                            found = true;
                            break;
                        }
                    }
                }
                if(found){
                    break;
                }


            }
        }
        Document doc;
        try {
            doc = Jsoup.connect(albumUrl).get();
        } catch (Exception e){
            throw new PageNotFoundException();
        }
        return doc;
    }


    private String getAlbum(Document albumPage){
        return albumPage.select("h1.document-title").first().child(0).text().trim();
    }
}

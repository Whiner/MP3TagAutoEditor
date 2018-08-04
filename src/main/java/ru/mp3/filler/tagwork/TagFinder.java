package ru.mp3.filler.tagwork;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.mp3.filler.UrlOpener;
import ru.mp3.filler.tagwork.exceptions.PageNotFoundException;
import ru.mp3.filler.tagwork.exceptions.UnknownArtistException;
import ru.mp3.filler.tagwork.exceptions.UnknownTitleException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TagFinder { //парсить еще название песни и исполнителя. чтобы в итоге вышло все без ошибок в названии
    private static final String GOOGLE_MUSIC_SEARCH_URL = "https://play.google.com/store/search?c=music";
    private static final String GOOGLE_MUSIC_HOME_URL = "https://play.google.com";

    public TagFinder() {

    }

    public Map<String, String> findTags(String artist, String title) throws PageNotFoundException, Exception {
        Map<String, String> tags = new HashMap<String, String>();
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

        } catch (PageNotFoundException e){
            throw new PageNotFoundException("Не получилось найти этот трек. Попробуйте переименовать его в формате \"Исполнитель - Название\"." +
                    " Если данное условие выполнено - и ошибка повторилась - ничем не могу помочь ¯ \\ _ (ツ) _ / ¯");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tags;
    }

    private String getTitle(Element trackListElement) {
        return trackListElement.select("td.title-cell").first().select("div.title").first().text().trim();
    }

    private String getArtist(Element leftInfo) {
        return  leftInfo.child(0).child(0).ownText().trim();
    }

    private Element getTrackListElement(Elements trackList, String title){
        for (Element el: trackList){
            String s = el
                    .select("td.title-cell")
                    .first()
                    .select("div.title")
                    .first()
                    .text()
                    .trim();
            if(s.equalsIgnoreCase(title) || s.contains(title)){
                return el;
            }
        }
        return null;
    }
    private String getTrackNumber(Element trackListElement){
        return trackListElement.child(0).child(0).text().trim();
    }

    private Element getLeftInfo(Document albumPage){ // дата и жанр находятся в этом блоке
        return albumPage.select("div.left-info").first();
    }
    private String getReleaseDate(Element leftInfo){
        return leftInfo.child(0).ownText().trim();
    }

    private String getGenre(Element leftInfo){
        return  leftInfo.child(1).child(0).text().trim();
    }

    private Elements getAlbumTrackList(Document albumPage){
        return albumPage.select("tr.track-list-row");
    }

    private Document getSearchPage(String query) throws IOException {
        query = query.trim();
        query = query.replace(" ", "%20");
        String searchAddress = GOOGLE_MUSIC_SEARCH_URL + "&q=" + query;
        return Jsoup.connect(searchAddress).get();
    }


    private Document getAlbumPage(Document searchPage, String title) throws IOException { //выбирать исходя из цены. трек стоит меньше 10-19р
        Elements el = searchPage.select("div.id-cluster-container");

        /*Elements elements = searchPage.select("a[href].title");
        Elements elements2 = searchPage.select("h2.single-title-link");*/
        String albumUrl = GOOGLE_MUSIC_HOME_URL;
        for (Element element: el){
            if(element.child(0).child(0).child(0).child(0).ownText().equals("Треки")) {
                String s = element.child(0).child(1).child(0).child(0).child(2).child(1).ownText();
                if (s.contains(title) || s.equalsIgnoreCase(title)) {
                    albumUrl = albumUrl.concat(element.child(0).child(1).child(0).child(0).child(2).child(0).attr("href"));
                    break;
                }
            }

            /*if(element.ownText().contains(title)){
                albumUrl = albumUrl.concat(element.attr("href"));
                break;
            }*/
            /*if(element.attr("title").equalsIgnoreCase(title)){
                albumUrl = albumUrl.concat(element.attr("href"));
                break;
            }*/
        }
        //UrlOpener.open(albumUrl);
        return Jsoup.connect(albumUrl).get();
    }


    private String getAlbum(Document albumPage){
        return albumPage.select("h1.document-title").first().child(0).text().trim();
    }
}

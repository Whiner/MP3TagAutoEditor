package ru.mp3.filler;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlOpener {
    public static void open(String url) {
        Desktop desktop = java.awt.Desktop.getDesktop();
        URI oURL = null;
        try {
            oURL = new URI(url);
            desktop.browse(oURL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

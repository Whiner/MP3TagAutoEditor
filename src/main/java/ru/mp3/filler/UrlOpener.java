package ru.mp3.filler;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlOpener {
    public static void open(String url) throws URISyntaxException, IOException {
        Desktop desktop = java.awt.Desktop.getDesktop();
        URI oURL = new URI(url);
        desktop.browse(oURL);
    }
}

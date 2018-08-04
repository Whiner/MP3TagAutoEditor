package ru.mp3.filler.tagwork.exceptions;

public class PageNotFoundException extends Exception {
    public PageNotFoundException() {
    }

    public PageNotFoundException(String message) {
        super(message);
    }
}

package ru.mp3.filler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ReleaseDate extends java.util.GregorianCalendar {


    private SimpleDateFormat dateFormat;

    public ReleaseDate(){
        super();
    }

    public ReleaseDate(long millis){
        super.setTime(new Date(millis));
    }

    public ReleaseDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }

    @Autowired
    public ReleaseDate(SimpleDateFormat dateFormat){
        this.dateFormat = dateFormat;
    }

    @Override
    public String toString() {
        return dateFormat.format(getTime());
    }

    public void fromString(String string) throws ParseException {
        Date date = dateFormat.parse(string);
        super.setTime(date);
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}

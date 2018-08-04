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
    private SimpleDateFormat spareDateFormat;

    public SimpleDateFormat getSpareDateFormat() {
        return spareDateFormat;
    }

    public void setSpareDateFormat(SimpleDateFormat spareDateFormat) {
        this.spareDateFormat = spareDateFormat;
    }

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
    public ReleaseDate(SimpleDateFormat dateFormat, SimpleDateFormat spareDateFormat){
        this.dateFormat = dateFormat;
        this.spareDateFormat = spareDateFormat;
    }

    @Override
    public String toString() {
        return dateFormat.format(getTime());
    }

    public void fromString(String string) throws ParseException {
        Date date;
        try {
            date = dateFormat.parse(string);
        } catch (ParseException e){
            date = spareDateFormat.parse(string);
        }
        super.setTime(date);
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}

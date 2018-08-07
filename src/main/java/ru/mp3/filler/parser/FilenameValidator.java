package ru.mp3.filler.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameValidator {
    public static String validate(String filename){
        filename = filename.replace('_', ' ');
        filename = getWithout("\\[.*]", filename);
        filename = getWithout("\\(.*[feat|ft|Feat].*\\)", filename);
        filename = getWithout("\\(*ft.*\\)*", filename);
        filename = getWithout("\\(.*\\)", filename);
        return filename.trim();
    }

    public static String getWithout(String pattern, String filename){
        pattern = "(.*)" + pattern + "(.*)";
        Pattern nr = Pattern.compile(pattern);
        final Matcher matcher = nr.matcher(filename);
        if(matcher.find()){
            String returnResult = matcher.group(1).trim();
            if(matcher.group(2) != null){
                returnResult = returnResult.concat(matcher.group(2).trim());
            }
            return returnResult;
        } else {
            return filename;
        }
    }

    public static String getWithin(String pattern, String filename){
        pattern = ".*(" + pattern + ").*";
        Pattern nr = Pattern.compile(pattern);
        final Matcher matcher = nr.matcher(filename);
        if(matcher.find()){
            return matcher.group(1).trim();
        } else {
            return "-";
        }
    }
}

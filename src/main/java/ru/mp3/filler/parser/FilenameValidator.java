package ru.mp3.filler.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameValidator {
    public static String validate(String filename){
        //System.out.println("--------------------------------------------------");
        //System.out.println("Изначально: " + filename);

        //System.out.println("Удалено: " + getWithin("\\[.*]", filename));
        filename = getWithout("\\[.*]", filename);
        //System.out.println(filename);

        //System.out.println("Удалено: " + getWithin("\\(.*[feat|ft|Feat].*\\)", filename));
        filename = getWithout("\\(.*[feat|ft|Feat].*\\)", filename);
        //System.out.println(filename);

        //System.out.println("Итог: " + filename);
        return filename.trim();
    }

    public static String getWithout(String pattern, String filename){
        pattern = "(.*)" + pattern + "(.*)";
        Pattern nr = Pattern.compile(pattern);
        final Matcher matcher = nr.matcher(filename);
        if(matcher.find()){
//            System.out.println("*--------------*(out)Matcher*-------------*");
//            for (int i = 0; i < 3; i++){
//                System.out.println(matcher.group(i));
//            }
//            System.out.println("*-----------------------------------------*");
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
//            System.out.println("*---------------*(in)Matcher*-------------*");
//            for (int i = 0; i < 2; i++){
//                System.out.println(matcher.group(i));
//            }
//            System.out.println("*-----------------------------------------*");
            return matcher.group(1).trim();
        } else {
            return "-";
        }
    }
}

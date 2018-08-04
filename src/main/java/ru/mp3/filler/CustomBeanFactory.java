package ru.mp3.filler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CustomBeanFactory {

    public static ApplicationContext getInstance() {
        if(context == null){
            context = new ClassPathXmlApplicationContext("mainconfig.xml");
        }
        return context;
    }

    private CustomBeanFactory() {
    }

    private static ApplicationContext context;


}

package com.kuzan.permission.commons;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by sui on 2019/3/21.
 */
public class ApplicationContextHelper implements ApplicationContextAware{
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    public static <T> T getBean(Class<T> clazz){
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(clazz);
    }
    public static <T> T getBean(String name,Class<T> clazz){
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(name,clazz);
    }
}

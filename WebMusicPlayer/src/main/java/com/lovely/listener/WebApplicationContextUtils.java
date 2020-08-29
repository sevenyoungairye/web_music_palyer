package com.lovely.listener;

import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * to get spring ApplicationContext
 * @author echo lovely
 * @date 2020/8/16 12:07
 */
public class WebApplicationContextUtils {

    /**
     * get applicationContext  from ServletContext
     * @param sec ServletContext
     * @return applicationContext
     */
    public static ApplicationContext getApp(ServletContext sec) {
        return (ApplicationContext) sec.getAttribute("app");
    }
}

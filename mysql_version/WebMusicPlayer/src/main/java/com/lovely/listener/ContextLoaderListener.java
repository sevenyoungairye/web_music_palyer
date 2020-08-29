package com.lovely.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * 用来创建spring容器
 * @author echo lovely
 * @date 2020/8/16 12:00
 */
public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // get spring config file name
        String applicationContextLocation = sce.getServletContext().getInitParameter("applicationContextLocation");
        // get spring ioc
        ApplicationContext app = new ClassPathXmlApplicationContext(applicationContextLocation);
        // save ApplicationContext in Application scope!
        sce.getServletContext().setAttribute("app", app);

        System.out.println("spring container init...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        String prefix = getClass().getSimpleName() +" destroy() ";

        ServletContext ctx = sce.getServletContext();
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            System.out.println("driver element==================" +  drivers + "===========");
            while(drivers.hasMoreElements()) {
                DriverManager.deregisterDriver(drivers.nextElement());
            }
        } catch(Exception e) {
            ctx.log(prefix + "Exception caught while deregistering JDBC drivers", e);
        }

        ctx.log(prefix + "complete");

    }

}

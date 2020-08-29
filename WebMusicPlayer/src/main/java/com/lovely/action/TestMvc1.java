package com.lovely.action;

import com.lovely.listener.WebApplicationContextUtils;
import com.lovely.mvc.DispatcherAction;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author echo lovely
 * @date 2020/8/13 11:01
 */
public class TestMvc1 extends DispatcherAction {

    private String name;
    public Object test1(HttpServletRequest req, HttpServletResponse resp) {
        ApplicationContext app = WebApplicationContextUtils.getApp(req.getServletContext());
        Object bean = app.getBean("musicUserService");

        System.out.println("bean: " + bean);
        System.out.println(name);
        return "/index.jsp";

    }

}

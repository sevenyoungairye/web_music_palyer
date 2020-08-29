package com.lovely.dao;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author echo lovely
 * @date 2020/8/13 15:27
 */
public class JdbcTemplateTest {

    private JdbcTemplate jdbcTemplate;

    @Test
    public void test1() {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        jdbcTemplate = app.getBean(JdbcTemplate.class);
        System.out.println(jdbcTemplate);
    }

}

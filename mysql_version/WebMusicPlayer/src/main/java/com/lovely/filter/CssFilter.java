package com.lovely.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName = "cssFilter", urlPatterns = "*.css")
public class CssFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filter) throws IOException, ServletException {
		resp.setContentType("text/css;charset=utf-8");
		filter.doFilter(req, resp);
		System.out.println("css dochain..");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("css filter start...");
	}

}

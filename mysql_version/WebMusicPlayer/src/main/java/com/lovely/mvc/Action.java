package com.lovely.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {

	// init ApplicationContext
	public void init(HttpServletRequest req, HttpServletResponse resp);
	
	// ����object����
	public Object service(HttpServletRequest req, HttpServletResponse resp);

}

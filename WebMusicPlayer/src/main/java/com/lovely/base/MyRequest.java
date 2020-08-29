package com.lovely.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MyRequest extends HttpServletRequestWrapper {

	private Map<String, String[]> map;
	
	public MyRequest(HttpServletRequest request) {
		super(request);
		this.map = request.getParameterMap();
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		return this.map;
	}
	
	@Override
	public String getParameter(String name) {
		String[] value = map.get(name);
		if (value!=null && value.length>0) {
			return value[0];
		}
		return null;
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String[] value = map.get(name);
		return value;
	}

	

}

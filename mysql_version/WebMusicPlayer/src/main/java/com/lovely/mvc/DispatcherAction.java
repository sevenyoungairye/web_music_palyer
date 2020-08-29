package com.lovely.mvc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 实体服务类规范
public class DispatcherAction implements Action {


	@Override
	public void init(HttpServletRequest req, HttpServletResponse resp) {
	}

	public Object service(HttpServletRequest req, HttpServletResponse resp) {

		// 初始化 applicationContext;
		init(req, resp);

		// 反射执行子类方法
		Class<?> c = this.getClass();
		// 请求方法
		String method = req.getParameter("method");

		if (method == null || "".equals(method))
			return null;
		
		System.out.println(method);
		
		Object returnValue = null;
		try {
			// 反射拿到子类的方法对象
			Method methodObj = c.getDeclaredMethod(method, HttpServletRequest.class, HttpServletResponse.class);
			
			returnValue = methodObj.invoke(this, req, resp);
			// System.out.println("服务方法返回值: " + returnValue);

		} catch (NoSuchMethodException e) {
			try {
				resp.sendError(404, method + "方法不存在...");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}		
		
		return returnValue;
	}

}

package com.lovely.util;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsAlertUtil {
	
	/**
	 * 用响应流弹出提示消息
	 * @param req 请求对象
	 * @param resp 响应对象
	 * @param msg 你要弹出的消息
	 * @param path 跳转到哪里的页面路径
	 * @return  返回null
	 */
	public static String alert(HttpServletRequest req, HttpServletResponse resp, String msg, String path) {
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = resp.getWriter();
			out.write("<script>");
			out.write("alert('" + msg + "');");
			out.write("location.href = '" + req.getContextPath() + "/" + path + "' ;");
			out.write("</script>");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

}

package com.lovely.util;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsAlertUtil {
	
	/**
	 * ����Ӧ��������ʾ��Ϣ
	 * @param req �������
	 * @param resp ��Ӧ����
	 * @param msg ��Ҫ��������Ϣ
	 * @param path ��ת�������ҳ��·��
	 * @return  ����null
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

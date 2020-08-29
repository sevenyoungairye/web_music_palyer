package com.lovely.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.lovely.base.MyRequest;

/**
 * 编码过滤器
 */
@WebFilter(filterName = "encodingFilter", urlPatterns = "/*")
public class EncodingFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletrequest,
			ServletResponse servletresponse, FilterChain filterchain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletrequest;
		String method = req.getMethod();//获取请求方式
		if (method.equalsIgnoreCase("get")) {
			
			req = new MyRequest(req);
			
			//获取request对象中存储数据的那个map
			Map<String, String[]> map = req.getParameterMap();
			Set<Entry<String, String[]>> enSet = map.entrySet();
			for (Entry<String, String[]> en : enSet) {
				String key = en.getKey();
				String[] value = en.getValue();
				System.out.println("转码前:" + key + "--" + Arrays.toString(value));
				for (int i = 0; i < value.length; i++) {
					//拿出来转码,再放回去覆盖原来乱码的中文 (tomcat8 可不转码)
					// value[i] = new String(value[i].getBytes("ISO8859-1"),"UTF-8");
				}
				System.out.println("转码后:" + key + "--" + Arrays.toString(value));
			}
		}else if (method.equalsIgnoreCase("post")) {
			//请求转码
			servletrequest.setCharacterEncoding("UTF-8");
		}

		/*String resourcePath = req.getServletPath(); // 客户端请求资源路径
		System.out.println("uri======" + resourcePath);*/
		//响应转码
		// servletresponse.setContentType("text/html;charset=utf-8");
		filterchain.doFilter(req, servletresponse);

	}

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {

		System.out.println("encoding start...");
	}

}

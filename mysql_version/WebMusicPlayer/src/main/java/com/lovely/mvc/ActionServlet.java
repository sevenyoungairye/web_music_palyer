package com.lovely.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.lovely.util.ConvertUtil;

// 核心控制器帮忙转发
@WebServlet(name = "ActionServlet", urlPatterns = "*.do")
public class ActionServlet extends HttpServlet {

	/**
	 * 核心控制servlet
	 * 根据访问方式 和 类的路径信息 到对应的servlet来处理
	 */
	private static final long serialVersionUID = 1L;
	
	Map<String, MapperAction> map = new HashMap<String, MapperAction>();

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		// 加载xml内容 调用具体服务类
		SAXReader reader = new SAXReader();

		InputStream in = this.getServletContext().getResourceAsStream("/WEB-INF/config.xml");
		
		try {
			Document doc = reader.read(in);
			
			List<Element> list = doc.selectNodes("//action");

			// System.out.println(list);

			for (Element action : list) {
				String path = action.elementText("path");
				String type = action.elementText("type");
				MapperAction mapper = new MapperAction();
				mapper.setPath(path);
				mapper.setType(type);
		
				map.put(path, mapper);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = req.getRequestURI();
		String key = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
		
		if (map.get(key) == null)
			return;
		
		// 根据请求对象 拿到对应的类 进 行服务
		String value = map.get(key).getType();
		System.out.println("类：" + value);
		
		if (value == null) {
			resp.sendError(404, key + "不在配置文件xml中");
			return;
		}
		
		try {
			// 反射拿到类对象
			Class<?> c = Class.forName(value);
			
			// 创建action对象
			Action action = (Action) c.newInstance();
			
			// 对request请求数据进行封装		
			// xxx服务类直接拿到 前台数据
			Map<String, String[]> requestMap = req.getParameterMap();
			Set<Entry<String,String[]>> entrySet = requestMap.entrySet();
			
			// 对表单提交过来的每个值 封装到xxxaction服务类中的属性
			for (Entry<String, String[]> entry : entrySet) {
				// 表单的名字
				String name = entry.getKey();
				// 表单的值 
				String[] formValue = entry.getValue();
				
				// 判断表单元素的名称是p.name 或者 是name 这里判断是 name
				if (name.indexOf(".") == -1) {
					// 直接提交属性名 为xxxAction中的属性赋值
					try {
						Field field = c.getDeclaredField(name);
						field.setAccessible(true);
						// 属性的类型可能是 String int/Integer double/Double boolean Date/Timstamp Integer[]/int[] Double[]/doble[]					
						field.set(action, ConvertUtil.convert(field, formValue));
					} catch (NoSuchFieldException e) {
						
						//e.printStackTrace(); 可能没这个属性
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					
					
				} else {
					// 提交的值 封装到对象中
					try {
						// p.name p.age 表单元素的名称写法  对象.属性
						String[] split = name.split("\\.");
						// 父级对象属性
						Field fieldEntity = c.getDeclaredField(split[0]);
						fieldEntity.setAccessible(true);
						// 取到对象
						Object entity = fieldEntity.get(action);
						// 空对象
						if (entity == null) {
							// 拿到属性对象的类型 创建对象
							entity = fieldEntity.getType().newInstance();
							fieldEntity.set(action, entity);
						}
						
						// 取到二级对象 
						// f.name 取到name
						
						// 父级属性类对象
						Class<?> ff = fieldEntity.getType();
						Field field = ff.getDeclaredField(split[1]);
						field.setAccessible(true);
						
						// 设置 p.name 的值
						field.set(entity, ConvertUtil.convert(field, formValue));
						
						
					} catch (NoSuchFieldException e) {
						// e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
			Object obj = action.service(req, resp);
			
			if (obj == null || obj == "") {
				return;
			}
			
			if (obj instanceof String) {
				String result = obj.toString();
				// 根据返回结果 决定是请求转发 还是重定向
				if (result.startsWith("forward")) {
					String path = result.split(":")[1];
					req.getRequestDispatcher(path).forward(req, resp);
				} else if (result.startsWith("redirect")) {
					String path = result.split(":")[1];
					resp.sendRedirect(req.getContextPath() + path);
				} else {
					// 写路径默认请求转发
					req.getRequestDispatcher(result).forward(req, resp);
				}	
			} else {
				// 转json
				resp.setContentType("text/json;charset=utf-8");
				PrintWriter out = resp.getWriter();
				out.write(JSON.toJSONString(obj));
				// System.out.println(JSON.toJSONString(obj));
				out.close();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}

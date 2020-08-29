package com.lovely.mvc;

/**
 * 
 * @author echo lovely
 * 
 * action.xml 映射数据, 加载xml中的类
 *
 */
public class MapperAction {
	
	private String path; // 请求路径
	private String type; // 类的全路径
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "MapperAction [path=" + path + ", type=" + type + "]";
	}
	
}

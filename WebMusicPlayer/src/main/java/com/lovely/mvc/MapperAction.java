package com.lovely.mvc;

/**
 * 
 * @author echo lovely
 * 
 * action.xml ӳ������, ����xml�е���
 *
 */
public class MapperAction {
	
	private String path; // ����·��
	private String type; // ���ȫ·��
	
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

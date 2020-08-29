package com.lovely.util;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ConvertUtil {

	/**
	 * 
	 * @param f Ҫ����ֵ�����Զ���
	 * @param value ��ת����ֵ
	 * @return ת�����Զ����Ӧ���͵�ֵ
	 */
	public static Object convert(Field f, String[] value) {
		
		if (value == null || value.length == 0)
			return null;
		
		Object obj = null;
		
		try {
			if (f.getType() == Date.class) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = sdf.parse(value[0]);			
				// ��util����ת���� sql ����
				obj = new Date(date.getTime());
				
			} else if (f.getType() == Timestamp.class) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date date = sdf.parse(value[0]);
				obj = new Timestamp(date.getTime());
			} else if ((f.getType() == int.class || f.getType() == Integer.class) && value[0] != "") { // �ж��ύ�������ǿ��ַ�
				obj = new Integer(value[0]);
			} else if ((f.getType() == double.class || f.getType() == Double.class) && value[0] != "") {
				obj = new Double(value[0]);
			} else if ((f.getType() == long.class || f.getType() == Long.class) && value[0] != "") {
				obj = new Long(value[0]);
			} else if (f.getType() == String.class && value[0] != "") {
				obj = value[0];
			} else if (f.getType() == Integer[].class) {
				Integer[] integer = new Integer[value.length];
				for (int i = 0; i < value.length; i++) {
					integer[i] = new Integer(value[i]);
				}
				obj = integer;
			} else if (f.getType() == int[].class) {
				int[] arr = new int[value.length];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = Integer.parseInt(value[i]);
				}
			} else if ((f.getType() == boolean.class || f.getType() == Boolean.class) && value[0] != "") {
				if (value[0] == "y" || value[0] == "1" || value[0] == "true") {
					obj = true;
				} else {
					obj = false;
				}		
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}

}

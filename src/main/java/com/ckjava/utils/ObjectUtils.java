package com.ckjava.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	/**
	 * 对象的所有字段都为空才返回true
	 * 
	 * @param Object obj
	 * @return boolean
	 */
	public static boolean isEmptyObject(Object obj) {
		if (obj == null || obj.toString().equals("")) {
			return true;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				boolean flag = field.isAccessible();
				boolean isStatic = Modifier.toString(field.getModifiers()).contains("static");
				if (isStatic) {
					continue;
				}
				if (!flag) {
					field.setAccessible(true);
				}
				Object fieldObj = field.get(obj);
				if (fieldObj != null && !fieldObj.toString().equals("")) {
					return false;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * 对象的所有字段都不为空才返回 true
	 * 
	 * @param Object obj
	 * @return boolean
	 */
	public static boolean isNotEmptyObject(Object obj) {
		if (obj == null || obj.toString().equals("")) {
			return false;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				boolean flag = field.isAccessible();
				boolean isStatic = Modifier.toString(field.getModifiers()).contains("static");
				if (isStatic) {
					continue;
				}
				if (!flag) {
					field.setAccessible(true);
				}
				Object fieldObj = field.get(obj);
				if (fieldObj == null || fieldObj.toString().equals("")) {
					return false;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * 将一个对象中不为空的字段拼接成 key1=value1&key2=value2
	 * 
	 * @param obj
	 * @return
	 */
	public static String getObjectString(Object obj) {
		if (obj == null || obj.toString().equals("")) {
			return null;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		StringBuilder data = new StringBuilder();
		for (Field field : fields) {
			try {
				boolean flag = field.isAccessible();
				boolean isStatic = Modifier.toString(field.getModifiers()).contains("static");
				if (isStatic) {
					continue;
				}
				if (!flag) {
					field.setAccessible(true);
				}
				Class<?> clazz = field.getType();
				if (clazz.equals(obj.getClass())) {
					continue;
				}
				Object fieldObj = field.get(obj);
				if (fieldObj != null && !fieldObj.toString().equals("")) {
					data.append(field.getName()).append("=").append(String.valueOf(fieldObj)).append("&");
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return data.toString();
	}
	
	/**
	 * 将 key1=value1&key2=value2的字符串存入Map中
	 * 
	 * @param data String
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> fillMapWithString(String data) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (StringUtils.isBlank(data)) {
			return dataMap;
		}
		String[] datas = data.split("&");
		for (String str : datas) {
			String[] keyValue = str.split("=");
			dataMap.put(ArrayUtils.getValue(keyValue, 0), ArrayUtils.getValue(keyValue, 1));
		}
		return dataMap;
	}
}

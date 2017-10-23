package com.ckjava.utils;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	public static String getUTF8Str(String ocharset, String str) throws Exception {
		try {
			if (str != null && ocharset != null) {
				return new String(str.getBytes(ocharset), "UTF-8");
			}
			return null;
		} catch (Exception e) {
			throw new Exception("get string UTF-8 string occurrence error", e);
		}
	}
	
	public static String getProperStr(String ocharset, String str) throws Exception {
		try {
			if (str != null && ocharset != null) {
				return new String(str.getBytes(ocharset), ocharset);
			}
			return null;
		} catch (Exception e) {
			throw new Exception("get proper string occurrence error", e);
		}
	}
	
	public static String getCleanStr(Object object) {
		return object != null ? object.toString().replace("'", "‘").replace("\"", "”") : "";
	}
	
	/**
	 * 入库前过滤特殊字符
	 * @param Object original
	 * @return String
	 */
	public static String getCleanXmlString(Object original) {
		String data = getStr(original);
		data = data.replaceAll("&", "&amp;");
		data = data.replaceAll("<", "&lt;");
		data = data.replaceAll(">", "&gt;");
		data = data.replaceAll("\"", "&quot;");
		data = data.replaceAll("'", "&apos;");
		
		data = data.replaceAll("•", "&#8226;"); // 加重号 U+2022
		return data;
	}
	
	/**
	 * 回退过滤特殊字符
	 * @param Object original
	 * @return String
	 */
	public static String getDecodeCleanXmlString(Object original) {
		String data = getStr(original);
		data = data.replaceAll("&amp;", "&");
		data = data.replaceAll("&lt;", "<");
		data = data.replaceAll("&gt;", ">");
		data = data.replaceAll("&quot;", "\"");
		data = data.replaceAll("&apos;", "'");
		return data;
	}
	

	/**
	 * 获取Object对象的 String对象
	 * @param obj Object对象
	 * @return
	 */
	public static boolean isNotNullAndNotBlank(Object obj) {
		return obj != null && getStr(obj).trim().length() > 0;
	}

	/**
	 * 获取Object对象的 String对象
	 * @param obj Object对象
	 * @return
	 */
	public static String getStr(Object obj) {
		return obj != null ? obj.toString() : "";
	}
	
	/**
	 * 获取Object对象的 String对象，如果为空返回默认值
	 * @param obj Object对象
	 * @param defaultStr 默认值
	 * @return
	 */
	public static String getStr(Object obj, String defaultStr) {
		String str = getStr(obj);
		return str.equals("") ? defaultStr : str;
	}
	
	/**
	 * 判断某个 Object对象 里面是否含有指定的字符串
	 * @param obj Object对象
	 * @param str 指定的字符串
	 * @return true:含有，false:不含有
	 */
	public static boolean objectHasStr(Object obj, String str) {
		String objStr = getStr(obj);
		return objStr.contains(str);
	}
	
	/**
	 * 判断字符串是否在字符数组中
	 * @param str 字符串
	 * @param strs 字符数组
	 * @return true:在，false:不在
	 */
	public static boolean containsStr(String str, String[] strs) {
		for (String string : strs) {
			if (str.contains(string)) {
				return true;
			}
		}
		return false;
	}
	
}

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
	 * 将null的Object转成""
	 * @param obj
	 * @return String
	 */
	public static String getStr(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	public static String getStr(Object object, String defaultString) {
		return object != null ? (object.toString().equals("") ? defaultString : object.toString()) : defaultString;
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
	
}

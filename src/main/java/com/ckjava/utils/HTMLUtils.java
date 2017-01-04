package com.ckjava.utils;

public class HTMLUtils {
	public static String htmlEscape(String htmlstr) {
		if (StringUtils.isEmpty(htmlstr)) {
			return htmlstr;
		}
		htmlstr = htmlstr.replace("'", "&acute;");
		htmlstr = htmlstr.replace("\"", "&quot;");
		htmlstr = htmlstr.replace("&", "&amp;");
		htmlstr = htmlstr.replace("<", "&lt;");
		htmlstr = htmlstr.replace(">", "&gt;");
		return htmlstr;
	}
	
	public static String parseHtml(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		str = str.replace("&acute;", "'");
		str = str.replace("&quot;", "\"");
		str = str.replace("&amp;", "&");
		str = str.replace("&lt;", "<");
		str = str.replace("&gt;", ">");
		return str;
	}
}

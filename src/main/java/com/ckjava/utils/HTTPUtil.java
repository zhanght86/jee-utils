package com.ckjava.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * HTTP 相关功能
 * @author chen_k
 *
 */
public class HTTPUtil {
	
	protected static final String ENCODE = "utf-8";

	/**
	 * 向url添加请求参数的键值对
	 * @param url 请求url
	 * @param paramname 参数名称
	 * @param value 参数值
	 * @throws UnsupportedEncodingException 
	 */
	public void addParamValue(String url, String paramname, String value) throws UnsupportedEncodingException {
		if (paramname == null || value == null) {
			return;
		}
		String tempUrl = url;
		if (paramname.length() != paramname.getBytes().length && value.length() != value.getBytes().length) {
			tempUrl += URLEncoder.encode(paramname, ENCODE) + "=" + URLEncoder.encode(value, ENCODE) + "&";
			if (isValidUrl(tempUrl)) {
				url = tempUrl;
			}
		}
		if (paramname.length() == paramname.getBytes().length && value.length() != value.getBytes().length) {
			tempUrl += paramname + "=" + URLEncoder.encode(value, ENCODE) + "&";
			if (isValidUrl(tempUrl)) {
				url = tempUrl;
			}
		}
		if (paramname.length() != paramname.getBytes().length && value.length() == value.getBytes().length) {
			tempUrl += URLEncoder.encode(paramname, ENCODE) + "=" + value + "&";
			if (isValidUrl(tempUrl)) {
				url = tempUrl;
			}
		}
		if (paramname.length() == paramname.getBytes().length && value.length() == value.getBytes().length) {
			tempUrl += paramname + "=" + value + "&";
			if (isValidUrl(tempUrl)) {
				url = tempUrl;
			}
		}
	}
	
	/**
	 * 拼接url时候判断是否合法
	 * @param urlString
	 * @return
	 */
	public boolean isValidUrl(String urlString) {
	    URI uri = null;
	    try {
	        uri = new URI(urlString);
	    } catch (URISyntaxException e) {
	        return false;
	    }

	    if (uri.getHost() == null) {
	        return false;
	    }
	    
	    if (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")) {
	        return true;
	    }
	    return false;
	}
}

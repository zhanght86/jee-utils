package com.ckjava.utils;

import java.nio.charset.UnsupportedCharsetException;

public class CharsetUtils {
	
	public static String getEncoding(String str) throws Exception {
		if (str == null) {
			throw new IllegalArgumentException("judge string encoding, the parameter is null");
		}
		try {
	        String encode = "GB2312";
	        if (str.equals(new String(str.getBytes(encode), encode))) {   
	           return encode;   
	        }
	        encode = "ISO-8859-1";   
	        if (str.equals(new String(str.getBytes(encode), encode))) {   
	           return encode;   
	        }
	        encode = "UTF-8";   
	        if (str.equals(new String(str.getBytes(encode), encode))) {   
	           return encode;   
	        }
	        encode = "GBK";   
	        if (str.equals(new String(str.getBytes(encode), encode))) {   
	           return encode;   
	        }
		} catch (Exception e) {
			if (e instanceof UnsupportedCharsetException) {
				throw new UnsupportedCharsetException("cann't judge the string's encoding");
			} else {
				throw new Exception("judge the string's encoding occurrence error", e);
			}
		}
		return null;
    }
	
}

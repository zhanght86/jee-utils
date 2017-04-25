package com.ckjava.utils;

import java.nio.charset.UnsupportedCharsetException;

public class CharsetUtils {
	
	public static final String ENCODE_UTF_8 = "UTF-8";
	public static final String ENCODE_GB2312 = "GB2312";
	public static final String ENCODE_ISO_8859_1 = "ISO-8859-1";
	public static final String ENCODE_GBK = "GBK";
	
	public static String getEncoding(String str) throws Exception {
		if (str == null) {
			throw new IllegalArgumentException("judge string encoding, the parameter is null");
		}
		try {
	        String encode = ENCODE_GB2312;
	        if (str.equals(new String(str.getBytes(encode), encode))) {   
	           return encode;   
	        }
	        encode = ENCODE_ISO_8859_1;   
	        if (str.equals(new String(str.getBytes(encode), encode))) {   
	           return encode;   
	        }
	        encode = ENCODE_UTF_8;   
	        if (str.equals(new String(str.getBytes(encode), encode))) {   
	           return encode;   
	        }
	        encode = ENCODE_GBK;   
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

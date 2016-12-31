package com.ckjava.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class IOUtils {
	
	public static String getString(InputStream is, String charset) throws Exception {
		if (is == null) {
			throw new IllegalArgumentException("InputStream is null");
		}
		StringBuilder str = new StringBuilder();
		int temp = 0;
		byte[] b = new byte[102400];
		try {
			while ((temp = is.read(b, 0, b.length)) != -1) {
				str.append(new String(b, 0, temp, charset));
			}
			is.close();
		} catch (Exception e) {
			if (e instanceof UnsupportedEncodingException) {
				throw new UnsupportedEncodingException(charset + "unknown encoding type");
			} else {
				throw new Exception("IOUtil.getString(InputStream is, String charset)", e);
			}
		}
		return str.toString();
	}
	
	public static String getString(InputStream is) throws Exception {
		if (is == null) {
			throw new IllegalArgumentException("InputStream is null");
		}
		StringBuilder str = new StringBuilder();
		int temp = 0;
		byte[] b = new byte[102400];
		try {
			while ((temp = is.read(b, 0, b.length)) != -1) {
				str.append(new String(b, 0, temp));
			}
			is.close();
		} catch (Exception e) {
			throw new Exception("IOUtil.getString(InputStream is)", e);
		}
		return str.toString();
	}
}

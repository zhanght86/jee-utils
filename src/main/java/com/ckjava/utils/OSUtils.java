package com.ckjava.utils;

import java.util.Properties;

/**
 * 操作系统相关工具方法
 * 
 * @author ck
 *
 */
public class OSUtils {
	
	public static final String WINDOWS = "windows";
	public static final String MAC = "mac";
	public static final String LINUX = "linux";
	
	private static Properties props = System.getProperties();

	/**
	 * 获取当前的操作系统类型
	 *
	 * @return windows or mac or linux
	 */
	public static String getCurrentOSType() {
		String osName = props.getProperty("os.name").toLowerCase();
		if (osName.contains(WINDOWS)) {
			return WINDOWS;
		} else if (osName.contains(MAC)) {
			return MAC;
		} else if (osName.contains(LINUX)) {
			return LINUX;
		} else {
			return null;
		}
	}
}

package com.ckjava.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CommandUtils {
	public static InputStream exec(String command) throws IOException {
		return exec(command , null , null);
	}

	public static InputStream exec(String command, String workpath) throws IOException {
		return exec(command , null , workpath);
	}
	
	public static InputStream exec(String command, String[] envp, String workpath) throws IOException {
		File dir = null;
		if(null != workpath) {
			dir = new File(workpath);
		}
		return Runtime.getRuntime().exec(command, envp, dir).getInputStream();
	}

	public static InputStream exec(String[] commands) throws IOException {
		return exec(commands , null , null);
	}

	public static InputStream exec(String[] commands, String workpath) throws IOException {
		return exec(commands , null , workpath);
	}
	
	public static InputStream exec(String[] commands, String[] envp , String workpath) throws IOException {
		File dir=null;
		if(null != workpath) {
			dir=new File(workpath);
		}
		return Runtime.getRuntime().exec(commands, envp, dir).getInputStream();
	}
}
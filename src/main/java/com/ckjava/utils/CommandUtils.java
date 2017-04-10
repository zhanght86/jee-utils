package com.ckjava.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandUtils {
	
	public static final String COMMAND_FLAG = "command";
	public static final String RESULT_TYPE_RESULT = "result";
	public static final String RESULT_TYPE_ERROR = "error";
	
	private static Logger logger = LoggerFactory.getLogger(CommandUtils.class);
	
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
	
	/**
	 * 执行命令
	 * 
	 * @param command 命令
	 * @param output 输出的结果
	 */
	public static void execTask(String command, StringBuffer output) {
		logger.info(" thread name = {}, start execute command = {} ", new Object[]{Thread.currentThread().getName(), command});
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
		
		Process proc = null;
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try {
			proc = run.exec(command);// 启动另一个进程来执行命令
			// 读取错误输出
			executorService.submit(new StreamGobbler(proc.getErrorStream(), RESULT_TYPE_ERROR, output));
			// 读取正常输出
			executorService.submit(new StreamGobbler(proc.getInputStream(), RESULT_TYPE_RESULT, output));
		    // 检查命令是否执行失败 0 表示正常终止, 这种情况通常是 "java -jar ..." 之类的命令会一直在后台执行,无法终止,只有系统重启或者杀死该进程后才能正常
		    if (proc.waitFor() != 0) {
		    	output.append("执行命令没有正常终止");
		    	logger.error("执行命令没有正常终止");
		        if (proc.exitValue() == 1) { // p.exitValue()==0表示正常结束，1：非正常结束
		        	logger.error("执行命令没有正常返回结果，执行失败");
		        	output.append(",执行命令没有正常返回结果，执行失败");
		        }
		        
		    }
		} catch (Exception e) {
			logger.error("执行命令出现异常", e);
			output.append("执行命令出现异常");
		} finally {
			try {
				proc.destroy(); 
				executorService.shutdown();	
			} catch (Exception e2) {
			}
		}
		logger.info(" thread name = {}, finish execute command = {}", new Object[]{Thread.currentThread().getName(), command});
	}
	
	public static class StreamGobbler implements Runnable {
		private StringBuffer output;
	    private InputStream is;
	    private String type;
	  
	    private StreamGobbler(InputStream is, String type, StringBuffer output) {
	        this.is = is;
	        this.type = type;
	        this.output = output;
	    }  
	  
	    public void run() {
	    	InputStreamReader isr = null;
	    	BufferedReader inReader = null;
	    	String tempStr = null;
	        try {
	            isr = new InputStreamReader(is);
	            inReader = new BufferedReader(isr);
			    while ((tempStr = inReader.readLine()) != null) {
			    	output.append(tempStr).append("\n");
			    }
	        } catch (IOException ioe) {
	        	logger.error("read Process InputStream has error, type="+type);
	        } finally {
	        	try {
	        		inReader.close();
	        		isr.close();
				} catch (Exception e) {
				}
	        }
	    }
	}
	
}
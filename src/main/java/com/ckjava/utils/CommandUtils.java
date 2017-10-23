package com.ckjava.utils;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandUtils {
	
	private static Logger logger = LoggerFactory.getLogger(CommandUtils.class);
	
	/**
	 * 执行命令
	 * 
	 * @param command 命令
	 * @param charset 执行命令所在的系统编码
	 * @param output 输出的结果
	 */
	public static void execTask(String command, String[] envp, File dir, String charset, StringBuffer output) {
		logger.info("thread name = {}, start execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
		
		Process proc = null;
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try {
			proc = run.exec(command, envp, dir);// 启动另一个进程来执行命令
			// 读取错误输出
			Future<?> errorFuture = executorService.submit(new StreamGobbler(proc.getErrorStream(), charset, output));
			// 读取正常输出
			Future<?> normalFuture = executorService.submit(new StreamGobbler(proc.getInputStream(), charset, output));
		    // 检查命令是否执行失败 0 表示正常终止, 这种情况通常是 "java -jar ..." 之类的命令会一直在后台执行,无法终止,只有系统重启或者杀死该进程后才能正常
		    if (proc.waitFor() != 0) {
		    	output.append("执行命令没有正常终止");
		    	logger.error("执行命令没有正常终止");
		        if (proc.exitValue() == 1) { // p.exitValue()==0表示正常结束，1：非正常结束
		        	logger.error("执行命令没有正常返回结果，执行失败");
		        	output.append(",执行命令没有正常返回结果，执行失败");
		        }
		        
		    }
		    
		    errorFuture.get();
		    normalFuture.get();
		} catch (Exception e) {
			logger.error("执行命令出现异常", e);
			output.append("执行命令出现异常");
		} finally {
			try {
				proc.destroy();
				executorService.shutdown();
			} catch (Exception e2) {
			}
		
			logger.info("thread name = {}, finish execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		}
	}
	
	/**
	 * 执行命令并将输出写出到 OutputStream 中
	 * 
	 * @param command 在所在系统执行的命令
	 * @param charset 执行命令所在的系统编码
	 * @param output OutputStream 
	 * 
	 */
	public static void execTask(String command, String[] envp, File dir, String charset, OutputStream output) {
		logger.info("thread name = {}, start execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
		
		Process proc = null;
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try {
			proc = run.exec(command, envp, dir);// 启动另一个进程来执行命令
			// 读取错误输出
			Future<?> errorFuture = executorService.submit(new WriteToStream(proc.getErrorStream(), charset, output));
			// 读取正常输出
			Future<?> normalFuture = executorService.submit(new WriteToStream(proc.getInputStream(), charset, output));
			// 接收socket输入
			// executorService.submit(new RobotThread(socketInput));
			// 检查命令是否执行失败 0 表示正常终止, 这种情况通常是 "java -jar ..." 之类的命令会一直在后台执行,无法终止,只有系统重启或者杀死该进程后才能正常
		    if (proc.waitFor() != 0) {
		    	writeString("执行命令没有正常终止", output);
		    	logger.error("执行命令没有正常终止");
		        if (proc.exitValue() == 1) { // p.exitValue()==0表示正常结束，1：非正常结束
		        	writeString("执行命令没有正常返回结果，执行失败", output);
		        	logger.error("执行命令没有正常返回结果，执行失败");
		        }
		    }
		    
		    errorFuture.get();
		    normalFuture.get();
		} catch (Exception e) {
			logger.error("执行命令出现异常", e);
			writeString("执行命令出现异常", output);
		} finally {
			try {
				proc.destroy(); 
				executorService.shutdown();	
			} catch (Exception e2) {
			}
			logger.info("thread name = {}, finish execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		}
		
	}
	
	/**
	 * 执行命令并将输出写出到 OutputStream 中
	 * @param command 在所在系统执行的命令
	 * @param charset 执行命令所在的系统编码
	 * @param startRobotSign
	 * @param output OutputStream 
	 */
	public static void execTask(String command, String[] envp, File dir, String charset, String startRobotSign, OutputStream output) {
		logger.info("thread name = {}, start execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
		
		Process proc = null;
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		try {
			proc = run.exec(command, envp, dir);// 启动另一个进程来执行命令
			// 读取错误输出
			Future<?> errorFuture = executorService.submit(new WriteToStream(proc.getErrorStream(), charset, output));
			// 读取正常输出
			Future<?> normalFuture = executorService.submit(new WriteToStream(proc.getInputStream(), charset, output));
			// 启动机器人线程
			Future<?> robotFuture =  executorService.submit(new RobotThread(startRobotSign));
			// 检查命令是否执行失败 0 表示正常终止, 这种情况通常是 "java -jar ..." 之类的命令会一直在后台执行,无法终止,只有系统重启或者杀死该进程后才能正常
		    if (proc.waitFor() != 0) {
		    	writeString("执行命令没有正常终止", output);
		    	logger.error("执行命令没有正常终止");
		        if (proc.exitValue() == 1) { // p.exitValue()==0表示正常结束，1：非正常结束
		        	writeString("执行命令没有正常返回结果，执行失败", output);
		        	logger.error("执行命令没有正常返回结果，执行失败");
		        }
		    }
		    
		    errorFuture.get();
		    normalFuture.get();
		    robotFuture.get();
		} catch (Exception e) {
			logger.error("执行命令出现异常", e);
			writeString("执行命令出现异常", output);
		} finally {
			try {
				proc.destroy(); 
				executorService.shutdown();	
			} catch (Exception e2) {
			}
			logger.info("thread name = {}, finish execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		}
	}
	
	private static class RobotThread implements Runnable {
		private String startRobotSign;
		
		public RobotThread(String startRobotSign) {
			super();
			this.startRobotSign = startRobotSign;
		}

		@Override
		public void run() {
			long current = System.currentTimeMillis();
			while (true) {
				long now = System.currentTimeMillis();
	    		if (now - current >= 1000) { // 每秒检查一次
	    			current = now;
	    			logger.info("robot wait to start");
	    			File file = new File(startRobotSign);
	    			if (file.exists()) {
	    				logger.info("start robot");
	    				try {
				    		Robot robot = new Robot();
				    		robot.keyPress(KeyEvent.VK_Q);
				    		robot.keyRelease(KeyEvent.VK_Q);
				    		robot.keyPress(KeyEvent.VK_ENTER);
				    		robot.keyRelease(KeyEvent.VK_ENTER);
				    		file.delete();
				    		logger.info("robot send Q success");
				    		break;
						} catch (Exception e) {
							logger.error("robot send Q has error", e);
							break;
						}
	    			}
	    		}
			}
		}
		
	}

	private static class StreamGobbler implements Runnable {
		private InputStream is; // Process 正常和错误输出流
		private StringBuffer output; // 输出信息
		private String charset;

		private StreamGobbler(InputStream is, String charset, StringBuffer output) {
	        this.is = is;
	        this.output = output;
	        this.charset = charset;
	    }  
	  
	    public void run() {
	    	BufferedReader inReader = null;
	    	String tempStr = null;
	        try {
	            inReader = new BufferedReader(new InputStreamReader(is, Charset.forName(charset)));
			    while ((tempStr = inReader.readLine()) != null) {
			    	output.append(tempStr).append("\n");
			    }
	        } catch (IOException e) {
	        	logger.error("read Process InputStream has error", e);
            } finally {
                try {
                    inReader.close();
                } catch (Exception e) {
                }
            }
	    }
	}
	
	private static class WriteToStream implements Runnable {
	    private InputStream input; // Process stream
	    private OutputStream output; // don't close!
	    private String charset;
	    
	    private WriteToStream(InputStream input, String charset, OutputStream output) {
	        this.input = input;
	        this.charset = charset;
	        this.output = output;
	    }
	    
	    public void run() {
	    	BufferedReader inReader = null;
	        try {
	            inReader = new BufferedReader(new InputStreamReader(input, Charset.forName(charset)));

	            String tempStr = null;
			    while ((tempStr = inReader.readLine()) != null) {
			    	writeString(tempStr, output);
			    }
	        } catch (IOException ioe) {
	        	logger.error("WriteToStream has error", ioe);
            } finally {
                try {
                	input.close();
                    inReader.close();
                } catch (Exception e) {
                }
            }
	    }
	}
	
	private static void writeString(String message, OutputStream output) {
        try {
        	DataOutputStream dos = new DataOutputStream(output); // 外部输出流,不要关闭
        	dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
        	logger.info("CommandUtils writeString method has error", e);
        }
    }
	
}
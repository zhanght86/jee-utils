package com.ckjava.utils;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandUtils {
	
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
		logger.info("thread name = {}, start execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
		
		Process proc = null;
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try {
			proc = run.exec(command);// 启动另一个进程来执行命令
			// 读取错误输出
			executorService.submit(new StreamGobbler(proc.getErrorStream(), output));
			// 读取正常输出
			executorService.submit(new StreamGobbler(proc.getInputStream(), output));
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
		logger.info("thread name = {}, finish execute command = {}", new Object[]{Thread.currentThread().getName(), command});
	}
	
	/**
	 * 执行命令并将输出写出到 OutputStream 中
	 * 
	 * @param command 命令
	 * @param socketOutput OutputStream 
	 * 
	 */
	public static void execTask(String command, OutputStream socketOutput) {
		logger.info("thread name = {}, start execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
		
		Process proc = null;
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		try {
			proc = run.exec(command);// 启动另一个进程来执行命令
			StringBuffer data = new StringBuffer();
			// 读取错误输出
			executorService.submit(new WriteToStream(proc.getErrorStream(), "", socketOutput, data));
			// 读取正常输出
			executorService.submit(new WriteToStream(proc.getInputStream(), "", socketOutput, data));
			// 接收socket输入
			// executorService.submit(new RobotThread(socketInput));
			// 检查命令是否执行失败 0 表示正常终止, 这种情况通常是 "java -jar ..." 之类的命令会一直在后台执行,无法终止,只有系统重启或者杀死该进程后才能正常
		    if (proc.waitFor() != 0) {
		    	writeString("执行命令没有正常终止", socketOutput);
		    	logger.error("执行命令没有正常终止");
		        if (proc.exitValue() == 1) { // p.exitValue()==0表示正常结束，1：非正常结束
		        	writeString("执行命令没有正常返回结果，执行失败", socketOutput);
		        	logger.error("执行命令没有正常返回结果，执行失败");
		        }
		    }
		} catch (Exception e) {
			logger.error("执行命令出现异常", e);
			writeString("执行命令出现异常", socketOutput);
		} finally {
			try {
				proc.destroy(); 
				executorService.shutdown();	
			} catch (Exception e2) {
			}
		}
		logger.info("thread name = {}, finish execute command = {}", new Object[]{Thread.currentThread().getName(), command});
	}
	
	public static void execTask(String command, String startRobotSign, OutputStream socketOutput) {
		logger.info("thread name = {}, start execute command = {}", new Object[]{Thread.currentThread().getName(), command});
		Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
		
		Process proc = null;
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		try {
			proc = run.exec(command);// 启动另一个进程来执行命令
			StringBuffer data = new StringBuffer();
			// 读取错误输出
			executorService.submit(new WriteToStream(proc.getErrorStream(), startRobotSign, socketOutput, data));
			// 读取正常输出
			executorService.submit(new WriteToStream(proc.getInputStream(), startRobotSign, socketOutput, data));
			
			executorService.submit(new RobotThread(startRobotSign));
			
			// 接收socket输入
			// executorService.submit(new RobotThread(socketInput));
			// 检查命令是否执行失败 0 表示正常终止, 这种情况通常是 "java -jar ..." 之类的命令会一直在后台执行,无法终止,只有系统重启或者杀死该进程后才能正常
		    if (proc.waitFor() != 0) {
		    	writeString("执行命令没有正常终止", socketOutput);
		    	logger.error("执行命令没有正常终止");
		        if (proc.exitValue() == 1) { // p.exitValue()==0表示正常结束，1：非正常结束
		        	writeString("执行命令没有正常返回结果，执行失败", socketOutput);
		        	logger.error("执行命令没有正常返回结果，执行失败");
		        }
		    }
		} catch (Exception e) {
			logger.error("执行命令出现异常", e);
			writeString("执行命令出现异常", socketOutput);
		} finally {
			try {
				proc.destroy(); 
				executorService.shutdown();	
			} catch (Exception e2) {
			}
		}
		logger.info("thread name = {}, finish execute command = {}", new Object[]{Thread.currentThread().getName(), command});
	}
	
	public static class RobotThread implements Runnable {
		private String startRobotSign;
		
		public RobotThread(String startRobotSign) {
			super();
			this.startRobotSign = startRobotSign;
		}

		@Override
		public void run() {
			long current = System.currentTimeMillis();
			boolean isrun = true;
			while (isrun) {
				long now = System.currentTimeMillis();
	    		if (now - current >= 1000) { // 每秒检查一次
	    			File file = new File(startRobotSign);
	    			System.out.println("client:" + file.getAbsolutePath());
	    			if (file.exists()) {
	    				try {
				    		Robot robot = new Robot();
				    		robot.keyPress(KeyEvent.VK_Q);
				    		robot.keyRelease(KeyEvent.VK_Q);
				    		robot.keyPress(KeyEvent.VK_ENTER);
				    		robot.keyRelease(KeyEvent.VK_ENTER);
				    		file.delete();
				    		logger.info("robot send Q success");
				    		isrun = false;
						} catch (Exception e) {
							logger.error("robot send Q has error", e);
						}
	    			}
	    			current = now;
	    		}
			}
		}
		
	}

	public static class StreamGobbler implements Runnable {
		private InputStream is; // Process 正常和错误输出流
		private StringBuffer output; // 输出信息

		private StreamGobbler(InputStream is, StringBuffer output) {
	        this.is = is;
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
	        	logger.error("read Process InputStream has error");
	        } finally {
	        	try {
	        		inReader.close();
	        		isr.close();
				} catch (Exception e) {
				}
	        }
	    }
	}
	
	public static class WriteToStream implements Runnable {
	    private InputStream input; // Process 输入流
	    private OutputStream socketOutput; // socket 输出到外部,不要关闭
	    private StringBuffer data;
	    private String startRobotSign;
	    
	    private WriteToStream(InputStream input, String startRobotSign, OutputStream socketOutput, StringBuffer data) {
	        this.input = input;
	        this.socketOutput = socketOutput;
	        this.data = data;
	        this.startRobotSign = startRobotSign;
	    }
	    
	    public void run() {
	    	BufferedReader inReader = null;
	    	
	    	DataOutputStream socketDos = null;
	        try {
	            inReader = new BufferedReader(new InputStreamReader(input));
	            socketDos = new DataOutputStream(socketOutput);
	            
	            String tempStr = null;
			    while ((tempStr = inReader.readLine()) != null) {
			    	logger.debug("process output:"+tempStr);

			    	if (tempStr.contains("Successfully opened log file")) {
			    		logger.info("find Successfully opened log file");

			    		// 按Q退出进程
			    		/*try {
				    		Robot robot = new Robot();
				    		robot.keyPress(KeyEvent.VK_Q);
				    		robot.keyRelease(KeyEvent.VK_Q);
				    		robot.keyPress(KeyEvent.VK_ENTER);
				    		robot.keyRelease(KeyEvent.VK_ENTER);
				    		logger.info("robot send q success");
						} catch (Exception e) {
							logger.error("robot send q has error", e);
						}*/
			    	}
			    	
			    	data.append(tempStr);
			    	socketDos.writeUTF(tempStr);
			    }
			    
			    socketDos.flush();
	        } catch (IOException ioe) {
	        	logger.error("read Process InputStream has error");
	        } finally {
	        	try {
	        		inReader.close();
	        		input.close();
				} catch (Exception e) {
				}
	        }
	    }
	}
	
	public static class ReadWriteToStream implements Runnable {
	    private InputStream input; // 外部输入流, 不要关闭
	    private OutputStream poutput; // Process输入流, 外部向process写入数据
	    
	    private ReadWriteToStream(InputStream input, OutputStream poutput) {
	        this.input = input;
	        this.poutput = poutput;
	    }  
	  
	    public void run() {
	    	InputStreamReader isr = null;
	    	BufferedReader inReader = null;
	    	PrintWriter pw = null;
	        try {
	        	isr = new InputStreamReader(input);
	            inReader = new BufferedReader(isr);
	            pw = new PrintWriter(poutput);
	            String tempStr = null;
			    while ((tempStr = inReader.readLine()) != null) {
			    	logger.debug("process input:"+tempStr);
			    	pw.write(tempStr);
			    }
			    pw.flush();
	        } catch (IOException ioe) {
	        	logger.error("read Process InputStream has error");
	        } finally {
	        	try {
	        		pw.close();
				} catch (Exception e) {
				}
	        }
	    }
	}
	
	public static class WriteToStreamOnSign implements Runnable {
	    private OutputStream output; // Process输入流, 外部向process写入数据
	    private StringBuffer data;
	    
	    private WriteToStreamOnSign(OutputStream output, StringBuffer data) {
	        this.output = output;
	        this.data = data;
	    }  
	  
	    public void run() {
	    	PrintWriter pw = null;
	        try {
	            pw = new PrintWriter(output);
	            long current = System.currentTimeMillis();
			    while (true) {
			    	if (System.currentTimeMillis() - current >= 1000) {
			    		logger.info("wait for [Successfully opened log file]");
			    		current = System.currentTimeMillis();
			    	}
			    	if (data.toString().contains("Successfully opened log file")) {
			    		logger.debug("process input:Q");
			    		System.out.println("process input:Q");
				    	pw.write("Q");
				    	break;
			    	}
			    }
			    pw.flush();
	        } catch (Exception ioe) {
	        	logger.error("read Process InputStream has error");
	        } finally {
	        	try {
	        		pw.close();
				} catch (Exception e) {
				}
	        }
	    }
	}
	
	public static class PrintToStream implements Runnable {
	    private InputStream input; // Process 输入流
	    private OutputStream output; // 输出到外部,不要关闭
	  
	    private PrintToStream(InputStream input, OutputStream output) {
	        this.input = input;
	        this.output = output;
	    }  
	  
	    public void run() {
	    	InputStreamReader isr = null;
	    	BufferedReader inReader = null;
	    	PrintWriter pw = null;
	        try {
	        	isr = new InputStreamReader(input);
	            inReader = new BufferedReader(isr);
	            pw = new PrintWriter(output);
	            String tempStr = null;
			    while ((tempStr = inReader.readLine()) != null) {
			    	pw.println(tempStr);
			    }
			    pw.flush();
	        } catch (IOException ioe) {
	        	logger.error("read Process InputStream has error");
	        } finally {
	        	try {
	        		input.close();
				} catch (Exception e) {
				}
	        }
	    }
	}
	
	public static void writeString(String message, OutputStream output) {
        try {
        	DataOutputStream dos = new DataOutputStream(output); // 外部输出流,不要关闭
        	dos.writeUTF(message);
        	dos.flush();
        } catch (IOException e) {
        	logger.info("Connection object writeString method has error", e);
        }
    }
	
	public static void printString(String message, OutputStream output) {
        PrintWriter pw = new PrintWriter(output); // 外部输出流,不要关闭
		pw.println(message);
		pw.flush();
    }
	
}
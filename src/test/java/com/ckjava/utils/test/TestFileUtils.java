package com.ckjava.utils.test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class TestFileUtils {
	public static void main(String[] args) {
		enCodeFile();
		
		deCodeFile();
	}
	
	@Test
	public void testzipFiles() {
		//FileUtils.zipFiles("D:/svn-workspace/", "*", "D:/svn-workspace/svn-workspace.zip");
		//System.out.println("finish");
	}

	private static void deCodeFile() {
		String data = "D:\\BaiduYunDownload\\dagger.txt";
		
		String temp = "";
		try {
			BufferedReader is = new BufferedReader(new FileReader(new File(data)));
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File("D:\\BaiduYunDownload\\dagger.zs.jar")));
			while ((temp = is.readLine()) != null) {
				byte[] str = Base64.decodeBase64(temp.getBytes());
				os.write(str);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}

	public static void enCodeFile() {
		String data = "D:\\BaiduYunDownload\\dagger.jar";
		
		int temp = 0;
		byte[] b = new byte[102400];
		try {
			InputStream is = new FileInputStream(new File(data));
			BufferedWriter os = new BufferedWriter(new FileWriter(new File("D:\\BaiduYunDownload\\dagger.txt")));
			while ((temp = is.read(b, 0, b.length)) != -1) {
				byte[] realdata = new byte[temp];
				System.arraycopy(b, 0, realdata, 0, temp);
				byte[] str = Base64.encodeBase64(realdata);
				os.write(new String(str));
				os.newLine();
			}
			is.close();
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}
}

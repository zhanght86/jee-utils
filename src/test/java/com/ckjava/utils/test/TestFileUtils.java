package com.ckjava.utils.test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.ckjava.utils.FileUtils;

public class TestFileUtils {
	
	@Test
	public void testzipFiles() {
		String compressDir = "D:/git-workspace/"; 
		String zipFile = "D:/git-workspace_zip.zip";
		String[] excludePath = {".svn",".git",".metadata",".recommenders", "target", "bin", ".settings", "classes", "logs"}, excludeFile = {".class"};
		FileUtils.zipFiles(compressDir, "*", excludePath, excludeFile, zipFile);
		//FileUtils.moveFileToDirectory(srcFile, destDir, createDestDir);
		System.out.println("finish");
	}

	@Test
	public void deCodeFile() {
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

	@Test
	public void enCodeFile() {
		String dataFile = "D:/git-workspace_zip.zip";
		String enCodeFile = "D:/git-workspace_zip_encode";
		
		int temp = 0;
		byte[] b = new byte[102400];
		try {
			InputStream is = new FileInputStream(new File(dataFile));
			BufferedWriter os = new BufferedWriter(new FileWriter(new File(enCodeFile)));
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
	
	@Test
	public void zipFile() {
		FileUtils.zipFile(new File("D:/git-workspace_zip_encode"));
		//FileUtils.moveFileToDirectory(srcFile, destDir, createDestDir);
		System.out.println("finish");
	}
	
	@Test
	public void moveFile() {
		String srcFile = "D:/git-workspace_zip_encode_zip.zip";
		try {
			FileUtils.moveFileToDirectory(new File(srcFile), new File("D:/BaiduYunDownload/encode-files"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("finish");
	}
	
}

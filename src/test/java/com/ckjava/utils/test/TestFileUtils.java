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

import com.ckjava.utils.FileUtils;

public class TestFileUtils {
	
	public static void main(String[] args) {
		String path = "D:/git-workspace";
		String desDir = "D:/BaiduYunDownload/encode-files";
		backupDir(path, desDir);
	}
	
	public static void backupDir(String path, String desDir) {
		File zipFile = new File(path + ".zip");
		File encodeFile = new File(path + "_zip_encode");
		
		String[] excludePath = {".svn",".git",".metadata",".recommenders", "target", "bin", ".settings", "classes", "logs"}, excludeFile = {".class"};
		FileUtils.zipFiles(path, "*", excludePath, excludeFile, zipFile);
		
		enCodeFile(zipFile, encodeFile);
		
		File finalZipFile = FileUtils.zipFile(encodeFile);
		
		try {
			File desFile = new File(desDir + File.separator + finalZipFile.getName());
			if (desFile.exists()) {
				desFile.delete();
			}
			FileUtils.moveFileToDirectory(finalZipFile, new File(desDir), true);
			
			if (zipFile.delete()) {
				System.out.println("delete " + zipFile);
			}
			
			if (encodeFile.delete()) {
				System.out.println("delete " + encodeFile);
			}
			
			if (finalZipFile.delete()) {
				System.out.println("delete " + finalZipFile.getAbsolutePath());
			}
			
			System.out.println("finish");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void deCodeFile() {
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

	/**
	 * 将 zip文件通过 base64 处理 
	 * 
	 * @param zipFile
	 * @param encodeFile
	 */
	public static void enCodeFile(File zipFile, File encodeFile) {
		int temp = 0;
		byte[] b = new byte[102400];
		try {
			InputStream is = new FileInputStream(zipFile);
			BufferedWriter os = new BufferedWriter(new FileWriter(encodeFile));
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
	}
	
}

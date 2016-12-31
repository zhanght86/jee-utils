package com.ckjava.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileUtils {
	
	/**
	 * 获取制定目录下的文件列表
	 * @param filepath 目录
	 * @param filelist 文件列表
	 * @param filetype 制定文件类型
	 * @return
	 */
	public static void getFileToList(String filepath, List<File> filelist, String filetype) {
		File[] files = FileUtils.listFiles(filepath);
		if (files == null) {
			return;
		}
		for (File f : files) {
			File file = f.getAbsoluteFile();
			if (file.isFile()) {
				String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
				if (filetype != null && !filetype.equalsIgnoreCase(suffix)) {
					continue;
				}
				filelist.add(file);
			} else {
				getFileToList(file.getAbsolutePath(), filelist, filetype);
			}
		}
	}
	
	/**
	 * 从指定目录获取指定名称的文件
	 * @param filepath 目录
	 * @param fileName 名称
	 * @return
	 */
	public static File getFileFromPath(String filepath, String fileName) {
		File[] files = FileUtils.listFiles(filepath);
		if (files == null) {
			return null;
		}
		for (File file : files) {
			if (file.getName().equals(fileName)) {
				return file;
			}
		}
		return null;
	}
	
	public static void fileCopy(File sourcefile, File destfile) throws Exception {
		if (sourcefile == null) {
			throw new IllegalArgumentException("source file is null");
		}
		if (destfile == null) {
			throw new IllegalArgumentException("destination file is null");
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(sourcefile);
			fos = new FileOutputStream(destfile);
			byte[] b = new byte[1024 * 10 * 10];
			int temp = 0;
			while ((temp = fis.read(b)) != -1) {
				fos.write(b, 0, temp);
			}
			fos.flush();
		} catch (Exception e) {
			throw new Exception("copy file to destination occurrence error", e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e2) {
				throw new Exception("copy file to destination when close stream occurrence error", e2);
			}
		}
	}
	
	public static File[] listFiles(String filepath) {
		if (filepath == null) {
			throw new IllegalArgumentException("the file path is null");
		}
		File file = new File(filepath);
		if (file.isDirectory()) {
			return file.listFiles();
		}
		return null;
	}
	
	public static String readFileContent(File file, String charset) throws Exception {
		if (file == null) {
			throw new IllegalArgumentException("the file object is null");
		} else {
			try {
				return IOUtils.getString(new FileInputStream(file), charset);
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException("the " + file.getName() + " not found");
			} catch (Exception e) {
				throw new Exception("read " + file.getName() + " content occurence error");
			}
		}
	}
	
	public static String readFileContent(File file) throws Exception {
		if (file == null) {
			throw new IllegalArgumentException("the file object is null");
		} else {
			try {
				return IOUtils.getString(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException("the " + file.getName() + " not found");
			} catch (Exception e) {
				throw new Exception("read " + file.getName() + " content occurence error");
			}
		}
	}
	
	/**
	 * @param file 目标文件
	 * @param str 写入文件的内容
	 * @param append 是否追加
	 * @param encoding 内容编码
	 * @throws Exception
	 */
    public static void writeStringToFile(File file, String str, boolean append, String encoding) throws Exception {
    	try {
    		if (!file.getParentFile().exists()) {
    			file.getParentFile().mkdirs();
    		}
    		file.createNewFile();
    		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, append), encoding);
            writer.write(str);
            writer.flush();
            writer.close();
		} catch (Exception e) {
			throw new Exception("write string to file " + file.getName() + " occurrence error", e);
		}
    }

    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                flag = deleteFile(file.getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
		}
        if (!flag) {
            return false;
        }
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    
}

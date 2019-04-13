package com.sljm.svnadmin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FilesUtil {
	public static ArrayList<String> getFileDir(String path) throws Exception {
		ArrayList<String> l = new ArrayList<String>();
	    File dirFile = new File(path);
	    if (dirFile.exists()){
	        File[] files = dirFile.listFiles();
	        if (files != null) {
	            for (File fileChildDir:files) {
	                //输出文件夹名
	                if (fileChildDir.isDirectory()) {
	                	l.add(fileChildDir.getName());
	                }
	            }
	        }
	    }else{
	    	throw new Exception("文件夹不存在");
	    }
	    return l;
	}
	public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }
	public static boolean writeFile(String sourceString,String path) {
		byte[] sourceByte = sourceString.getBytes();
		if (null != sourceByte){
			try {
				File file = new File(path); // 文件路径（路径+文件名）
				if (!file.exists()) { // 文件不存在则创建文件，先创建目录
					File dir = new File(file.getParent());
					dir.mkdirs();
					file.createNewFile();
				}
				FileOutputStream outStream = new FileOutputStream(file); // 文件输出流用于将数据写入文件
				outStream.write(sourceByte);
				outStream.close(); // 关闭文件输出流
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}else {
			return false;
		}
	}

	public static String readFiles(String path) throws IOException {
		StringBuffer sb = new StringBuffer();
		FilesUtil.readToBuffer(sb, path);
		return sb.toString();
	}

	/**
	 * 迭代删除文件夹
	 * 
	 * @param dirPath 文件夹路径
	 */
	public static void deleteDir(String dirPath) {
		File file = new File(dirPath);
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			if (files == null) {
				file.delete();
			} else {
				for (int i = 0; i < files.length; i++) {
					deleteDir(files[i].getAbsolutePath());
				}
				file.delete();
			}
		}
	}
	public static void main(String[] args) throws IOException {
//		StringBuffer sb = new StringBuffer();
//		FilesUtil.readToBuffer(sb,"C:/Users/lisy/Desktop/conf/passwd");
//		System.out.println(sb.toString());
		FilesUtil.writeFile("aadf发斯蒂芬","C:/Users/lisy/Desktop/sdfasdf.txt");
	}
}

package com.sljm.svnadmin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Lisy {
	@Test
	public void dir()
	{
		try {
			List<String> list = getFileDir("E:/workspace/");
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static ArrayList<String> getFileDir(String path) throws Exception {
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
}

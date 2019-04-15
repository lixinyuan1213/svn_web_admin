package com.sljm.svnadmin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class Lisy {
	@Value("${mysvn.path}")
	private String path;
	@Test
	public void dir()
	{
		try {
			List<String> list = getFileDir(path);
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
	@Test
	public void str() {
		 // 要验证的字符串
	    String str = "baike.xsoftlab.net";
	    // 正则表达式规则
	    String regEx = "baike.*";
	    // 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx);
	    // 忽略大小写的写法
	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(str);
	    // 查找字符串中是否有匹配正则表达式的字符/字符串
	    boolean rs = matcher.find();
	    System.out.println(rs);
	}
}

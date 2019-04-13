package com.sljm.svnadmin.controller;
/**
 * 
 *  定时扫描文件夹，更新数据库
 * 
 * **/
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sljm.svnadmin.util.FilesUtil;
@Component
public class Crontab {
	@Value("${mysvn.path}")
	private String svnPath;
	@Scheduled(cron="0 */10 * * * *")
	public void getFiles() throws Exception
	{
		List<String> dirs = FilesUtil.getFileDir(svnPath);
		if(!"".equals(dirs))
		{
			System.out.println(String.join(",", dirs));			
		}
	}
}

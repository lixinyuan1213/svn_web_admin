package com.sljm.svnadmin;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.sljm.svnadmin.service.SvnService;
import com.sljm.svnadmin.model.SvnProject;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SvnServiceTest {
	@Autowired
	private SvnService SvnService;
	@Test
	public void insert()
	{
		SvnProject svn = new SvnProject();
		svn.setName("呵呵2");
		svn.setFile_name("svn2");
		svn.setIntro("添加3");
		svn.setCreator(1);
		svn.setCtime("2019-04-13 12:00:12");
		svn.setMtime("2019-04-13 12:00:12");
		svn.setUsers("lisy=lisy\nwzr=wzr");
		svn.setAuth("wzr=rw\nlisy=rw");
//		SvnProject rs = SvnService.addProject(svn);
//		System.out.println(rs);
	}
	@Test
	public void list() {
		String keyWords = "root";
		SvnProject condition = SvnService.getWhereConditionModelByName(keyWords);
		List<SvnProject> l = SvnService.getList(null);
		List<SvnProject> l2 = SvnService.getList(condition);
		System.out.println(l);
		System.out.println(l2);
	}
	@Test
	public void update() {
//		SvnProject svn = SvnService.getSvnInfo(1);
//		if(svn==null)
//		{
//			System.out.println("没有记录");
//		}else {
//			svn.setAuth("sz=rw");
//			svn.setCreator(2);
//			svn.setCtime("2019-05-01 00:00:00");
//			svn.setFile_name("www");
//			svn.setIntro("测试");
//			svn.setMtime("2019-05-01 00:00:00");
//			svn.setName("测试");
//			svn.setUsers("sz=sz");
////			int rs = SvnService.update(request,svn);
////			System.out.println(rs);
//		}
	}
}

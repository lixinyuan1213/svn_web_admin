package com.sljm.svnadmin.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
/**
 * svn相关控制器
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.sljm.svnadmin.exception.BusinessException;
import com.sljm.svnadmin.model.ResultMsg;
import com.sljm.svnadmin.model.SvnModel;
import com.sljm.svnadmin.model.SvnProject;
import com.sljm.svnadmin.service.SvnService;
import com.sljm.svnadmin.service.UserService;

@Controller
public class Index {
	@Autowired
	private SvnService SvnService;
	@Autowired
	private SvnModel SvnModel;
	@Autowired
	private UserService UserService;
	@Value("${mysvn.delPwd}")
	private String delPwd;
	@GetMapping("/")
	public String index(HttpServletRequest request,String keyWords,@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "20")Integer pageSize) 
	{
		SvnProject condition = null;
		if(!StringUtil.isEmpty(keyWords)) {
			keyWords = keyWords.trim();
			condition =  SvnService.getWhereConditionModelByName(keyWords);
		}else {
			keyWords = "";
		}
		PageHelper.startPage(pageNum,pageSize);
		List<SvnProject> list = SvnService.getList(condition);
		PageInfo<SvnProject> pageInfo = new PageInfo<SvnProject>(list);
		//查询到的数据
		request.setAttribute("svnList", pageInfo.getList());
		//分页相关
		request.setAttribute("page", pageInfo);
		//搜索关键字
		request.setAttribute("keywords",keyWords);
		//本地svn链接
		request.setAttribute("localHost",SvnModel.getLocalHost());
		//远程的svn链接
		request.setAttribute("host",SvnModel.getHost());
		//所有用户
		HashMap<Long,String> users = UserService.getUserMap();
		request.setAttribute("users",users);
		return "index";
	}
	@GetMapping("/add")
	public String addPage()
	{
		return "add";
	}
	@PostMapping("/doAdd")
	@ResponseBody
	public ResultMsg doAdd(HttpServletRequest request,SvnProject svn)
	{
		if(StringUtil.isEmpty(svn.getIntro()))
		{
			return ResultMsg.error("项目说明不能为空");
		}
		try {
			SvnProject rs = SvnService.addProject(request,svn);
			if(rs!=null)
			{
				return ResultMsg.success("添加成功,请及时配置好项目",rs);
			}else {
				return ResultMsg.error("失败");
			}
		} catch (BusinessException e) {
			return ResultMsg.error(e.getMessage());
		}catch (Exception e) {
			return ResultMsg.error("系统错误");
		}
	}
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request,Integer id)
	{
		if(id==null||id<=0)
		{
			return ResultMsg.pageErrorMsg(request, "参数错误");
		}
		SvnProject info;
		try {
			info = SvnService.getSvnInfo(id);
		} catch (IOException e) {
			e.printStackTrace();
			return ResultMsg.pageErrorMsg(request, "查询失败");
		}
		if(info==null)
		{
			return ResultMsg.pageErrorMsg(request, "没有记录");
		}
		// 搜索关键字
		request.setAttribute("info",info);
		// 本地svn链接
		request.setAttribute("localHost", SvnModel.getLocalHost());
		// 远程的svn链接
		request.setAttribute("host", SvnModel.getHost());
		return "edit";
	}
	@PostMapping("/doEdit")
	@ResponseBody
	public ResultMsg doEdit(HttpServletRequest request,SvnProject SvnProject)
	{
		try {
			int rs = SvnService.update(request,SvnProject);
			if(rs>0)
			{
				return ResultMsg.success("更新成功");
			}else {
				return ResultMsg.error("更新失败");
			}
		}catch (BusinessException e) {
			e.printStackTrace();
			return ResultMsg.error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return ResultMsg.error("系统错误");
		}
	}
	@PostMapping("/project/del")
	@ResponseBody
	public ResultMsg del(HttpServletRequest request,Integer id,String pass,String fileName)
	{
		if(id<=0) {
			return ResultMsg.error("参数错误");
		}
		if(StringUtil.isEmpty(pass)||StringUtil.isEmpty(fileName)) {
			return ResultMsg.error("参数错误");
		}
		if(!pass.equals(delPwd)) {
			return ResultMsg.error("删除密码错误");
		}
		try {
			int rs = SvnService.delSvn(request,id,fileName);
			if(rs>0) {
				return ResultMsg.success("操作成功");
			}else {
				return ResultMsg.error("操作失败");
			}
		} catch (BusinessException e) {
			return ResultMsg.error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return ResultMsg.error("操作失败");
		}
	}
}

package com.sljm.svnadmin.controller;
/**
 * 用户相关控制器
 */
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sljm.svnadmin.exception.BusinessException;
import com.sljm.svnadmin.model.ResultMsg;
import com.sljm.svnadmin.model.UserModel;
import com.sljm.svnadmin.service.UserService;

@Controller
@RequestMapping("/user")
public class User {
	@Autowired
	private UserService UserService;
	@RequestMapping("/list")
	public String list(HttpServletRequest request,UserModel u,@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "20")Integer pageSize) 
	{
		try {
			if(!isRole(request)) {
				return ResultMsg.pageErrorMsg(request, "您没有管理权限");
			}
			PageHelper.startPage(pageNum,pageSize);
			List<UserModel> list = UserService.getUsers(u);
			PageInfo<UserModel> pageInfo = new PageInfo<UserModel>(list);
			//查询到的数据
			request.setAttribute("users", pageInfo.getList());
			//分页相关
			request.setAttribute("page", pageInfo);
			//搜索关键字
			request.setAttribute("name", u.getName());
			//搜索条件
			request.setAttribute("condition", this.getParam(u));
			return "users";
		}catch (BusinessException e) {
			e.printStackTrace();
			return "users";
		}catch (Exception e) {
			e.printStackTrace();
			return "users";
		}
	}
	//获取查询参数
	private String getParam(UserModel u)
	{
		String name = u.getName();
		if(name==null)
		{
			name = "";
		}
		return "id="+u.getId()+"&name="+name;
	}
	//编辑用户信息
	@RequestMapping("/edit")
	public String editUser(HttpServletRequest request,Integer userId)
	{
		if(!isRole(request)) {
			return "login";
		}
		UserModel u = UserService.getUserInfo(userId);
		if(u==null)
		{
			return ResultMsg.pageErrorMsg(request, "查询的数据不存在");
		}
		request.setAttribute("userInfo", u);
		return "userinfo";
	}

	// 编辑用户信息
	@ResponseBody
	@RequestMapping(value="/editUser",method = RequestMethod.POST)
	public ResultMsg editUser(HttpServletRequest request, UserModel u) {
		if(!isRole(request)) {
			return ResultMsg.success("您没有管理权限");
		}
		if (u == null) {
			return ResultMsg.error("参数错误");
		}
		int flag = UserService.editUser(u);
		if(flag>0){
			return ResultMsg.success("操作成功");
		}else {
			return ResultMsg.error("操作失败");
		}
	}

	// 编辑用户信息
	@ResponseBody
	@RequestMapping(value="/delUser",method = RequestMethod.POST)
	public ResultMsg delUser(HttpServletRequest request,Integer id) {
		if(!isRole(request)) {
			return ResultMsg.success("您没有管理权限");
		}
		if(id==null) {
			return ResultMsg.success("参数错误");
		}
		if(id<0||id==1) {
			return ResultMsg.success("不允许操作的数据");
		}
		int flag = UserService.deleteUser(id);
		if (flag > 0) {
			return ResultMsg.success("操作成功");
		} else {
			return ResultMsg.error("操作失败");
		}
	}

	// 添加用户页面
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request) {
		if(!isRole(request)) {
			return "login";
		}
		return "adduser";
	}
	// 执行用户添加
	@ResponseBody
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public ResultMsg add(HttpServletRequest request,UserModel u) {
		if(!isRole(request)) {
			return ResultMsg.error("您没有管理权限");
		}
		try {
			UserModel nu = UserService.addUser(u);
			if(nu!=null&&nu.getId()>0) {
				return ResultMsg.success("成功成功");
			}else {
				return ResultMsg.error("添加失败");
			}
		}catch (BusinessException be) {
			return ResultMsg.error(be.getMessage());
		}catch (Exception e) {
			return ResultMsg.error("服务器处理异常");
		}
	}
	//判断当前用户是否有权限管理
	private boolean isRole(HttpServletRequest request) {
		UserModel loginUser = UserService.getLoginUser(request.getSession());
		if(loginUser==null) {
			return false;
		}
		if(loginUser.getRole()!=1) {
			return false;
		}
		return true;
	}
}

package com.sljm.svnadmin.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sljm.svnadmin.exception.BusinessException;
import com.sljm.svnadmin.model.ResultMsg;
import com.sljm.svnadmin.model.UserModel;
import com.sljm.svnadmin.service.UserService;

@Controller
public class Login {
	@Autowired
	private UserService UserService;
	@GetMapping("/login")
	public String login(HttpSession session){
		//判断是否登录，登录跳转到首页，没有登录跳转到登录页面
		int LoginFlag = UserService.isLogin(session);
		if(LoginFlag==0) {
			return "login";
		}else {
			return "forward:/index";
		}
	}
	@ResponseBody
	@PostMapping(value="/doLogin")
	public ResultMsg doLogin(UserModel u,HttpSession session)
	{
		try {
			int loginFlag = UserService.login(u,session);
			if(loginFlag==1) {
				return ResultMsg.success("登录成功");
			}else {
				return ResultMsg.error("登录失败");
			}
		}catch (BusinessException e) {
			return ResultMsg.error(e.getMessage());
		}catch (Exception e) {
			return ResultMsg.error("服务器处理异常");
		}
	}
	@GetMapping("/logOut")
	public String logOut(HttpSession session){
		session.removeAttribute("user");
		return "login";
	}
}

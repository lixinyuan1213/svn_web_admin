package com.sljm.svnadmin.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.StringUtil;
import com.sljm.svnadmin.exception.BusinessException;
import com.sljm.svnadmin.mapper.UserMapper;
import com.sljm.svnadmin.model.UserModel;
import com.sljm.svnadmin.util.MD5Util;
import com.sljm.svnadmin.util.TimeUtil;

@Service
public class UserService {
	@Autowired
	private  UserMapper userMapper;
	@Autowired
	private SvnService svnService;
	public UserModel addUser(UserModel model){	
		/**
		 * 验证用户名不能重复
		 */
		String userName = model.getName().trim();
		UserModel dbUser = getUserByName(userName);
		if(dbUser!=null)
		{
			throw new BusinessException("用户名不能重复");
		}
		String nowTime = TimeUtil.nowTime();
		model.setCtime(nowTime);
		model.setMtime(nowTime);
		/**
		 * 加密密码
		 */
		String pwd = model.getPassword();
		if(pwd.equals(""))
		{
			throw new BusinessException("密码不能为空");
		}
		model.setPassword(MD5Util.generate(pwd));
		model.setName(userName);
		int rs = userMapper.insertSelective(model);
		if(rs>0)
		{
			return model;
		}else {
			throw new BusinessException("添加用户失败");
		}
    }
	/*
	 * 用户登录
	 */
	public int login(UserModel u,HttpSession session) 
	{
		if(u==null)
		{
			throw new BusinessException("请确认提交的数据是否正确");
		}
		String pwd = u.getPassword();
		String name = u.getName();
		if(pwd.equals("")||name.equals(""))
		{
			throw new BusinessException("用户名和密码都不能为空");
		}
		UserModel dbUserModel = userMapper.selectByName(name);
		if(dbUserModel==null)
		{
			throw new BusinessException("账号或密码错误");
		}
		if(dbUserModel.getStatus()!=1)
		{
			throw new BusinessException("已经被禁用");
		}
		if((MD5Util.verify(pwd,dbUserModel.getPassword())))
		{
			session.setAttribute("user",dbUserModel);
			return 1;
		}else{
			throw new BusinessException("账号或密码错误");
		}
	}
	/**
	 * 用户是否登录
	 * @return
	 */
	public int isLogin(HttpSession session)
	{
		UserModel u = getLoginUser(session);
		if(u==null) 
		{
			return 0;
		}else {
			return 1;
		}
	}
	/**
	 * 获取用户信息
	 * @param uid
	 * @return
	 */
	public UserModel getUserInfo(Integer uid)
	{
		return userMapper.selectByPrimaryKey(uid);
	}
	/**
	 * 获取用户信息
	 * @param uid
	 * @return
	 */
	public UserModel getUserByName(String name)
	{
		return userMapper.selectByName(name);
	}
	/**
	 * 获取用户列表
	 * @return
	 */
	public List<UserModel> getUsers(UserModel u)
	{
		return userMapper.getUserList(u);
	}
	public int editUser(UserModel u)
	{
		if(!"".equals(u.getPassword())) {
			u.setPassword(MD5Util.generate(u.getPassword()));
		}
		return userMapper.updateByPrimaryKeySelective(u);
	}
	public int deleteUser(int userId)
	{
		UserModel u = getUserInfo(userId);
		if(u==null)
		{
			return 0;
		}
		String userName = u.getName();
		userName = userName.trim();
		int rs = userMapper.deleteByPrimaryKey(userId);
		if(rs>0)
		{
			//删除配置文件总所有的该用户名
			svnService.removeNameOnConfig(userName);
			return rs;
		}else {
			return 0;
		}
	}
	/**
	 * 获取登录人信息
	 * @param session
	 * @return
	 */
	public UserModel getLoginUser(HttpSession session) {
		UserModel u = (UserModel) session.getAttribute("user");
		return u;
	}
	/**
	 * 获取用户信息map（id为键，name为值）
	 * @return
	 */
	public HashMap<Long,String> getUserMap() {
		HashMap<Long, String> users = new HashMap<Long, String>();
		List<UserModel> list = getUsers(null);
		if(list!=null) {
			for(int i=0;i<list.size();i++) {
				UserModel temp = list.get(i);
				Long key = temp.getId();
				String val = temp.getReal_name();
				users.put(key, val);
			}
		}
		return users;
	}
	public int editUserInfo(HttpServletRequest request,UserModel u) {
		UserModel loginInfo = getLoginUser(request.getSession());
		if(loginInfo==null)
		{
			throw new BusinessException("请先登录");
		}
		if(!StringUtil.isEmpty(u.getName()))
		{
			throw new BusinessException("不能修改登录名称");
		}
		//修改密码
		if(!StringUtil.isEmpty(u.getPassword())){
			u.setPassword(MD5Util.generate(u.getPassword()));
		}else {
			u.setPassword(null);
		}
		u.setMtime(TimeUtil.nowTime());
		u.setId(loginInfo.getId());
		int rs = userMapper.updateByPrimaryKeySelective(u);
		return rs;
	}
}

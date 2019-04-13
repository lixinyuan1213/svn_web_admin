package com.sljm.svnadmin.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sljm.svnadmin.model.UserModel;
@Component
public interface UserMapper {
	//根据id获取数据
	UserModel selectByPrimaryKey(int userId);
	//根据用户名获取数据
	UserModel selectByName(String name);
	//根据id删除数据
	int deleteByPrimaryKey(int userId);
	// 插入数据
	int insertSelective(UserModel model);
	//更新数据
	int updateByPrimaryKeySelective(UserModel model);
	//用户列表
	List<UserModel> getUserList(UserModel model);
}

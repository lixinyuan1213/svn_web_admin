package com.sljm.svnadmin.mapper;
import java.util.List;

import org.springframework.stereotype.Component;
import com.sljm.svnadmin.model.SvnProject;

@Component
public interface SvnProjectMapper {
	// 根据id获取数据
	SvnProject selectByPrimaryKey(int Id);

	// 根据用户名获取数据
	SvnProject selectByName(String name);
	// 根据用户名获取数据
	SvnProject selectByNameNotId(String name,int id);

	// 根据id删除数据
	int deleteByPrimaryKey(int Id);

	// 插入数据
	int insertSelective(SvnProject model);

	// 更新数据
	int updateByPrimaryKeySelective(SvnProject model);

	// 用户列表
	List<SvnProject> getSvnList(SvnProject model);
}

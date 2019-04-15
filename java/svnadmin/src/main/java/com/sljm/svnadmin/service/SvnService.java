package com.sljm.svnadmin.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.StringUtil;
import com.sljm.svnadmin.exception.BusinessException;
import com.sljm.svnadmin.mapper.SvnProjectMapper;
import com.sljm.svnadmin.model.SvnModel;
import com.sljm.svnadmin.model.SvnProject;
import com.sljm.svnadmin.model.UserModel;
import com.sljm.svnadmin.util.FilesUtil;
import com.sljm.svnadmin.util.PatternUtil;
import com.sljm.svnadmin.util.Shell;
import com.sljm.svnadmin.util.TimeUtil;;
@Service
public class SvnService {
	@Autowired
	private SvnProjectMapper SvnProjectMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private SvnModel svnModel;
	/**
	 * 创建svn
	 * @param model
	 * @return
	 */
	public SvnProject addProject(HttpServletRequest request,SvnProject model)
	{
		//用户提交的项目名称
		String subSvnNameStr = model.getFile_name();
		//只允许数字和字母
		subSvnNameStr = PatternUtil.OnlyCharAndNum(subSvnNameStr);
		//过滤特殊字符
		subSvnNameStr = PatternUtil.StringFilter(subSvnNameStr);
		if(StringUtil.isEmpty(subSvnNameStr))
		{
			throw new BusinessException("请填写svn的英文名称");
		}
		//svn目录下面是否已经有了目录
		boolean containsFlag = isContains(svnModel.getSvnPath(),subSvnNameStr);
		if(containsFlag)
		{
			throw new BusinessException("svn项目已经存在，请更换");
		}
		//svn路径
		String svnNameFinal = svnModel.getSvnPath()+subSvnNameStr;
		try {
			Shell.doShell("svnadmin create "+svnNameFinal);
		} catch (IOException e) {
			throw new BusinessException("svn项目创建失败");
		}
		/**
		 *  修改svn的配置文件
		 */
		//svnserve.conf
		String svnServeConfig = "[general]\nanon-access = none\nauth-access = write\npassword-db = passwd\nauthz-db = authz\n[sasl]\n";
		FilesUtil.writeFile(svnServeConfig, svnNameFinal + "/conf/svnserve.conf");
		//authz
		String svnAuthConfig = "[/]\n";
		FilesUtil.writeFile(svnAuthConfig, svnNameFinal + "/conf/authz");
		//passwd
		String passwdConfig = "[users]\n";
		FilesUtil.writeFile(passwdConfig,svnNameFinal+"/conf/passwd");
		//处理创建人id
		UserModel loginUser = userService.getLoginUser(request.getSession());
		if(loginUser==null)
		{
			throw new BusinessException("请先登录");
		}
		model.setCreator(loginUser.getId());
		//处理添加时间等
		String nowTime = TimeUtil.nowTime();
		model.setCtime(nowTime);
		model.setMtime(nowTime);
		model.setFile_name(subSvnNameStr);
		int rs = SvnProjectMapper.insertSelective(model);
		if(rs>0)
		{
			return model;
		}else {
			return null;
		}
	}
	/**
	 * 根据id查询svn详情
	 * @param id 记录id
	 * @return
	 * @throws IOException 
	 */
	public SvnProject getSvnInfo(int id) throws IOException
	{
		SvnProject dbInfo = SvnProjectMapper.selectByPrimaryKey(id);
		if(dbInfo==null)
		{
			return dbInfo;
		}
		//权限配置文件
		String auth = FilesUtil.readFiles(svnModel.getSvnPath()+dbInfo.getFile_name()+"/conf/authz");
		//用户配置文件
		String passwd = FilesUtil.readFiles(svnModel.getSvnPath()+dbInfo.getFile_name()+"/conf/passwd");
		dbInfo.setAuth(auth);
		dbInfo.setUsers(passwd);
		//用户配置文件
		return dbInfo;
	}
	/**
	 * 查询列表
	 * @param model svn模型
	 * @return
	 */
	public List<SvnProject> getList(SvnProject model)
	{
		return SvnProjectMapper.getSvnList(model);
	}
	/**
	 * 根据id删除svn记录
	 * @param id
	 * @return
	 */
	public int delSvn(HttpServletRequest request,int id,String fileName)
	{
		SvnProject dbInfo = SvnProjectMapper.selectByPrimaryKey(id);
		if(dbInfo==null) {
			throw new BusinessException("记录不存在");
		}
		UserModel u = userService.getLoginUser(request.getSession());
		//只有管理员和创建者可删除
		if(u.getRole()!=1) {
			if(u.getId()!=dbInfo.getCreator()) {
				throw new BusinessException("没有删除权限");
			}
		}
		int rs = SvnProjectMapper.deleteByPrimaryKey(id);
		if(rs>0) {
			FilesUtil.deleteDir(svnModel.getSvnPath()+fileName);
			return rs;
		}else {
			return 0;
		}
	}
	/**
	 * 根据关键字构造查询条件
	 * @param name
	 * @return
	 */
	public SvnProject getWhereConditionModelByName(String keyWord)
	{
		if(StringUtil.isEmpty(keyWord))
		{
			return null;
		}else {
			SvnProject svn = new SvnProject();
			svn.setName(keyWord);
			svn.setFile_name(keyWord);
			svn.setIntro(keyWord);
			svn.setUsers(keyWord);
			svn.setAuth(keyWord);
			UserModel userModel = userService.getUserByName(keyWord);
			if(userModel!=null)
			{
				svn.setCreator(userModel.getId());
			}
			return svn;
		}
	}
	public int update(HttpServletRequest request,SvnProject model)
	{
		//svn目录名称
		String fileName = model.getFile_name();
		if(!StringUtil.isEmpty(fileName))
		{
			//不能修改svn目录下的名称
			throw new BusinessException("不能修改svn英文名");
		}
		//id
		long id = model.getId();
		if(id<=0)
		{
			throw new BusinessException("参数错误");
		}
		int primaryKey = (int)id;
		SvnProject dbInfo = SvnProjectMapper.selectByPrimaryKey(primaryKey);
		if(dbInfo==null)
		{
			throw new BusinessException("更新的记录不存在");
		}
		
		UserModel u = userService.getLoginUser(request.getSession());
		if(u==null)
		{
			throw new BusinessException("请先登录");
		}
		//只有管理员或者是项目创建人可以修改
		if(u.getRole()!=1) {
			if(u.getId()!=dbInfo.getCreator()) 
			{
				throw new BusinessException("您没有修改权限");
			}
		}
		//设置日期格式，保存更新时间
		String nowTime = TimeUtil.nowTime();
		model.setMtime(nowTime);
		
		//svn路径
		String svnNameFinal = svnModel.getSvnPath() + dbInfo.getFile_name();
		//修改authz配置
		FilesUtil.writeFile(model.getAuth(), svnNameFinal + "/conf/authz");
		//修改passwd配置
		FilesUtil.writeFile(model.getUsers(), svnNameFinal + "/conf/passwd");
		return SvnProjectMapper.updateByPrimaryKeySelective(model);
	}
	//判断某路径下是否包含某个文件夹,包含返回true，不包含返回false
	private boolean isContains(String path,String dirName)
	{
		List<String> svnList = null;
		try {
			svnList = FilesUtil.getFileDir(path);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return svnList.contains(dirName);
	}
	/**
	  *   删除svn配置文件中的用户名
	 * @param userName 用户名
	 * @return void
	 */
	public void removeNameOnConfig(String userName)
	{
		//查询所有项目
		List<SvnProject> list = getList(null);
		//svn路径
		String svnPath = svnModel.getSvnPath();
		if(list!=null) {
			//删除svn配置文件中的用户名
			for(int i=0;i<list.size();i++)
			{
				SvnProject item = list.get(i);
				String dbAuth = item.getAuth();
				//如果配置文件中包含某个用户名，剔除掉
				if(PatternUtil.svnAuthPattern(dbAuth,userName)) {
					String authStr = PatternUtil.getNonBlankStr(dbAuth,userName);
					FilesUtil.writeFile(authStr, svnPath+item.getFile_name()+ "/conf/authz");
				}
			}
		}
	}
}

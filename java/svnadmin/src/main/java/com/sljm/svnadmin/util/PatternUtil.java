package com.sljm.svnadmin.util;
/**
 * 字符串过滤和替换
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternUtil {
	/**
	 * 去掉特殊字符
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String str) throws PatternSyntaxException {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll("").trim().replace(" ","");
	}
	/**
	 *  只允许字母和数字
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String OnlyCharAndNum(String str) throws PatternSyntaxException {
		String regEx = "[^a-zA-Z0-9]"; 
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll("").trim().replace(" ","");
	}
	/**
	 *  去掉svn配置文件中权限
	 * @param str
	 * @param name
	 * @return
	 */
	public static String getNonBlankStr(String str,String name) {
		if (str != null && !"".equals(str)) {
			String replaceStr = name+"=rw\n|"+name+"=rw|"+name+"=w\n|"+name+"=w|"+name+"=r\n|"+name+"=r";
			Pattern pattern = Pattern.compile(replaceStr); 
			Matcher matcher = pattern.matcher(str);
			String result = matcher.replaceAll("");
			return result;
		} else {
			return str;
		}
	}
	/**
	 *  是否包含
	 * @param str         字符串
	 * @param expression  正则表达式
	 * @return
	 */
	public static boolean isPattern(String str,String expression)
	{
		Pattern p = Pattern.compile(expression); // 正则表达式 
		Matcher m = p.matcher(str); // 操作的字符串 
		boolean b = m.matches(); //返回是否匹配的结果 
		return b;
	}
	/**
	 * svn权限配置中是否包含某个名字
	 * @param str         字符串
	 * @param expression  正则表达式
	 * @return
	 */
	public static boolean svnAuthPattern(String str,String name)
	{
		String[] array = str.split(name);
		if((array.length-1)>0)
		{
			return true;
		}else {
			return false;
		}
	}
}

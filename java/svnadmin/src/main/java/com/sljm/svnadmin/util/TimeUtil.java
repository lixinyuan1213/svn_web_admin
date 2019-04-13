package com.sljm.svnadmin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
	public static String nowTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return df.format(new Date());
	}
}

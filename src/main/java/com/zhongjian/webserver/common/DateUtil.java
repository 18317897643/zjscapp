package com.zhongjian.webserver.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static boolean isLatestWeek(Date addtime, Date now) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(now);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为1天前
		Date before1days = calendar.getTime(); // 得到1天前的时间
		if (before1days.getTime() < addtime.getTime()) {
			return true;
		} else {
			return false;
		}
	}
	public static String DateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}
}

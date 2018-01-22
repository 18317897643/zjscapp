package com.zhongjian.webserver.common;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static boolean isLatestWeek(Date addtime, Date now) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(now);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -7); // 设置为7天前
		Date before7days = calendar.getTime(); // 得到7天前的时间
		if (before7days.getTime() < addtime.getTime()) {
			return true;
		} else {
			return false;
		}
	}
}

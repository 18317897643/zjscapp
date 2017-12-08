package com.zhongjian.webserver.beanconfiguration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zhongjian.webserver.common.ExpiryMap;



/**
 * releans verify code bean
 * @author chen_di
 *
 */
@Component
public class RealeasVerifyCode {
	@Autowired
	private ExpiryMap<String, String> expiryMap;
	
	@Scheduled(cron = "0 0 6,12,23 * * ?") //  每天上午六点，中午十二点和晚上十一点各触发一次
	public void run() {
		expiryMap.release();
	}
}
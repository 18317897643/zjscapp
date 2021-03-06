package com.zhongjian.webserver.component;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zhongjian.webserver.common.ExpiryMap;
import com.zhongjian.webserver.service.SignInService;



/**
 * releans verify code bean
 * @author chen_di
 *
 */
@Component
public class RealeasBean {
	
	@Autowired  
	private Map<String, ExpiryMap<String, String>> map;  
	
	@Autowired
	private SignInService signInService;
	
//	@Scheduled(cron = "0 0/1 * * * ?")
	@Scheduled(cron = "0 0 6,12,23 * * ?") //  每天上午六点，中午十二点和晚上十一点各触发一次
	public void realeasVerifyCode() {
		  for (ExpiryMap<String, String> v : map.values()) {
			   v.release();
			  }
	}
	
//	@Scheduled(cron = "0 0/1 * * * ?")
	@Scheduled(cron = "0 15 3 1 * ?")
	public void realeasDatedSign() {
		signInService.deleteDatedSignInData();
		signInService.deleteDatedSignAwardData();
	}
}
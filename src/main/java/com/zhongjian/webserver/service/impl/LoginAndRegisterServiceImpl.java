package com.zhongjian.webserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.zhongjian.webserver.beanconfiguration.ExpiryMap;
import com.zhongjian.webserver.common.SendSmsUtil;
import com.zhongjian.webserver.service.LoginAndRegisterService;

@Service
public class LoginAndRegisterServiceImpl implements LoginAndRegisterService {
	
	@Autowired
	ExpiryMap<String, String> expiryMap;
	

	@Override
	public void sendRegisterVerifyCode(String phoneNum) throws ApiException{
		//create VerifyCode
		String captcha = SendSmsUtil.randomCaptcha(4);
		//store the VerifyCode 		
		expiryMap.put(phoneNum, captcha);
		//send the sms
	    SendSmsUtil.sendCaptcha(phoneNum, captcha);
	}

}

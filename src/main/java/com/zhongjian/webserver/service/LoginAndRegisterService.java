package com.zhongjian.webserver.service;

import com.taobao.api.ApiException;

public interface LoginAndRegisterService {

	void sendRegisterVerifyCode(String phoneNum) throws ApiException;
	
	
}

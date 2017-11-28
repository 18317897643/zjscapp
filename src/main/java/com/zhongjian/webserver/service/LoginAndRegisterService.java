package com.zhongjian.webserver.service;

import com.taobao.api.ApiException;

public interface LoginAndRegisterService {
	void sendRegisterVerifyCode(String phoneNum) throws ApiException;
	
	boolean checkUserExists(String phoneNum);
	
	boolean checkVerifyCode(String phoneNum,String code);
	
	void registerUser(String phoneNum,String password,Integer inviteCode);
	
	boolean checkUserNameAndPassword(String phoneNum,String password);
	
	boolean modifyPassword(String phoneNum,String password);
}

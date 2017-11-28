package com.zhongjian.webserver.service;


public interface LoginAndRegisterService {
	void sendRegisterVerifyCode(String phoneNum) throws Exception;
	
	boolean checkUserExists(String phoneNum);
	
	boolean checkVerifyCode(String phoneNum,String code);
	
	void registerUser(String phoneNum,String password,Integer inviteCode);
	
	boolean checkUserNameAndPassword(String phoneNum,String password);
	
	boolean modifyPassword(String phoneNum,String password);
}

package com.zhongjian.webserver.service;

import com.zhongjian.webserver.pojo.User;

public interface LoginAndRegisterService {
	void sendRegisterVerifyCode(String phoneNum) throws Exception;
	
	boolean checkUserExists(String phoneNum);
	
	boolean checkVerifyCode(String phoneNum,String code);
	
	void registerUser(String phoneNum,String password,Integer inviteCode);
	
	boolean checkUserNameAndPassword(String phoneNum,String password);
	
	boolean modifyPassword(String phoneNum,String password);
	
	Integer getUserIdByUserName(String userName);
	
	Integer updateUser(User user);

	boolean InviteCodeIsExists(Integer inviteCode);
	
	boolean checkUserNameAndPayPassword(String phoneNum,String password);
	
	boolean userIsFreeze(String userName);
	
	boolean userFundsIsFreeze(String userName);
	
	boolean userNewExclusiveIsDraw (String userName);
	
	Integer drawNewExclusive(String userName);
	
	boolean checkUserIdExits(Integer id);
}

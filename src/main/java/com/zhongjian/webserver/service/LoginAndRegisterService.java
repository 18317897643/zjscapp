package com.zhongjian.webserver.service;

import java.math.BigDecimal;
import java.util.Map;

import com.zhongjian.webserver.pojo.User;

public interface LoginAndRegisterService {
	void sendRegisterVerifyCode(String phoneNum) throws Exception;
	
	boolean checkUserExists(String phoneNum);
	
	boolean checkVerifyCode(String phoneNum,String code);
	
	void registerUser(String phoneNum,String password,Integer inviteCode);
	
	boolean checkUserNameAndPassword(String phoneNum,String password);
	
	boolean modifyPassword(String phoneNum,String password);
	
	Integer getUserIdByUserName(String userName);
	
	String getUserNameByUserId(Integer userId);
	
	Integer updateUser(User user);

	boolean InviteCodeIsExists(Integer inviteCode);
	
	boolean checkUserNameAndPayPassword(String phoneNum,String password);
	
	boolean userIsFreeze(String userName);
	
	boolean userFundsIsFreeze(String userName);
	
	boolean userNewExclusiveIsDraw (Integer userId);
	
	Integer drawNewExclusive(Integer userId);
	
	boolean checkUserIdExits(Integer id);
	
	Map<String, Object> getUserInfoBySysID(Integer sysID);
	
	void sendCouponByInviteCode(BigDecimal coupon,Integer inviteCode);
	
	void sendCouponByUserId (BigDecimal coupon,Integer userId);
}

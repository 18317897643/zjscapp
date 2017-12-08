package com.zhongjian.webserver.service.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.common.ExpiryMap;
import com.zhongjian.webserver.common.SendSmsUtil;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.pojo.User;
import com.zhongjian.webserver.service.LoginAndRegisterService;

@Service
public class LoginAndRegisterServiceImpl implements LoginAndRegisterService {
	
	@Autowired
	private ExpiryMap<String, String> expiryMap;
	
	@Autowired
	private UserMapper userMapper;
	
	private AtomicInteger sysID = null;
	@Override
	public void sendRegisterVerifyCode(String phoneNum) throws Exception{
		//create VerifyCode
		String captcha = SendSmsUtil.randomCaptcha(4);
		//send the sms
	    SendSmsUtil.sendCaptcha(phoneNum, captcha);
		//store the VerifyCode 		
		expiryMap.put(phoneNum, captcha);
	}
	@Override
	public boolean checkVerifyCode(String phoneNum, String code) {
		String verifyCode = expiryMap.get(phoneNum);
		if (code.equals(verifyCode)) {
			return true;
		}
		else {
			return false;
		}
	}
	@Override
	public void registerUser(String phoneNum, String password, Integer inviteCode) {
		if (sysID == null) {
			synchronized(this){
				if (sysID == null) {
					sysID = new AtomicInteger(userMapper.selectUserMaxSysID());
				}
			}
		}
		Integer currentThreadSysID = sysID.incrementAndGet();
		User user = new User();
		user.setUsername(phoneNum);
		user.setPassword(password);
		user.setPhone(phoneNum);
		user.setBeinvitecode(inviteCode);
		user.setInvitecode(currentThreadSysID);
		user.setHeadphoto("/upload/pics/nohead.png");
		user.setSysid(currentThreadSysID);
		userMapper.insertSelective(user);
	}

	@Override
	public boolean checkUserExists(String phoneNum) {
		Integer integer = userMapper.checkUserNameExists(phoneNum);
		if (integer == null) {
			//不存在,该用户可以注册
			return true;
		}
		return false;
	}
	@Override
	public boolean checkUserNameAndPassword(String phoneNum, String password) {
		Integer integer = userMapper.checkUserNameAndPassword(phoneNum, password);
		if (integer == null) {
			//不存在改用户
			return false;
		}
		return true;
		
	}
	@Override
	public boolean modifyPassword(String phoneNum, String password) {
		if (userMapper.updatePassword(phoneNum,password) == 1) {
			return true;
		}
		return false;
	}
	@Override
	public Integer getUserIdByUserName(String userName) {
		return userMapper.getUserIdByUserName(userName);
	}
	
}
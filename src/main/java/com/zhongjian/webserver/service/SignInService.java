package com.zhongjian.webserver.service;

import java.text.ParseException;
import java.util.HashMap;

public interface SignInService {
	//处理签到核心逻辑
	boolean Signing(Integer UserId) throws ParseException;
	
	//标记已奖励的项
	boolean markAreadyAward(Integer UserId,String awardType);
	
	//签到页面数据加载
	HashMap<String, Object> initSignData(Integer UserId);
	
	void deleteDatedSignInData();
	
	void deleteDatedSignAwardData();
}

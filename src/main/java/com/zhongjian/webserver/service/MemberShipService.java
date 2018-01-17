package com.zhongjian.webserver.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MemberShipService {

	// 生成viporder
	HashMap<String, Object> createVOrder(Integer lev, BigDecimal needPay, Integer UserId, Integer type);

	// 同步支付viporder
	void syncHandleVipOrder(Integer UserId, String orderNo);

	// 生成viporder
	String createCOrder(Integer userId, BigDecimal money);

	//获取红黄蓝粉
	Map<String, Integer> getRYBFans(Integer userId);
	
	//获取红黄蓝粉
	List<Map<String, Object>> getRYBFansDetails(Integer userId,String type);
}

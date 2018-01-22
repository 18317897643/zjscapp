package com.zhongjian.webserver.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.zhongjian.webserver.ExceptionHandle.shareBenefitException;

public interface MemberShipService {

	// 生成viporder
	HashMap<String, Object> createVOrder(Integer lev, BigDecimal needPay, Integer UserId, Integer type);

	// 同步支付viporder
	void syncHandleVipOrder(Integer UserId, String orderNo);

	// 生成viporder
	String createCOrder(Integer userId, BigDecimal money);

	// 获取红黄蓝粉
	Map<String, Integer> getRYBFans(Integer userId);

	// 获取红黄蓝粉
	List<Map<String, Object>> getRYBFansDetails(Integer userId, String type);

	// 判断升级
	boolean memberUpdate(Integer userId, Integer type) throws shareBenefitException;

	// 查询某个用户的累计积分
	BigDecimal getAccumulateScore(Integer inviteCode);

	// 会员之前转让现金币
	boolean transferOfMoney(BigDecimal actualMoney, Integer userId, Integer sysID);

	// 查询未赠送名额
	List<Map<String, Object>> getPossessorPresent(Integer userId);
	
	// 查询已赠送名额
	List<Map<String, Integer>> getAlreadyGivePresent(Integer userId);

}

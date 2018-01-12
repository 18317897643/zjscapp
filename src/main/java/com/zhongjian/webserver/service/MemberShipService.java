package com.zhongjian.webserver.service;

import java.math.BigDecimal;
import java.util.HashMap;

public interface MemberShipService {

	// 生成viporder
	HashMap<String, Object> createVOrder(Integer lev,BigDecimal needPay, Integer UserId, Integer type);

	// 同步支付viporder
	void syncHandleVipOrder(Integer UserId, String orderNo);
}

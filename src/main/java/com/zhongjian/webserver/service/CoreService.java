package com.zhongjian.webserver.service;

import java.math.BigDecimal;

public interface CoreService {

	//记录分润以防遗漏
	void preRecordShareBenti(Integer type, Integer masterUserId, Integer slaveUserId, String memo, BigDecimal ElecNum);
	
	//分润
	void shareBenit(Integer type, Integer masterUserId, Integer slaveUserId, String memo, BigDecimal ElecNum);

	void test(Integer type , Integer masterUserId);
}

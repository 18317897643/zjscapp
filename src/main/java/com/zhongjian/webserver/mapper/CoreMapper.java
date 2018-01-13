package com.zhongjian.webserver.mapper;

import java.util.Map;

public interface CoreMapper {
	Map<String, Integer> selectHigherLev(Integer userId);
	
	void addDistributionRecord(Map<String, Object> distributionRecord);
}

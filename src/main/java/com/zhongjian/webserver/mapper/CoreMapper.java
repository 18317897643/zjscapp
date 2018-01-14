package com.zhongjian.webserver.mapper;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CoreMapper {
	Map<String, Integer> selectHigherLev(Integer userId);

	void addDistributionRecord(Map<String, Object> distributionRecord);

	Integer selectCommendNumOfUser(Integer userId);

	void insertCommendNumOfUser(Integer userId);

	void setNumCommendOfUser(@Param("UserId")Integer userId , @Param("Num") Integer num);
	
	void insertSendHead(@Param("CreateTime") Date createTime, @Param("UserId") Integer userId,
			@Param("Lev") Integer lev, @Param("SendType") Integer sendType);
}

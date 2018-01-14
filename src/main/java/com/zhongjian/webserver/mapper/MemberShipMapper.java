package com.zhongjian.webserver.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MemberShipMapper {
	
	void insertVipOrder(Map<String, Object> map);
	
	void insertCOrder(Map<String, Object> map);
	
	Map<String, Object> selectViporderByOrderAndUser(@Param("OrderNo") String orderNo,@Param("UserId") Integer userId);
	
	Map<String, Object> selectViporderByOrder(String orderNo);
	
	Map<String, Object> selectCOrderByOrderNo(String orderNo);
	
	Integer changeVipOrderToPaid(String orderNo);
	
	Integer changeCOrderToPaid(String orderNo);
	
}

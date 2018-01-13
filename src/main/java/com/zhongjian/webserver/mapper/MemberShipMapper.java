package com.zhongjian.webserver.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MemberShipMapper {
	
	void insertVipOrder(Map<String, Object> map);
	
	Map<String, Object> selectViporderByOrderNo(@Param("OrderNo") String orderNo,@Param("UserId") Integer userId);
}

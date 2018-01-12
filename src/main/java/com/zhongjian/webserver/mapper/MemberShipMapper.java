package com.zhongjian.webserver.mapper;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MemberShipMapper {
	
	void insertVipOrder(Map<String, Object> map);
	
	BigDecimal selectViporderByOrderNo(@Param("OrderNo") String orderNo,@Param("UserId") Integer userId);
}

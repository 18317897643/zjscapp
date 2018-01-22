package com.zhongjian.webserver.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MemberShipMapper {

	void insertVipOrder(Map<String, Object> map);

	void insertCOrder(Map<String, Object> map);

	Map<String, Object> selectViporderByOrderAndUser(@Param("OrderNo") String orderNo, @Param("UserId") Integer userId);

	Map<String, Object> selectViporderByOrder(String orderNo);

	Map<String, Object> selectCOrderByOrderNo(String orderNo);

	Integer changeVipOrderToPaid(String orderNo);

	Integer changeCOrderToPaid(String orderNo);

	// 红粉
	Integer getRedFans(Integer inviteCode);

	// 蓝粉
	Integer getBlueFans(Integer inviteCode);

	// 黄粉
	Integer getYellowFans(Integer inviteCode);

	// 红粉具体数据
	List<Map<String, Object>> getRedFansDetails(Integer inviteCode);

	// 蓝粉具体数据
	List<Map<String, Object>> getBlueFansDetails(Integer inviteCode);

	// 黄粉具体数据
	List<Map<String, Object>> getYellowFansDetails(Integer inviteCode);

	// 计算贡献额度
	BigDecimal getContributeAmount(@Param("FromUserId") Integer fromUserId, @Param("UserId") Integer userId);
	
	List<Map<String, Object>> getPossessorPresent(Integer userId);
	
	List<Map<String, Integer>> getAlreadyGivePresent(Integer userId);

}
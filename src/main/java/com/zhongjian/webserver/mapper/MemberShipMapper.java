package com.zhongjian.webserver.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MemberShipMapper {

	void insertVipOrder(Map<String, Object> map);

	void insertCOrder(Map<String, Object> map);

	Map<String, Object> selectViporderByOrderAndUser(@Param("OrderNo") String orderNo, @Param("UserId") Integer userId);

	Map<String, Object> selectViporderByOrderNo(String orderNo);

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
	List<Map<String, Object>> getRedFansDetails(@Param("inviteCode") Integer inviteCode,
			@Param("OffSet") Integer offSet, @Param("PageNum") Integer pageNum);

	// 蓝粉具体数据
	List<Map<String, Object>> getBlueFansDetails(@Param("inviteCode") Integer inviteCode,
			@Param("OffSet") Integer offSet, @Param("PageNum") Integer pageNum);

	// 黄粉具体数据
	List<Map<String, Object>> getYellowFansDetails(@Param("inviteCode") Integer inviteCode,
			@Param("OffSet") Integer offSet, @Param("PageNum") Integer pageNum);

	// 计算贡献额度
	BigDecimal getContributeAmount(@Param("FromUserId") Integer fromUserId, @Param("UserId") Integer userId);

	List<Map<String, Object>> getPossessorPresent(Integer userId);

	List<Map<String, Integer>> getAlreadyGivePresent(Integer userId);

	Integer getPresentById(Integer sendHeadId);

	Integer changePresentStatusToOne(Integer sendHeadId);

	void insertSendHeadRecord(@Param("SendHeadId") Integer sendHeadId, @Param("UserId") Integer userId,
			@Param("Lev") Integer lev, @Param("CreateTime") Date createTime);

	void insertSplitStreamRecord(@Param("CreateTime") Date createTime, @Param("FromUserId") Integer fromUserId,
			@Param("ToUserId") Integer toUserId, @Param("Amount") BigDecimal amount);

	List<Map<String, Object>> selectSplitStreamRecord(Integer fromUserId);

}
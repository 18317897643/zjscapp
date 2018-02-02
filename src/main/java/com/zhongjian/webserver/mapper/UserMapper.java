package com.zhongjian.webserver.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.BillReacord;
import com.zhongjian.webserver.pojo.User;

public interface UserMapper {

	int insertSelective(User record);

	Integer checkUserNameExists(String userName);

	Integer selectUserMaxSysID();

	Integer checkUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

	String getPayPasswordByUserName(@Param("userName") String userName);

	Integer updatePassword(@Param("userName") String userName, @Param("password") String password);

	Map<String, Object> selectPersonalInform(String userName);

	Map<String, Object> getInviteCodeAndTotalCostById(Integer id);
	
	Integer getUserIdByInviteCode(Integer inviteCode);
	
	Map<String, Object> selectPersonalInformById(Integer userId);

	Integer getUserIdByUserName(String userName);

	String getUserNameByUserId(Integer userId);

	Integer updateByUserNameSelective(User user);
	
	Integer updateUserNameByOldUserName(@Param("UserName") String userName, @Param("OldUserName") String oldUserName);

	Integer InviteCodeIsExists(Integer inviteCode);

	Map<String, Object> selectUserQuotaForUpdate(Integer id);

	Map<String, Integer> checkUserFundOrCurSta(String userName);

	Integer checkUserNewExclusive(Integer userId);

	void updateUserQuota(Map<String, Object> quotaMap);

	Integer updateNewExclusiveDraw(Integer userId);

	Integer checkUserIdExits(Integer id);

	Integer queryUserAuth(Integer id);

	Date getExpireTimeFromGcOfUser(Integer id);

	void setLev(@Param("Lev") Integer lev, @Param("IsSubProxy") Integer subProxy, @Param("UserId") Integer userId);

	Integer updateExpireTimeOfGcOfUser(@Param("ExpireTime") Date expireTime, @Param("UserId") Integer userId);

	void insertExpireTimeOfGcOfUser(@Param("ExpireTime") Date expireTime, @Param("UserId") Integer userId);

	List<BillReacord> getCouponBill(@Param("UserId") Integer userId, @Param("page") Integer page,
			@Param("pageNum") Integer pageNum);

	List<BillReacord> getElecBill(@Param("UserId") Integer userId, @Param("page") Integer page,
			@Param("pageNum") Integer pageNum);

	List<BillReacord> getPointBill(@Param("UserId") Integer userId, @Param("page") Integer page,
			@Param("pageNum") Integer pageNum);

	List<BillReacord> getVipBill(@Param("UserId") Integer userId, @Param("page") Integer page,
			@Param("pageNum") Integer pageNum);

	List<Map<String, Object>> getDownInviteAndTotalCost(Integer inviteCode);
	
	Map<String, Object> getUserInfoBySysID(Integer sysID);
	
	Map<String, Object> getCertificationInfo(Integer userId);
}
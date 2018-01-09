package com.zhongjian.webserver.mapper;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.annotations.Param;

public interface LogMapper {
	void insertCouponRecord(@Param("UserId") Integer userId, @Param("CreateTime") Date createTime,
			@Param("Amount") BigDecimal amount, @Param("AddSub") String addSub, @Param("Memo") String memo);
	void insertElecRecord(@Param("UserId") Integer userId, @Param("CreateTime") Date createTime,
			@Param("Amount") BigDecimal amount, @Param("AddSub") String addSub, @Param("Memo") String memo);
	void insertVipRemainRecord(@Param("UserId") Integer userId, @Param("CreateTime") Date createTime,
			@Param("Amount") BigDecimal amount, @Param("AddSub") String addSub, @Param("Memo") String memo);
	void insertPointRecord(@Param("UserId") Integer userId, @Param("CreateTime") Date createTime,
			@Param("Amount") BigDecimal amount, @Param("AddSub") String addSub, @Param("Memo") String memo);
}

package com.zhongjian.webserver.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderLineDto;

public interface OrderMapper {

	List<Integer> getUserOrderStatus(Integer userId);

	Integer insertOrderHead(OrderHeadDto orderHeadDto);

	Integer insertOrderLine(OrderLineDto orderLineDto);

	Integer getSpecElecNumById(Integer specId);

	Map<String, Object> getDetailsFormorderheadC(String orderNoCName);

	Integer updateOrderHeadCoCur();

	void updateUserQuota(BigDecimal RemainStream);

	void updateOrderHeadScore(@Param("Score") Integer score, @Param("Id") Integer id);

	void insertOrderHeadCo(@Param("orderNoCName") String orderNoCName, @Param("orderNoC") String orderNoC,
			@Param("tolAmount") BigDecimal tolAmount,@Param("curTime")Date curTime ,@Param("UserId") Integer UserId);

	Map<String, Object> getNeedSubDetailsOfOrderHead(String oderNo);

	Integer updateOrderHeadStatusToWP(String oderNo);

	void updateOrderHeadStatus(@Param("CurStatus") Integer curStatus, @Param("OrderNo") String orderNo);

	void insertPreSubQuata(@Param("UserId") Integer userId,@Param("PreUseCoupon") BigDecimal preUseCoupon,@Param("PreUseElec") BigDecimal preUseElec,@Param("PreUsePoints") BigDecimal preUsePoints,@Param("PreUseVipremain") BigDecimal preUseVipremain,@Param("ExpireTime") Date expireTime);

}
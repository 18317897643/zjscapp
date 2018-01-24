package com.zhongjian.webserver.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderLineDto;
import com.zhongjian.webserver.pojo.Orderhead;

public interface OrderMapper {

	List<Integer> getUserOrderStatus(Integer userId);

	Integer insertOrderHead(OrderHeadDto orderHeadDto);

	Integer insertOrderLine(OrderLineDto orderLineDto);

	Map<String, Object> getSpecElecNumAndPriceById(Integer specId);

	Map<String, Object> getDetailsFormorderheadC(String orderNoCName);

	Integer getPlatformMoneyOfOrderhead(String OderNo);

	Integer updateOrderHeadCoCur();

	void updateOrderHeadScore(@Param("Score") Integer score, @Param("Id") Integer id);

	void insertOrderHeadCo(@Param("orderNoCName") String orderNoCName, @Param("orderNoC") String orderNoC,
			@Param("tolAmount") BigDecimal tolAmount, @Param("platformMoney") BigDecimal platformMoney,
			@Param("curTime") Date curTime, @Param("UserId") Integer UserId);

	Map<String, Object> getNeedSubDetailsOfOrderHead(String oderNo);

	Integer updateOrderHeadStatusToWP(String oderNo);

	void updateOrderHeadStatus(@Param("CurStatus") Integer curStatus, @Param("OrderNo") String orderNo);

	void autoCancelOrder(@Param("DateStrString") String dateStr,@Param("Duration") Integer duration);
	
	void insertPreSubQuata(@Param("UserId") Integer userId, @Param("PreUseCoupon") BigDecimal preUseCoupon,
			@Param("PreUseElec") BigDecimal preUseElec, @Param("PreUsePoints") BigDecimal preUsePoints,
			@Param("PreUseVipremain") BigDecimal preUseVipremain, @Param("ExpireTime") Date expireTime);

	Integer getUserIdByOrderC(String orderNoC);

	Integer getUserIdByOrder(String orderNo);

	Orderhead getOrderDetailsById(Integer id);

	List<Orderhead> getOrderDetailsByCurStatus(@Param("UserId") Integer userId, @Param("Condition") String condition,
			@Param("OffSet") Integer offSet, @Param("PageNum") Integer pageNum);

	String getPhotoByProductId(Integer productId);
	
	Integer updateOrderHeadStatusToWC(String orderNo);
	
	//改变状态至申请退款 5
	Integer updateOrderHeadStatusToAR(String orderNo);
	
	//改变状态至申请退货 4
	Integer updateOrderHeadStatusToASR(String orderNo);
	
	Map<String, Object> getOrderDetailsByOrderNo(String oderNo);
	
	List<String> getWROrderNo(@Param("DateStrString") String dateStr,@Param("Duration") Integer duration);
	
	Integer getOrderIdByOrderNo(String orderNo);
}
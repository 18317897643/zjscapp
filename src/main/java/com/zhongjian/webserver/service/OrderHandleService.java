package com.zhongjian.webserver.service;

import java.util.HashMap;
import java.util.List;

import com.alipay.api.AlipayApiException;
import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderHeadEXDto;

public interface OrderHandleService {

	HashMap<String, Object> createOrder(List<OrderHeadDto> orderHeads, Integer UserId);

	boolean asyncHandleOrder(String orderNo, String totalAmount,String seller_id ,String app_id);
	
	boolean syncHandleOrder(String orderNo);
	
	String createAliSignature(String out_trade_no, String totalAmount) throws AlipayApiException;
	
	Integer getUserIdByOrderC(String orderNoC);
	
	Integer getUserIdByOrder(String orderNo);
	
	List<OrderHeadDto> handleOrderHeadDtoByAdressId(OrderHeadEXDto orderHeadEXDto);
	
	//取消订单
	void cancelOrder(String orderNo);
	
	//自动取消超过一天的待付款订单
	void autoCancelOrder();
	
	//确认收货
	void confirmOrder(String orderNo);
	
	//自动确认收货
	void autoConfirmOrder();
	
	
}

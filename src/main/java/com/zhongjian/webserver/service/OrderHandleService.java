package com.zhongjian.webserver.service;

import java.util.HashMap;
import java.util.List;

import com.alipay.api.AlipayApiException;
import com.zhongjian.webserver.dto.OrderHeadDto;

public interface OrderHandleService {

	HashMap<String, Object> createOrder(List<OrderHeadDto> orderHeads, Integer UserId);

	boolean asyncHandleOrder(String orderNo, String totalAmount,String seller_id ,String app_id);
	
	boolean syncHandleOrder(String orderNo, Integer platformMoneyAmount);
	
	String createAliSignature(String out_trade_no, String totalAmount) throws AlipayApiException;
	
	Integer getUserIdByOrderC(String orderNoC);
	
	Integer getUserIdByOrder(String orderNo);

}

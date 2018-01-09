package com.zhongjian.webserver.service;

import java.util.HashMap;
import java.util.List;

import com.alipay.api.AlipayApiException;
import com.zhongjian.webserver.dto.OrderHeadDto;

public interface OrderHandleService {

	HashMap<String, Object> createOrder(List<OrderHeadDto> orderHeads, Integer UserId);

	boolean handleOrder(String orderNo, String totalAmount,String seller_id ,String app_id);
	
	String createAliSignature(String out_trade_no, String totalAmount) throws AlipayApiException;
}

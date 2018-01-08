package com.zhongjian.webserver.service;

import java.util.HashMap;
import java.util.List;

import com.alipay.api.AlipayApiException;
import com.zhongjian.webserver.dto.OrderHeadDto;

public interface OrderHandleService {

	HashMap<String, Object> createOrder(List<OrderHeadDto> orderHeads, Integer UserId);

	void test(String orderNo);
	
	String createAliSignature(String out_trade_no, String totalAmount) throws AlipayApiException;
}

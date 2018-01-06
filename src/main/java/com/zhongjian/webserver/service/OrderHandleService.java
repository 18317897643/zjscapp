package com.zhongjian.webserver.service;

import java.util.List;

import com.zhongjian.webserver.dto.OrderHeadDto;

public interface OrderHandleService {
	
	boolean createOrder(List<OrderHeadDto> orderHeads,Integer UserId);
	
	

}

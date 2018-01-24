package com.zhongjian.webserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.service.OrderApplyService;

public class OrderApplyServiceImpl implements OrderApplyService {

	@Autowired
	OrderMapper orderMapper;
	
	@Override
	public void applyCancelOrder(String orderNo, String memo) {
		//更改订单为申请取消状态

	}

	@Override
	public void applySaleReturn(String orderNo, String memo, String photo1, String photo2, String photo3) {
		// TODO Auto-generated method stub

	}

}

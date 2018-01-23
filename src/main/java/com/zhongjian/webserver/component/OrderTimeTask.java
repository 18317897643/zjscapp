package com.zhongjian.webserver.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zhongjian.webserver.service.OrderHandleService;

@Component
public class OrderTimeTask {

	@Autowired
	OrderHandleService orderHeadService;
	
	//等待支付超过一天的订单系统自动取消掉每天午夜12点触发
	@Scheduled(cron = "0 0 00 * * ?")
	public void cancelOrder() {
		orderHeadService.autoCancelOrder();
	}
	
	//等待收货的订单超过7天自动确认收货
	@Scheduled(cron = "0 0 00 * * ?")
	public void autoConfirmOrder(){
		
	}
}

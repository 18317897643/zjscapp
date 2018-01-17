package com.zhongjian.webserver.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderTimeTask {

	
	//等待支付超过一天的订单系统自动取消掉
	@Scheduled(cron = "0 0/1 * * * ?")
	public void cancelOrder() {
		
		
	}
	
	//等待收货的订单超过7天自动确认收货
	@Scheduled(cron = "0 0/1 * * * ?")
	public void autoConfirmOrder(){
		
	}
}

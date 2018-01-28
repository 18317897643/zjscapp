package com.zhongjian.webserver.service;

public interface OrderApplyService {

	boolean applyCancelOrder(String orderNo,String memo);
	
	boolean applySaleReturn(String orderNo,String memo,String photo1,String photo2,String photo3);
	
}

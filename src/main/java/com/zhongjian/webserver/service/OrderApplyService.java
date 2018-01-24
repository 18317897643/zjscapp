package com.zhongjian.webserver.service;

public interface OrderApplyService {

	void applyCancelOrder(String orderNo,String memo);
	
	void applySaleReturn(String orderNo,String memo,String photo1,String photo2,String photo3);
	
}

package com.zhongjian.webserver.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.domain.Data;
import com.zhongjian.webserver.mapper.ApplyCancelOrderMapper;
import com.zhongjian.webserver.mapper.ApplyReturnOrderMapper;
import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.pojo.ApplyCancelOrder;
import com.zhongjian.webserver.service.OrderApplyService;

public class OrderApplyServiceImpl implements OrderApplyService {

	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	ApplyCancelOrderMapper applyCancelOrderMapper;
	
	@Autowired
	ApplyReturnOrderMapper applyReturnOrderMapper;
	
	@Override
	@Transactional
	public void applyCancelOrder(String orderNo, String memo) {
		//更改订单为申请取消状态
		Integer orderId = orderMapper.getUserIdByOrder(orderNo);
		if (orderMapper.updateOrderHeadStatusToAR(orderNo) == 1) {
			//插入申请取消审核
//			ApplyCancelOrder record = new ApplyCancelOrder();
//			record.setCreatetime(new Date());
//			record.setCurstatus(0);
//			record.setMemo(memo);
//		record.setOrderid(orderid);
//			applyCancelOrderMapper.insertSelective(record)
			
			
		}
	}

	@Override
	public void applySaleReturn(String orderNo, String memo, String photo1, String photo2, String photo3) {
		//更改订单为申请退货状态
		if (orderMapper.updateOrderHeadStatusToASR(orderNo) == 1) {
			//插入申请退货审核
			
			
			
		}

	}

}

package com.zhongjian.webserver.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.mapper.ApplyCancelOrderMapper;
import com.zhongjian.webserver.mapper.ApplyReturnOrderMapper;
import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.pojo.ApplyCancelOrder;
import com.zhongjian.webserver.pojo.ApplyReturnOrder;
import com.zhongjian.webserver.service.OrderApplyService;

@Service
public class OrderApplyServiceImpl implements OrderApplyService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private ApplyCancelOrderMapper applyCancelOrderMapper;

	@Autowired
	private ApplyReturnOrderMapper applyReturnOrderMapper;

	@Override
	@Transactional
	public boolean applyCancelOrder(String orderNo, String memo) {
		Integer orderId = orderMapper.getOrderIdByOrderNo(orderNo);
		// 更改订单为申请取消状态
		if (orderMapper.updateOrderHeadStatusToAR(orderNo) == 1) {
			// 插入申请取消审核
			ApplyCancelOrder record = new ApplyCancelOrder();
			record.setCreatetime(new Date());
			record.setCurstatus(0);
			record.setMemo(memo);
			record.setOrderid(orderId);
			applyCancelOrderMapper.insertSelective(record);
			return true;
		}else{
			return false;
		}
	}
	@Override
	public boolean applySaleReturn(String orderNo, String memo, String photo1, String photo2, String photo3) {
		Integer orderId = orderMapper.getOrderIdByOrderNo(orderNo);
		// 更改订单为申请退货状态
		if (orderMapper.updateOrderHeadStatusToASR(orderNo) == 1) {
			// 插入申请退货审核
			ApplyReturnOrder record = new ApplyReturnOrder();
			record.setCreatetime(new Date());
			record.setCurstatus(0);
			record.setMemo(memo);
			record.setOrderid(orderId);
			record.setPhoto1(photo1);
			record.setPhoto2(photo2);
			record.setPhoto3(photo3);
			applyReturnOrderMapper.insertSelective(record);
			return true;
		}else{
			return false;
		}
	}
}

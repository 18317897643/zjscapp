package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.common.RandomUtil;
import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderLineDto;
import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.service.OrderHandleService;

@Service
public class OrderHandleServiceImpl implements OrderHandleService {

	@Autowired
	OrderMapper orderMapper;

	@Override
	@Transactional
	public boolean createOrder(List<OrderHeadDto> orderHeads, Integer UserId) {
		orderHeads.forEach(e -> {
			if (e.getFreight().compareTo(BigDecimal.ZERO) == -1 || e.getRealPay().compareTo(BigDecimal.ZERO) == -1
					|| e.getUseCoupon().compareTo(BigDecimal.ZERO) == -1
					|| e.getUseElecNum().compareTo(BigDecimal.ZERO) == -1
					|| e.getUsePointNum().compareTo(BigDecimal.ZERO) == -1
					|| e.getUseVIPRemainNum().compareTo(BigDecimal.ZERO) == -1
					|| e.getRealPay().compareTo(BigDecimal.ZERO) == -1
					|| !e.getFreight().add(e.getUseVIPRemainNum()).add(e.getUseCoupon()).add(e.getUseElecNum())
							.add(e.getUsePointNum()).add(e.getRealPay()).equals(e.getTotalAmount())) {
					throw new RuntimeException("数据校验不通过");
			} else {
				e.setCreateTime(new Date());
				e.setOrderNo(RandomUtil.getFlowNumber());
				e.setUserId(UserId);
				// 如果插入成功返回orderId
				orderMapper.insertOrderHead(e);
				Integer orderId = e.getId();
				System.out.println(orderId);
				List<OrderLineDto> orderLines = e.getOrderLines();
				//该单总分值
				List<Integer> score = new ArrayList<>();
				score.add(0);
				orderLines.forEach(f -> {
					Integer productNum = f.getProductNum();
					Integer specElecNum = orderMapper.getSpecElecNumById(f.getSpecId());
					Integer thisTimeScore = productNum * specElecNum;
					score.set(0, score.get(0) + thisTimeScore);
					f.setOrderId(orderId);
					orderMapper.insertOrderLine(f);
				});
				//分值录入
				orderMapper.updateOrderHeadScore(score.get(0), orderId);
			}
		});
		return true;
	}
}

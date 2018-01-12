package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.common.RandomUtil;
import com.zhongjian.webserver.component.AsyncTasks;
import com.zhongjian.webserver.component.MallData;
import com.zhongjian.webserver.mapper.MemberShipMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.service.MemberShipService;

@Service
public class MemberShipServiceImpl implements MemberShipService {

	@Autowired
	MallData mallData;

	@Autowired
	MemberShipMapper memberShipMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	AsyncTasks tasks;

	@Override
	public HashMap<String, Object> createVOrder(Integer lev,BigDecimal needPay, Integer UserId, Integer type) {
		// 查询用户账上余额，不够就不生成订单
		HashMap<String, Object> data = new HashMap<>();
		if (type == 0) {
			data.put("ElecNum", needPay);
			data.put("RealPay", BigDecimal.ZERO);
			data.put("TolAmout", needPay);
		} else {
			data.put("RealPay", needPay);
			data.put("ElecNum", BigDecimal.ZERO);
			data.put("TolAmout", needPay);
		}
		String orderNo = "VO" + RandomUtil.getFlowNumber();
		data.put("OrderNo", orderNo);
		data.put("UserId", UserId);
		data.put("CreateTime", new Date());
		data.put("Lev", lev);
		memberShipMapper.insertVipOrder(data);
		HashMap<String, Object> result = new HashMap<>();
		result.put("OrderNo", orderNo);
		result.put("TolAmout", needPay);
		// 生成绿色通道订单
		return result;
	}

	@Override
	@Transactional
	public void syncHandleVipOrder(Integer UserId, String orderNo) {
		BigDecimal useElecNum = memberShipMapper.selectViporderByOrderNo(orderNo, UserId);
		Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
		BigDecimal remainElec = ((BigDecimal) curQuota.get("RemainElecNum")).subtract(useElecNum);
		curQuota.put("RemainElecNum", remainElec);
		userMapper.updateUserQuota(curQuota);
		// 分润
		tasks.shareBenitTask(1, UserId, 0, "购买会员", useElecNum);
	}
}

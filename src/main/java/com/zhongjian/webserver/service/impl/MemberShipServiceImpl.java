package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.beanconfiguration.AsyncTasks;
import com.zhongjian.webserver.beanconfiguration.MallData;
import com.zhongjian.webserver.common.RandomUtil;
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
	public HashMap<String, Object> createVOrder(Integer lev, Integer UserId, Integer type) {
		//查询用户账上余额，不够就不生成订单
		HashMap<String, Object> data = new HashMap<>();
		BigDecimal needMoney = null;
		if (lev == 0) {
			needMoney = new BigDecimal(mallData.getGcNeedPay());
			if (type == 0) {
				data.put("ElecNum", needMoney);
				data.put("RealPay", BigDecimal.ZERO);
				data.put("TolAmout", needMoney);
			} else {
				data.put("RealPay", needMoney);
				data.put("ElecNum", BigDecimal.ZERO);
				data.put("TolAmout", needMoney);
			}
			// lev = 1
		} else {
			needMoney = new BigDecimal(mallData.getVipNeedPay());
			if (type == 0) {
				data.put("ElecNum", needMoney);
				data.put("RealPay", BigDecimal.ZERO);
				data.put("TolAmout", needMoney);
			} else {
				data.put("RealPay", needMoney);
				data.put("ElecNum", BigDecimal.ZERO);
				data.put("TolAmout", needMoney);
			}
		}
		String orderNo = "VO" + RandomUtil.getFlowNumber();
		data.put("OrderNo", orderNo);
		data.put("UserId", UserId);
		data.put("CreateTime", new Date());
		data.put("Lev", lev);
		memberShipMapper.insertVipOrder(data);
		HashMap<String, Object> result = new HashMap<>();
		result.put("OrderNo", orderNo);
		result.put("TolAmout", needMoney);
		// 生成绿色通道订单
		return result;
	}

	@Override
	@Transactional
	public void syncHandleVipOrder(Integer UserId,String orderNo) {
		BigDecimal useElecNum = memberShipMapper.selectViporderByOrderNo(orderNo,UserId);
		Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
		BigDecimal remainElec =((BigDecimal) curQuota.get("RemainElecNum")).subtract(useElecNum);
		curQuota.put("RemainElecNum", remainElec);
		userMapper.updateUserQuota(curQuota);
		//分润
		tasks.shareBenitTask(1, UserId, 0, "购买会员", useElecNum);
	}
}

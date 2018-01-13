package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.common.RandomUtil;
import com.zhongjian.webserver.component.AsyncTasks;
import com.zhongjian.webserver.component.MallData;
import com.zhongjian.webserver.mapper.LogMapper;
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
	LogMapper logMapper;
	
	@Autowired
	AsyncTasks tasks;

	@Override
	public HashMap<String, Object> createVOrder(Integer lev, BigDecimal needPay, Integer UserId, Integer type) {
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
		Map<String, Object> data = memberShipMapper.selectViporderByOrderNo(orderNo, UserId);
		BigDecimal useElecNum = (BigDecimal) data.get("TolAmout");
		Integer lev = (Integer) data.get("Lev");
		Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
		BigDecimal remainElec = ((BigDecimal) curQuota.get("RemainElecNum")).subtract(useElecNum);
		curQuota.put("RemainElecNum", remainElec);
		userMapper.updateUserQuota(curQuota);
		String memo = "";
		BigDecimal amount = null;
		// 更新等级
		if (lev == 1) {
			//升级vip
			userMapper.setLev(1, 0, UserId);
			memo = "购买VIP，订单号：" + orderNo;
			amount = new BigDecimal(mallData.getVipNeedPay());
		}else{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 30);//计算30天后的时间
			Date expireTime = c.getTime();
			//升级绿色通道
			if (userMapper.updateExpireTimeOfGcOfUser(expireTime,UserId) != 1) {
				userMapper.insertExpireTimeOfGcOfUser(expireTime, UserId);
			}
			memo = "购买绿色通道，订单号：" + orderNo;
			amount = new BigDecimal(mallData.getGcNeedPay());
		}
	    //记录现金购买vip
		logMapper.insertElecRecord(UserId, new Date(), amount, "-", memo);
		// 分润
		tasks.shareBenitTask(1, UserId, 0, "购买会员", useElecNum);
	}
}

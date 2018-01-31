package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.mapper.WaterPurifierMapper;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.WaterPurifierService;

@Service
public class WaterPurifierServiceImpl implements WaterPurifierService {

	@Autowired
	private WaterPurifierMapper waterPurifierMapper;
	
	@Autowired
	private LoginAndRegisterService LoginAndRegisterService;
	
	@Override
	@Transactional
	public String drawWaterPurifierCupon(Integer userId, String codeNo) {
		//查询type和status状态
		Map<String, Object> codeInfo = waterPurifierMapper.getCodeInfoByCodeNo(codeNo);
		if (codeInfo == null) {
			return "1";
		}
		Date expiretime = (Date) codeInfo.get("Expiretime");
		Date curTime = new Date();
		if (curTime.compareTo(expiretime) > 0) {
			return "-1";//过期了
		}
		Integer curstatus = (Integer) codeInfo.get("CurStatus");
		if (curstatus == -1) {
			return "-1";//领过了
		}
		Integer type = (Integer) codeInfo.get("Type");
		if (waterPurifierMapper.deleteCode(codeNo) == 1) {
			if (type == 1) {
				LoginAndRegisterService.sendCouponByUserId(new BigDecimal("980.00"), userId,"水机充值兑换红包");
			}else {
				LoginAndRegisterService.sendCouponByUserId(new BigDecimal("1280.00"), userId,"水机充值兑换红包");
			}
			return "0";
		}else {
			return "-1";
		}
	}

}

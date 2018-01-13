package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.mapper.CoreMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.service.CoreService;

@Service
public class CoreServiceImpl implements CoreService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	CoreMapper coreMapper;

	@Override
	public void preRecordShareBenti(Integer type, Integer masterUserId, Integer slaveUserId, String memo,
			BigDecimal ElecNum) {

	}

	@Override
	@Transactional
	public void shareBenit(Integer type, Integer masterUserId, Integer slaveUserId, String memo, BigDecimal ElecNum) {
		Map<BigDecimal, Integer> resultMap = null;
		if (type == 1) {
			resultMap = normalBenifit(masterUserId);
		} else if (type == 2) {
			resultMap = new HashMap<>();
			Map<String, Integer> map = coreMapper.selectHigherLev(masterUserId);
			if (map != null) {
				if (map.get("Lev") == 3) {
					resultMap.put(new BigDecimal("0.10"), map.get("Id"));
				}
			}

		} else {
			// 此时masterUserId是被分流者，slaveUserId是分流者
			Map<String, Integer> slaveUpMap = coreMapper.selectHigherLev(slaveUserId);
			if (slaveUpMap.get("Lev") != 3) {
				resultMap = normalBenifit(masterUserId);
			} else {
				resultMap = abNormalBenifit(masterUserId);
			}
		}
		// 实际分润并产生记录
		if (resultMap.size() != 0) {
			for (Entry<BigDecimal, Integer> entry : resultMap.entrySet()) {
				
				System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
			}
		}
	}

	private Map<BigDecimal, Integer> normalBenifit(Integer masterUserId) {

		Map<BigDecimal, Integer> resultMap = new HashMap<>();
		Integer queryUserId = masterUserId;
		Integer i = 0;
		Integer flag = 0;
		while (true) {
			i++;
			Map<String, Integer> map = coreMapper.selectHigherLev(queryUserId);
			if (map == null) {
				break;
			}
			Integer curUserId = map.get("Id");
			Integer curUserlev = map.get("Lev");
			// 蓝粉和黄粉的分润
			if (i == 2) {
				resultMap.put(new BigDecimal("0.10"), curUserId);
			} else if (i == 3) {
				resultMap.put(new BigDecimal("0.20"), curUserId);
			}
			// 给代理和准代理的分润
			if (curUserlev == 3) {
				if (flag == 0) {
					resultMap.put(new BigDecimal("0.16"), curUserId);
					flag = 1;
				} else if (flag == 2) {
					resultMap.put(new BigDecimal("0.10"), curUserId);
					flag = 1;
				}
			} else if (curUserlev == 2) {
				if (flag == 0) {
					resultMap.put(new BigDecimal("0.06"), curUserId);
					flag = 2;
				}
			}
			if (i > 3 && flag == 1) {
				break;
			}
			queryUserId = curUserId;
		}
		return resultMap;
	}

	private Map<BigDecimal, Integer> abNormalBenifit(Integer masterUserId) {
		Map<BigDecimal, Integer> resultMap = new HashMap<>();
		Integer queryUserId = masterUserId;
		Integer i = 0;
		Integer flag = 0;
		while (true) {
			i++;
			Map<String, Integer> map = coreMapper.selectHigherLev(queryUserId);
			if (map == null) {
				break;
			}
			Integer curUserId = map.get("Id");
			Integer curUserlev = map.get("Lev");
			// 蓝粉和黄粉的分润
			if (i == 2) {
				resultMap.put(new BigDecimal("0.10"), curUserId);
			} else if (i == 3) {
				resultMap.put(new BigDecimal("0.20"), curUserId);
			}
			// 给代理和准代理的分润
			if (curUserlev == 3 || curUserlev == 2) {
				if (flag == 0) {
					resultMap.put(new BigDecimal("0.06"), curUserId);
					flag = 1;
				}
			}
			if (i > 3 && flag == 1) {
				break;
			}
			queryUserId = curUserId;
		}
		return resultMap;
	}

}

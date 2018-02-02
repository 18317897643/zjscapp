package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.common.TokenManager;
import com.zhongjian.webserver.common.jpushUtil;
import com.zhongjian.webserver.mapper.CoreMapper;
import com.zhongjian.webserver.mapper.LogMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.service.CoreService;
import com.zhongjian.webserver.service.PersonalCenterService;

@Service
public class CoreServiceImpl implements CoreService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private CoreMapper coreMapper;

	@Autowired
	private LogMapper logMapper;

	@Autowired
	private PersonalCenterService personalCenterService;

	@Autowired
	private TokenManager tokenManager;

	@Override
	public void preRecordShareBenti(Integer type, Integer masterUserId, Integer slaveUserId, String memo,
			BigDecimal ElecNum) {

	}

	@Override
	@Transactional
	public void shareBenit(Integer type, Integer masterUserId, Integer slaveUserId, String memo, BigDecimal ElecNum) {
		Map<Integer, BigDecimal> resultMap = null;
		if (type == 1) {
			//订单消费  购买会员 后台手动加积分
			resultMap = normalBenifit(masterUserId);
		} else if (type == 2) {
			//推荐代理
			resultMap = new HashMap<>();
			Map<String, Integer> map = coreMapper.selectHigherLev(masterUserId);
			if (map != null) {
				if (map.get("Lev") == 3) {
					Integer curUserId = map.get("Id");
					resultMap.put(curUserId, new BigDecimal("0.10").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
				}
			}

		} else {
			//分流
			// 此时masterUserId是被分流者，slaveUserId是分流者
			Map<String, Integer> slaveUpMap = coreMapper.selectHigherLev(slaveUserId);
			if (slaveUpMap == null || slaveUpMap.get("Lev") != 3) {
				resultMap = normalBenifit(masterUserId);
			} else {
				resultMap = abNormalBenifit(masterUserId);
			}
		}
		// 实际分润并产生记录
		if (resultMap.size() != 0) {
			BigDecimal percent10 = new BigDecimal("0.10");
			BigDecimal percent90 = new BigDecimal("0.90");
			for (Entry<Integer, BigDecimal> entry : resultMap.entrySet()) {
				// 查出自己的等级再决定分润到购物币或者现金币
				BigDecimal percent = entry.getValue();
				Integer userId = entry.getKey();
				Integer lev = (Integer) (userMapper.selectPersonalInformById(userId).get("Lev"));
				Boolean isGCMember = personalCenterService.isGCMember(userId);
				Date date = new Date();
				if (lev == 0 && !isGCMember) {
					// 分润购物币
					BigDecimal shareMoney = percent.multiply(ElecNum).setScale(2, BigDecimal.ROUND_HALF_UP);
					Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(userId);
					BigDecimal remainPoints = ((BigDecimal) curQuota.get("RemainPoints")).add(shareMoney);
					curQuota.put("RemainPoints", remainPoints);
					userMapper.updateUserQuota(curQuota);
					// 产生分润记录
					HashMap<String, Object> distributionRecord = new HashMap<>();
					distributionRecord.put("CreateTime", date);
					distributionRecord.put("FromUser_Id", masterUserId);
					distributionRecord.put("User_Id", userId);
					distributionRecord.put("User_ElecNum", shareMoney);
					distributionRecord.put("AmountType", 2);
					distributionRecord.put("Memo", memo);
					coreMapper.addDistributionRecord(distributionRecord);
					// 推送token
					String userName = userMapper.getUserNameByUserId(userId);
					String token = tokenManager.getTokenByUserName(userName);
					if (token != null) {
						jpushUtil.sendAlias("恭喜您得到" + shareMoney + "分润金额", DigestUtils.md5Hex(token), "extKey", "extValue");
					}
					// 个人账单明细记录
					logMapper.insertPointRecord(userId, date, shareMoney, "+", "分润");
				} else {
					// 分润现金币
					BigDecimal shareMoney = percent.multiply(ElecNum).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigDecimal shareMoneyToPoint = shareMoney.multiply(percent10).setScale(2, BigDecimal.ROUND_HALF_UP);
					BigDecimal shareMoneyToElec = shareMoney.multiply(percent90).setScale(2, BigDecimal.ROUND_HALF_UP);
					Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(userId);
					BigDecimal remainElecNum = ((BigDecimal) curQuota.get("RemainElecNum")).add(shareMoneyToElec);
					BigDecimal remainPoint = ((BigDecimal) curQuota.get("RemainPoints")).add(shareMoneyToPoint);
					curQuota.put("RemainElecNum", remainElecNum);
					curQuota.put("RemainPoints", remainPoint);
					userMapper.updateUserQuota(curQuota);
					// 产生现金币分润记录
					HashMap<String, Object> distributionElecRecord = new HashMap<>();
					distributionElecRecord.put("CreateTime", date);
					distributionElecRecord.put("FromUser_Id", masterUserId);
					distributionElecRecord.put("User_Id", userId);
					distributionElecRecord.put("User_ElecNum", shareMoneyToElec);
					distributionElecRecord.put("AmountType", 1);
					distributionElecRecord.put("Memo", memo);
					coreMapper.addDistributionRecord(distributionElecRecord);
					// 产生购物币分润记录
					HashMap<String, Object> distributionPointRecord = new HashMap<>();
					distributionPointRecord.put("CreateTime", date);
					distributionPointRecord.put("FromUser_Id", masterUserId);
					distributionPointRecord.put("User_Id", userId);
					distributionPointRecord.put("User_ElecNum", shareMoneyToPoint);
					distributionPointRecord.put("AmountType", 2);
					distributionPointRecord.put("Memo", memo);
					coreMapper.addDistributionRecord(distributionPointRecord);
					// 推送token
					String userName = userMapper.getUserNameByUserId(userId);
					String token = tokenManager.getTokenByUserName(userName);
					if (token != null) {
						jpushUtil.sendAlias("恭喜您得到" + shareMoney + "分润金额", DigestUtils.md5Hex(token), "extKey", "extValue");
					}
					// 个人账单明细记录
					logMapper.insertPointRecord(userId, date, shareMoneyToPoint, "+", "分润");
					logMapper.insertElecRecord(userId, date, shareMoneyToElec, "+", "分润");
				}
			}
		}
	}

	private Map<Integer, BigDecimal> normalBenifit(Integer masterUserId) {

		Map<Integer, BigDecimal> resultMap = new HashMap<>();
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
				resultMap.put(curUserId, new BigDecimal("0.10").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
			} else if (i == 3) {
				resultMap.put(curUserId, new BigDecimal("0.20").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
			}
			// 给代理和准代理的分润
			if (curUserlev == 3) {
				if (flag == 0) {
					resultMap.put(curUserId, new BigDecimal("0.16").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
					flag = 1;
				} else if (flag == 2) {
					resultMap.put(curUserId, new BigDecimal("0.10").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
					flag = 1;
				}
			} else if (curUserlev == 2) {
				if (flag == 0) {
					resultMap.put(curUserId, new BigDecimal("0.06").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
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

	private Map<Integer, BigDecimal> abNormalBenifit(Integer masterUserId) {
		Map<Integer, BigDecimal> resultMap = new HashMap<>();
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
				resultMap.put(curUserId, new BigDecimal("0.10").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
			} else if (i == 3) {
				resultMap.put(curUserId, new BigDecimal("0.20").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
			}
			// 给代理和准代理的分润
			if (curUserlev == 3 || curUserlev == 2) {
				if (flag == 0) {
					resultMap.put(curUserId, new BigDecimal("0.06").add(resultMap.get(curUserId)==null?BigDecimal.ZERO:resultMap.get(curUserId)));
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

	@Override
	@Transactional
	public void present(Integer type, Integer userId) {
		Map<String, Integer> map = coreMapper.selectHigherLev(userId);
		Date curDate = new Date();
		if (map == null) {
			return;
		}
		if (type == 1) {
			Integer curUserId = map.get("Id");
			// 查询推荐vip次数
			Integer num = coreMapper.selectCommendNumOfUser(curUserId);
			if (num == null) {
				coreMapper.insertCommendNumOfUser(curUserId);
			} else {
				if (num == 1) {
					// 送vip名额(二送一)
					coreMapper.insertSendHead(curDate, curUserId, 1, 1);
					// 请零
					coreMapper.setNumCommendOfUser(curUserId, 0);
				} else if (num == 0) {
					// 设为1
					coreMapper.setNumCommendOfUser(curUserId, 1);
				}
			}
		} else {
			Integer curUserId = map.get("Id");
			// 送vip名额(推荐代理送vip)
			coreMapper.insertSendHead(curDate, curUserId, 1, 2);
		}
	}
}

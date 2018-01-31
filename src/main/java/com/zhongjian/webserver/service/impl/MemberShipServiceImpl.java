package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.common.DateUtil;
import com.zhongjian.webserver.common.ExpiryMap;
import com.zhongjian.webserver.common.RandomUtil;
import com.zhongjian.webserver.common.ShareBenefitUtil;
import com.zhongjian.webserver.ExceptionHandle.shareBenefitException;
import com.zhongjian.webserver.component.AsyncTasks;
import com.zhongjian.webserver.mapper.LogMapper;
import com.zhongjian.webserver.mapper.MemberShipMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.service.MemberShipService;

@Service
public class MemberShipServiceImpl implements MemberShipService {

	@Autowired
	private MemberShipMapper memberShipMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private LogMapper logMapper;

	@Autowired
	private AsyncTasks tasks;

	@Autowired
	@Qualifier("mayNotUpdateMap")
	private ExpiryMap<String, String> mayNotUpdateMap;

	@Override
	public HashMap<String, Object> createVOrder(Integer lev, BigDecimal needPay, Integer UserId, Integer type) {
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
		if (memberShipMapper.changeVipOrderToPaid(orderNo) == 1) {
			Map<String, Object> data = memberShipMapper.selectViporderByOrderAndUser(orderNo, UserId);
			BigDecimal useElecNum = (BigDecimal) data.get("TolAmout");
			Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
			BigDecimal remainElec = ((BigDecimal) curQuota.get("RemainElecNum")).subtract(useElecNum);
			BigDecimal remainVIPAmount = ((BigDecimal) curQuota.get("RemainVIPAmount")).add(useElecNum);
			BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).add(useElecNum);
			BigDecimal remianTotalCost = ((BigDecimal) curQuota.get("TotalCost")).add(useElecNum);
			curQuota.put("RemainElecNum", remainElec);
			curQuota.put("RemainVIPAmount", remainVIPAmount);
			curQuota.put("Coupon", remainCoupon);
			curQuota.put("TotalCost", remianTotalCost);
			userMapper.updateUserQuota(curQuota);
			Integer lev = (Integer) data.get("Lev");
			String memo = "";
			// 更新等级
			if (lev == 1) {
				// 升级vip
				userMapper.setLev(1, 0, UserId);
				memo = "购买VIP，订单号：" + orderNo;
				// 升级提交生成二送一和一送一任务
				tasks.presentTask(1, UserId);
			} else {
				// Calendar c = Calendar.getInstance();
				// c.add(Calendar.DATE, 30);
				// Date expireTime = c.getTime();
				// 升级绿色通道
				Date curExpireTime = userMapper.getExpireTimeFromGcOfUser(UserId);
				Date newExpireTime = null;
				if (curExpireTime == null) {
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DATE, 30);
					newExpireTime = c.getTime();
					userMapper.insertExpireTimeOfGcOfUser(newExpireTime, UserId);
				} else {
					if (curExpireTime.getTime() > new Date().getTime()) {
						// 没过期
						Calendar c = Calendar.getInstance();
						c.setTime(curExpireTime);// 计算30天后的时间
						c.add(Calendar.DATE, 30);
						newExpireTime = c.getTime();

					} else {
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DATE, 30);
						newExpireTime = c.getTime();
					}
					userMapper.updateExpireTimeOfGcOfUser(newExpireTime, UserId);
				}
				memo = "购买绿色通道，订单号：" + orderNo;
			}
			// 记录现金购买vip
			logMapper.insertElecRecord(UserId, new Date(), useElecNum, "-", memo);
			logMapper.insertCouponRecord(UserId, new Date(), useElecNum, "+", memo);
			logMapper.insertVipRemainRecord(UserId, new Date(), useElecNum, "+", memo);
			// 分润
			tasks.shareBenitTask(1, UserId, 0, "购买会员", useElecNum);
		}
	}

	@Override
	public String createCOrder(Integer userId, BigDecimal money) {
		String orderNo = "CZ" + RandomUtil.getFlowNumber();
		HashMap<String, Object> data = new HashMap<>();
		data.put("OrderNo", orderNo);
		data.put("UserId", userId);
		data.put("CreateTime", new Date());
		data.put("Amount", money);
		memberShipMapper.insertCOrder(data);
		return orderNo;
	}

	@Override
	public Map<String, Integer> getRYBFans(Integer userId) {
		Integer inviteCode = (Integer) (userMapper.selectPersonalInformById(userId).get("InviteCode"));
		HashMap<String, Integer> datas = new HashMap<>();
		datas.put("Red", memberShipMapper.getRedFans(inviteCode));
		datas.put("Yellow", memberShipMapper.getRedFans(inviteCode));
		datas.put("Blue", memberShipMapper.getRedFans(inviteCode));
		return datas;
	}

	@Override
	public List<Map<String, Object>> getRYBFansDetails(Integer userId, String type, Integer page, Integer pageNum) {
		Integer inviteCode = (Integer) (userMapper.selectPersonalInformById(userId).get("InviteCode"));
		List<Map<String, Object>> datas = null;
		// 算页
		Integer offSet = page * pageNum;
		if ("Red".equals(type)) {
			datas = memberShipMapper.getRedFansDetails(inviteCode, offSet, pageNum);
			for (int i = 0; i < datas.size(); i++) {
				Map<String, Object> data = datas.get(i);
				Integer fromUserId = (Integer) data.get("Id");
				BigDecimal amount = memberShipMapper.getContributeAmount(fromUserId, userId);
				if (amount == null) {
					amount = BigDecimal.ZERO;
				}
				data.put("amount", amount);
			}
		} else if ("Blue".equals(type)) {
			datas = memberShipMapper.getBlueFansDetails(inviteCode, offSet, pageNum);
			for (int i = 0; i < datas.size(); i++) {
				Map<String, Object> data = datas.get(i);
				Integer fromUserId = (Integer) data.get("Id");
				BigDecimal amount = memberShipMapper.getContributeAmount(fromUserId, userId);
				if (amount == null) {
					amount = BigDecimal.ZERO;
				}
				data.put("amount", amount);
			}
		} else {
			datas = memberShipMapper.getYellowFansDetails(inviteCode, offSet, pageNum);
			for (int i = 0; i < datas.size(); i++) {
				Map<String, Object> data = datas.get(i);
				Integer fromUserId = (Integer) data.get("Id");
				BigDecimal amount = memberShipMapper.getContributeAmount(fromUserId, userId);
				if (amount == null) {
					amount = BigDecimal.ZERO;
				}
				data.put("amount", amount);
			}
		}
		return datas;
	}

	@Override
	public boolean memberUpdate(Integer userId, Integer type) throws shareBenefitException {
		if (mayNotUpdateMap.get(userId.toString()) != null) {
			return false;
		}
		Map<String, Object> inviteCodeAndTotalCost = userMapper.getInviteCodeAndTotalCostById(userId);
		if (type == 1) {// 升级到vip
			BigDecimal totalCost = (BigDecimal) inviteCodeAndTotalCost.get("TotalCost");
			if (totalCost.compareTo(new BigDecimal("3000")) == -1) {
				return false;
			}
			// 具体升级vip
			userMapper.setLev(1, 0, userId);
			tasks.presentTask(1, userId);
			return true;
		} else {
			Integer inviteCode = (Integer) inviteCodeAndTotalCost.get("InviteCode");
			// 查询他的所有下级累计积分
			List<Map<String, Object>> subordinates = userMapper.getDownInviteAndTotalCost(inviteCode);
			if (subordinates.size() == 0) {
				mayNotUpdateMap.put(userId.toString(), "false");
				return false;
			}
			List<Integer> scoreList = new ArrayList<>();
			for (int i = 0; i < subordinates.size(); i++) {
				Map<String, Object> curSubordinate = subordinates.get(i);
				Integer curInviteCode = (Integer) curSubordinate.get("InviteCode");
				BigDecimal curScore = getAccumulateScore(curInviteCode);
				scoreList.add(curScore.intValue());
			}
			if (type == 2) {
				if (ShareBenefitUtil.isProxy(scoreList, 50000, 2)) {
					// 升级为准代理
					userMapper.setLev(2, 1, userId);
					tasks.presentTask(2, userId);
					return true;
				} else {
					mayNotUpdateMap.put(userId.toString(), "false");
					return false;
				}
			} else {
				if (ShareBenefitUtil.isProxy(scoreList, 100000, 3)) {
					// 升级为代理
					userMapper.setLev(3, 0, userId);
					tasks.presentTask(2, userId);
					return true;
				} else {
					mayNotUpdateMap.put(userId.toString(), "false");
					return false;
				}
			}

		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public BigDecimal getAccumulateScore(Integer inviteCode) {
		// 递归求出某个人的累计积分
		List<Map<String, Object>> subordinates = userMapper.getDownInviteAndTotalCost(inviteCode);
		if (subordinates.size() == 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = BigDecimal.ZERO;
		for (int i = 0; i < subordinates.size(); i++) {
			Map<String, Object> curSubordinate = subordinates.get(i);
			Integer curInviteCode = (Integer) curSubordinate.get("InviteCode");
			BigDecimal curTotalCost = (BigDecimal) curSubordinate.get("TotalCost");
			result = result.add(curTotalCost).add(getAccumulateScore(curInviteCode));
		}
		return result;
	}

	@Override
	@Transactional
	public boolean transferOfMoney(BigDecimal actualMoney, Integer userId, Integer sysID) {
		Map<String, Object> passiveUserCurQuota = userMapper.selectUserQuotaForUpdate(userId);
		BigDecimal passiveUserCurElec = (BigDecimal) passiveUserCurQuota.get("RemainElecNum");
		if (passiveUserCurElec.compareTo(actualMoney) == 1) {
			// 扣钱
			BigDecimal remainElecNum = passiveUserCurElec.subtract(actualMoney);
			passiveUserCurQuota.put("RemainElecNum", remainElecNum);
			userMapper.updateUserQuota(passiveUserCurQuota);
			// 加钱
			Map<String, Object> activeUser = userMapper.getUserInfoBySysID(sysID);
			Integer activeUserId = (Integer) activeUser.get("Id");
			Map<String, Object> activeUserCurQuota = userMapper.selectUserQuotaForUpdate(activeUserId);
			BigDecimal activeUserCurElec = (BigDecimal) activeUserCurQuota.get("RemainElecNum");
			remainElecNum = activeUserCurElec.add(actualMoney);
			activeUserCurQuota.put("RemainElecNum", remainElecNum);
			userMapper.updateUserQuota(activeUserCurQuota);
			// 记录
			logMapper.insertElecRecord(userId, new Date(), actualMoney, "-", "转让 " + actualMoney + " 现金币到用户 " + sysID);
			Integer passiveUserSysID = (Integer) userMapper.selectPersonalInformById(userId).get("SysID");
			logMapper.insertElecRecord(activeUserId, new Date(), actualMoney, "+",
					"从用户 " + passiveUserSysID + " 收到 " + actualMoney + " 现金币");
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<Map<String, Object>> getPossessorPresent(Integer userId) {
		List<Map<String, Object>> datas = memberShipMapper.getPossessorPresent(userId);
		for (int i = 0; i < datas.size(); i++) {
			Date createTime = (Date) datas.get(i).get("CreateTime");
			if (DateUtil.isLatestWeek(createTime, new Date())) {
				datas.get(i).put("canUse", 0);
			} else {
				datas.get(i).put("canUse", 1);
			}
		}
		return datas;
	}

	@Override
	public List<Map<String, Integer>> getAlreadyGivePresent(Integer userId) {
		return memberShipMapper.getAlreadyGivePresent(userId);
	}

	@Override
	@Transactional
	public String givePresentPromptly(Integer sendHeadId, Integer activeUserId, Integer passiveUserId) {
		Integer lev = memberShipMapper.getPresentById(sendHeadId);
		if (lev == null) {
			return "1";
		}
		// 该人等级
		Integer activeUserLev = (Integer) userMapper.selectPersonalInformById(passiveUserId).get("Lev");
		if (activeUserLev != 0) {
			return "2";// 必须给免费会员
		}
		// 修改赠送状态
		if (memberShipMapper.changePresentStatusToOne(sendHeadId) == 1) {
			// 修改个人积分红包和累计分值
			Map<String, Object> passiveUserCurQuota = userMapper.selectUserQuotaForUpdate(passiveUserId);
			BigDecimal addBigDecimal = new BigDecimal("3000.00");
			BigDecimal remainVIPAmount = ((BigDecimal) passiveUserCurQuota.get("RemainVIPAmount")).add(addBigDecimal);
			BigDecimal coupon = ((BigDecimal) passiveUserCurQuota.get("Coupon")).add(addBigDecimal);
			BigDecimal totalCost = ((BigDecimal) passiveUserCurQuota.get("TotalCost")).add(addBigDecimal);
			passiveUserCurQuota.put("RemainVIPAmount", remainVIPAmount);
			passiveUserCurQuota.put("Coupon", coupon);
			passiveUserCurQuota.put("TotalCost", totalCost);
			userMapper.updateUserQuota(passiveUserCurQuota);
			// 设置等级
			userMapper.setLev(1, 0, passiveUserId);
			// 记录赠送
			logMapper.insertCouponRecord(passiveUserId, new Date(), addBigDecimal, "+", "获得VIP赠送");
			logMapper.insertVipRemainRecord(passiveUserId, new Date(), addBigDecimal, "+", "获得VIP赠送");
			// 4个数据
			memberShipMapper.insertSendHeadRecord(sendHeadId, passiveUserId, lev, new Date());
			return "0";
		} else {
			return "1";
		}

	}

	@Override
	@Transactional
	public String splitStream(Integer fromUserId, Integer toUserId, Integer type) {

		Integer toUserLev = (Integer) userMapper.selectPersonalInformById(toUserId).get("Lev");
		if (toUserLev != 0) {
			return "2";// 必须给免费会员
		}
		if (type == 1) {
			// 分流三千
			BigDecimal quotaCache = new BigDecimal("3000.00");
			Map<String, Object> fromUserCurQuota = userMapper.selectUserQuotaForUpdate(fromUserId);
			BigDecimal remainStream = (BigDecimal) fromUserCurQuota.get("RemainStream");
			if (remainStream.compareTo(quotaCache) < 0) {
				return "1";
			} else {
				// 减去分流币
				fromUserCurQuota.put("RemainStream", remainStream.subtract(quotaCache));
				userMapper.updateUserQuota(fromUserCurQuota);
				memberShipMapper.insertSplitStreamRecord(new Date(), fromUserId, toUserId, quotaCache);
				// 修改个人积分红包和累计分值
				Map<String, Object> toUserCurQuota = userMapper.selectUserQuotaForUpdate(toUserId);
				BigDecimal remainVIPAmount = ((BigDecimal) toUserCurQuota.get("RemainVIPAmount")).add(quotaCache);
				BigDecimal coupon = ((BigDecimal) toUserCurQuota.get("Coupon")).add(quotaCache);
				BigDecimal totalCost = ((BigDecimal) toUserCurQuota.get("TotalCost")).add(quotaCache);
				toUserCurQuota.put("RemainVIPAmount", remainVIPAmount);
				toUserCurQuota.put("Coupon", coupon);
				toUserCurQuota.put("TotalCost", totalCost);
				userMapper.updateUserQuota(toUserCurQuota);
				// 设置等级
				userMapper.setLev(1, 0, toUserId);
				// 分流记录
				logMapper.insertCouponRecord(toUserId, new Date(), quotaCache, "+", "获得3000分流");
				logMapper.insertVipRemainRecord(toUserId, new Date(), quotaCache, "+", "获得3000分流");
				// 分流分润
				tasks.shareBenitTask(3, toUserId, fromUserId, "分流", quotaCache);
				// 等级产生赠送名额
				tasks.presentTask(1, toUserId);
				return "0";
			}
		} else {
			// 分流5万
			BigDecimal quotaCache = new BigDecimal("50000.00");
			Map<String, Object> fromUserCurQuota = userMapper.selectUserQuotaForUpdate(fromUserId);
			BigDecimal remainStream = (BigDecimal) fromUserCurQuota.get("RemainStream");
			if (remainStream.compareTo(quotaCache) < 0) {
				return "1";
			} else {
				// 减去分流币
				fromUserCurQuota.put("RemainStream", remainStream.subtract(quotaCache));
				userMapper.updateUserQuota(fromUserCurQuota);
				// 分流记录
				memberShipMapper.insertSplitStreamRecord(new Date(), fromUserId, toUserId, quotaCache);
				quotaCache = new BigDecimal("20000.00");
				// 修改个人积分红包和累计分值
				Map<String, Object> toUserCurQuota = userMapper.selectUserQuotaForUpdate(toUserId);
				BigDecimal remainVIPAmount = ((BigDecimal) toUserCurQuota.get("RemainVIPAmount")).add(quotaCache);
				BigDecimal coupon = ((BigDecimal) toUserCurQuota.get("Coupon")).add(quotaCache);
				BigDecimal totalCost = ((BigDecimal) toUserCurQuota.get("TotalCost")).add(quotaCache);
				toUserCurQuota.put("RemainVIPAmount", remainVIPAmount);
				toUserCurQuota.put("Coupon", coupon);
				toUserCurQuota.put("TotalCost", totalCost);
				userMapper.updateUserQuota(toUserCurQuota);
				// 设置等级
				userMapper.setLev(2, 1, toUserId);
				logMapper.insertCouponRecord(toUserId, new Date(), quotaCache, "+", "获得50000分流");
				logMapper.insertVipRemainRecord(toUserId, new Date(), quotaCache, "+", "获得50000分流");
				// 分流分润
				tasks.shareBenitTask(3, toUserId, fromUserId, "分流", quotaCache);
				// 等级产生赠送名额
				tasks.presentTask(2, toUserId);
				return "0";
			}
		}
	}

	@Override
	public List<Map<String, Object>> getSplitStreamRecord(Integer userId) {
		List<Map<String, Object>> splitStreamRecord = memberShipMapper.selectSplitStreamRecord(userId);
		Integer splitStreamRecordSize = splitStreamRecord.size();
		for (int i = 0; i < splitStreamRecordSize; i++) {
			Date createTime = (Date) splitStreamRecord.get(i).get("CreateTime");
			splitStreamRecord.get(i).put("CreateTime", DateUtil.DateToStr(createTime));
		}
		return splitStreamRecord;
	}
}

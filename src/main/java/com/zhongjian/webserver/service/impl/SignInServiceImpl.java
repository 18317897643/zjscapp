package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.mapper.SignInMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.pojo.SigninAward;
import com.zhongjian.webserver.service.SignInService;

@Service
public class SignInServiceImpl implements SignInService {

	@Autowired
	private SignInMapper signInMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional
	public boolean Signing(Integer UserId) throws ParseException {
		// 往tb_signintermediate录入中间数据
		// 中间表不存在数据
		Date lastSignTime = signInMapper.getLastSignTimeByUserId(UserId);
		if (lastSignTime == null) {
			signInMapper.addSigninTermediateByUserId(UserId, new Date(), 1);
			signInMapper.addSignRecord(UserId, new Date());
			//领取奖励
			BigDecimal addCoupon = new BigDecimal("10.00");
			Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
			BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).add(addCoupon);
			curQuota.put("Coupon", remainCoupon);
			userMapper.updateUserQuota(curQuota);
		} else {
			Calendar dateCalendar = Calendar.getInstance();
			dateCalendar.setTime(lastSignTime);
			// 计算差的月数是否大于0
			int year = dateCalendar.get(Calendar.YEAR);
			int month = dateCalendar.get(Calendar.MONTH) + 1;
			YearMonth yearMonth = YearMonth.of(year, month);
			YearMonth yearMonthNow = YearMonth.now();
			int monthDifference = yearMonthNow.compareTo(yearMonth);
			// 计算差的天数
			Date curretDateTime = new Date();
			Calendar curretDateTimeCalendar = Calendar.getInstance();
			curretDateTimeCalendar.setTime(curretDateTime);
			int curretYear = curretDateTimeCalendar.get(Calendar.YEAR);
			int curretMonth = curretDateTimeCalendar.get(Calendar.MONTH) + 1;
			int curretDayOfMonth = curretDateTimeCalendar.get(Calendar.DAY_OF_MONTH);
			String curretDateStr = curretYear + "-" + curretMonth + "-" + curretDayOfMonth;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate = format.parse(curretDateStr);
			long dayDifference = (currentDate.getTime() - dateCalendar.getTimeInMillis()) / (1000 * 3600 * 24);
			if (dayDifference == 0) {
				// 最近签到时间就是今天不给签
				return false;
			}
			// 往tb_sign表中加入具体签到数据
			signInMapper.addSignRecord(UserId, new Date());
			//领取奖励
			BigDecimal addCoupon = new BigDecimal("10.00");
			Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
			BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).add(addCoupon);
			curQuota.put("Coupon", remainCoupon);
			userMapper.updateUserQuota(curQuota);
			if (monthDifference == 0 && dayDifference == 1) {
				String currentYearMonth = null;
				if (curretMonth < 10) {
					currentYearMonth = curretYear + "-0" + curretMonth;
				} else {
					currentYearMonth = curretYear + "-" + curretMonth;
				}

				SigninAward signinAward = signInMapper.getAwardsByUserId(UserId, currentYearMonth);
				if (signinAward == null) {
					// 插入一条数据
					signInMapper.addSigninAward(UserId, currentDate);
				}
				// 更新tb_tb_signintermediate
				signInMapper.updateSigninTermediateByUserId(UserId, currentDate);
				// 得到连续天数判断是否达标
				Integer continueDay = signInMapper.getContinueDayByUserId(UserId);

				int daysOfMonth = curretDateTimeCalendar.getActualMaximum(Calendar.DATE);
				if (continueDay >= 7 && continueDay < 14) {
					if (signinAward.getSevendaysaward() == 0) {
						signInMapper.updateSigninAwardSeven(UserId, currentYearMonth, 1,0);
					}
				} else if (continueDay >= 14 && continueDay < daysOfMonth) {
					if (signinAward.getSevendaysaward() == 0) {
						signInMapper.updateSigninAwardSeven(UserId, currentYearMonth, 1,0);
					}
					if (signinAward.getFourteendaysaward() == 0) {
						signInMapper.updateSigninAwardFourteen(UserId, currentYearMonth, 1,0);
					}
				} else if (continueDay == daysOfMonth) {
					if (signinAward.getSevendaysaward() == 0) {
						signInMapper.updateSigninAwardSeven(UserId, currentYearMonth, 1,0);
					}
					if (signinAward.getFourteendaysaward() == 0) {
						signInMapper.updateSigninAwardFourteen(UserId, currentYearMonth, 1,0);
					}
					if (signinAward.getThirtydaysaward() == 0) {
						signInMapper.updateSigninAwardThirty(UserId, currentYearMonth, 1,0);
					}
				}
			} else {
				// 中间表更新，置连续天数为1
				signInMapper.reSetContinueDayByUserId(UserId, currentDate);
			}
		}
		return true;
	}

	@Override
	@Transactional
	public boolean markAreadyAward(Integer UserId, String awardType) {
		String currentYearMonth = this.getCurrentYearMonth();
		if (("seven").equals(awardType)) {
			if (signInMapper.updateSigninAwardSeven(UserId, currentYearMonth, -1,1) == 1) {
				//领取7天奖励
				BigDecimal addCoupon = new BigDecimal("15.00");
				Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
				BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).add(addCoupon);
				curQuota.put("Coupon", remainCoupon);
				userMapper.updateUserQuota(curQuota);
			}else {
				return false;
			}
		} else if (("fourteen").equals(awardType)) {
			if (signInMapper.updateSigninAwardFourteen(UserId, currentYearMonth, -1,1) == 1) {
				//领取14天奖励
				BigDecimal addCoupon = new BigDecimal("20.00");
				Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
				BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).add(addCoupon);
				curQuota.put("Coupon", remainCoupon);
				userMapper.updateUserQuota(curQuota);
			}else{
				return false;
			}
		} else if (("thirty").equals(awardType)) {
			if (signInMapper.updateSigninAwardThirty(UserId, currentYearMonth, -1,1) == 1) {
				//领取月奖励
				BigDecimal addCoupon = new BigDecimal("50.00");
				Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
				BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).add(addCoupon);
				curQuota.put("Coupon", remainCoupon);
				userMapper.updateUserQuota(curQuota);
			}else{
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	@Override
	public HashMap<String, Object> initSignData(Integer UserId) {
		HashMap<String, Object> dataMap = new HashMap<>();
		String currentYearMonth = this.getCurrentYearMonth();
		List<java.sql.Date> signData = signInMapper.getTheMonthSignData(UserId, currentYearMonth);
		SigninAward signinAward = signInMapper.getAwardsByUserId(UserId, currentYearMonth);
		dataMap.put("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		dataMap.put("signData", signData);
		dataMap.put("signinAward", signinAward);
		return dataMap;
	}

	@Override
	public void deleteDatedSignInData() {
		signInMapper.deleteDatedSignInData(this.getCurrentYearMonth());
	}

	@Override
	public void deleteDatedSignAwardData() {
		signInMapper.deleteDatedSignAwardData(this.getCurrentYearMonth());
	}

	String getCurrentYearMonth() {
		Date curretDateTime = new Date();
		Calendar curretDateTimeCalendar = Calendar.getInstance();
		curretDateTimeCalendar.setTime(curretDateTime);
		int curretYear = curretDateTimeCalendar.get(Calendar.YEAR);
		int curretMonth = curretDateTimeCalendar.get(Calendar.MONTH) + 1;
		String currentYearMonth = null;
		if (curretMonth < 10) {
			currentYearMonth = curretYear + "-0" + curretMonth;
		} else {
			currentYearMonth = curretYear + "-" + curretMonth;
		}
		return currentYearMonth;
	}

}

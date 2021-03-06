package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.zhongjian.webserver.alipay.AlipayConfig;
import com.zhongjian.webserver.common.DateUtil;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.RandomUtil;
import com.zhongjian.webserver.component.AsyncTasks;
import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderHeadEXDto;
import com.zhongjian.webserver.dto.OrderLineDto;
import com.zhongjian.webserver.mapper.LogMapper;
import com.zhongjian.webserver.mapper.MemberShipMapper;
import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.service.AddressManagerService;
import com.zhongjian.webserver.service.OrderHandleService;

@Service
public class OrderHandleServiceImpl implements OrderHandleService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private LogMapper logMapper;

	@Autowired
	private MemberShipMapper memberShipMapper;

	@Autowired
	private AsyncTasks tasks;

	@Autowired
	private AlipayConfig alipayConfig;

	@Autowired
	private AddressManagerService addressManagerService;

	@Override
	@Transactional
	public HashMap<String, Object> createOrder(List<OrderHeadDto> orderHeads, Integer UserId) {
		String theCurrentOrderNoCollectionName = "CB" + RandomUtil.getFlowNumber();
		ArrayList<String> theCurrentOrderNoCollections = new ArrayList<>();
		ArrayList<BigDecimal> TotalEveryAmountCoList = new ArrayList<>();
		BigDecimal TotalRealPayCo = new BigDecimal("0.00");
		BigDecimal TotalCouponCo = new BigDecimal("0.00");
		BigDecimal TotalElecNumCo = new BigDecimal("0.00");
		BigDecimal TotalPointNumCo = new BigDecimal("0.00");
		BigDecimal TotalVIPRemainCo = new BigDecimal("0.00");
		TotalEveryAmountCoList.add(TotalRealPayCo);
		TotalEveryAmountCoList.add(1, TotalCouponCo);
		TotalEveryAmountCoList.add(2, TotalElecNumCo);
		TotalEveryAmountCoList.add(3, TotalPointNumCo);
		TotalEveryAmountCoList.add(4, TotalVIPRemainCo);
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
				TotalEveryAmountCoList.set(0, TotalEveryAmountCoList.get(0).add(e.getRealPay()));
				TotalEveryAmountCoList.set(1, TotalEveryAmountCoList.get(1).add(e.getUseCoupon()));
				TotalEveryAmountCoList.set(2, TotalEveryAmountCoList.get(2).add(e.getUseElecNum()));
				TotalEveryAmountCoList.set(3, TotalEveryAmountCoList.get(3).add(e.getUsePointNum()));
				TotalEveryAmountCoList.set(4, TotalEveryAmountCoList.get(4).add(e.getUseVIPRemainNum()));
				e.setCreateTime(new Date());
				String theCurrentOrderNo = "B" + RandomUtil.getFlowNumber();
				theCurrentOrderNoCollections.add(theCurrentOrderNo);
				e.setOrderNo(theCurrentOrderNo);
				e.setUserId(UserId);
				// 如果插入成功返回orderId
				orderMapper.insertOrderHead(e);
				Integer orderId = e.getId();
				System.out.println(orderId);
				List<OrderLineDto> orderLines = e.getOrderLines();
				// 该单总分值
				List<Integer> score = new ArrayList<>();
				score.add(0);
				orderLines.forEach(f -> {
					Integer productNum = f.getProductNum();
					// 查询该规格的分值和价格
					Map<String, Object> specElecNumAndPrice = orderMapper.getSpecElecNumAndPriceById(f.getSpecId());
					// 分值计算最终录入订单
					Integer specElecNum = (Integer) specElecNumAndPrice.get("ElecNum");
					Integer thisTimeScore = productNum * specElecNum;
					score.set(0, score.get(0) + thisTimeScore);
					BigDecimal price = (BigDecimal) specElecNumAndPrice.get("Price");
					BigDecimal amount = price.multiply(new BigDecimal(productNum));
					f.setPrice(price);
					f.setAmount(amount);
					f.setOrderId(orderId);
					orderMapper.insertOrderLine(f);
				});
				// 分值录入
				orderMapper.updateOrderHeadScore(score.get(0), orderId);
			}
		});
		// 记录父订单
		String theCurrentOrderNoCollectionsStr = "";
		for (int i = 0; i < theCurrentOrderNoCollections.size(); i++) {
			if (theCurrentOrderNoCollections.size() - 1 == i) {
				theCurrentOrderNoCollectionsStr = theCurrentOrderNoCollectionsStr + theCurrentOrderNoCollections.get(i);
			}
			theCurrentOrderNoCollectionsStr = theCurrentOrderNoCollectionsStr + theCurrentOrderNoCollections.get(i)
					+ "|";
		}
		BigDecimal totalNotRealPayCo = TotalEveryAmountCoList.get(1).add(TotalEveryAmountCoList.get(2))
				.add(TotalEveryAmountCoList.get(3)).add(TotalEveryAmountCoList.get(4));
		orderMapper.insertOrderHeadCo(theCurrentOrderNoCollectionName, theCurrentOrderNoCollectionsStr,
				TotalEveryAmountCoList.get(0), totalNotRealPayCo, new Date(), UserId);
		HashMap<String, Object> result = new HashMap<>();
		result.put("orderNoCollectionName", theCurrentOrderNoCollectionsStr);
		result.put("totalRealPayCo", TotalEveryAmountCoList.get(0));
		result.put("totalNotRealPayCo", totalNotRealPayCo);
		return result;
	}

	@Override
	@Transactional
	public boolean syncHandleOrder(String orderNo) {
		if (orderNo.startsWith("CB")) {
			Map<String, Object> map = orderMapper.getDetailsFormorderheadC(orderNo);
			if (orderMapper.updateOrderHeadCoCur() == 1) {
				String orderNoCName = (String) map.get("OrderNoC");
				String[] orderNoC = orderNoCName.split("|");
				int orderNoClenth = orderNoC.length;
				for (int i = 0; i < orderNoClenth; i++) {
					// 查看订单积分，现金币，购物币，红包使用情况去扣
					// 需要扣除的
					Map<String, Object> needSubMap = orderMapper.getNeedSubDetailsOfOrderHead(orderNoC[i]);
					Integer userId = (Integer) needSubMap.get("UserId");
					BigDecimal useCoupon = (BigDecimal) needSubMap.get("UseCoupon");
					BigDecimal usePointNum = (BigDecimal) needSubMap.get("UsePointNum");
					BigDecimal useElecNum = (BigDecimal) needSubMap.get("UseElecNum");
					BigDecimal useVIPRemainNum = (BigDecimal) needSubMap.get("UseVIPRemainNum");
					Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(userId);
					BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).subtract(useCoupon);
					BigDecimal remainPoints = ((BigDecimal) curQuota.get("RemainPoints")).subtract(usePointNum);
					BigDecimal remainElecNum = ((BigDecimal) curQuota.get("RemainElecNum")).subtract(useElecNum);
					BigDecimal remainVIPAmount = ((BigDecimal) curQuota.get("RemainVIPAmount"))
							.subtract(useVIPRemainNum);
					curQuota.put("Coupon", remainCoupon);
					curQuota.put("RemainPoints", remainPoints);
					curQuota.put("RemainElecNum", remainElecNum);
					curQuota.put("RemainVIPAmount", remainVIPAmount);
					curQuota.put("UserId", userId);
					userMapper.updateUserQuota(curQuota);
					// 记录日志
					Date curDate = new Date();
					if (useCoupon.compareTo(BigDecimal.ZERO) == 1) {
						logMapper.insertCouponRecord(userId, curDate, useCoupon, "-", "购买商品，订单号：" + orderNoC[i]);
					}
					if (useElecNum.compareTo(BigDecimal.ZERO) == 1) {
						logMapper.insertElecRecord(userId, curDate, useElecNum, "-", "购买商品，订单号：" + orderNoC[i]);
					}
					if (usePointNum.compareTo(BigDecimal.ZERO) == 1) {
						logMapper.insertPointRecord(userId, curDate, usePointNum, "-", "购买商品，订单号：" + orderNoC[i]);
					}
					if (remainVIPAmount.compareTo(BigDecimal.ZERO) == 1) {
						logMapper.insertVipRemainRecord(userId, curDate, useVIPRemainNum, "-",
								"购买商品，订单号：" + orderNoC[i]);
					}
					// 更改订单状态
					orderMapper.updateOrderHeadStatus(0, orderNoC[i]);
				}
				return true;
			} else {
				return false;
			}
		} else if (orderNo.startsWith("B")) {
			if (orderMapper.updateOrderHeadStatusToWP(orderNo) == 1) {
				// 查看订单积分，现金币，购物币，红包使用情况去扣
				// 需要扣除的
				Map<String, Object> needSubMap = orderMapper.getNeedSubDetailsOfOrderHead(orderNo);
				Integer userId = (Integer) needSubMap.get("UserId");
				BigDecimal useCoupon = (BigDecimal) needSubMap.get("UseCoupon");
				BigDecimal usePointNum = (BigDecimal) needSubMap.get("UsePointNum");
				BigDecimal useElecNum = (BigDecimal) needSubMap.get("UseElecNum");
				BigDecimal useVIPRemainNum = (BigDecimal) needSubMap.get("UseVIPRemainNum");
				Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(userId);
				BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).subtract(useCoupon);
				BigDecimal remainPoints = ((BigDecimal) curQuota.get("RemainPoints")).subtract(usePointNum);
				BigDecimal remainElecNum = ((BigDecimal) curQuota.get("RemainElecNum")).subtract(useElecNum);
				BigDecimal remainVIPAmount = ((BigDecimal) curQuota.get("RemainVIPAmount")).subtract(useVIPRemainNum);
				curQuota.put("Coupon", remainCoupon);
				curQuota.put("RemainPoints", remainPoints);
				curQuota.put("RemainElecNum", remainElecNum);
				curQuota.put("RemainVIPAmount", remainVIPAmount);
				curQuota.put("UserId", userId);
				userMapper.updateUserQuota(curQuota);
				// 记录日志
				Date curDate = new Date();
				if (useCoupon.compareTo(BigDecimal.ZERO) == 1) {
					logMapper.insertCouponRecord(userId, curDate, useCoupon, "-", "购买商品，订单号：" + orderNo);
				}
				if (useElecNum.compareTo(BigDecimal.ZERO) == 1) {
					logMapper.insertElecRecord(userId, curDate, useElecNum, "-", "购买商品，订单号：" + orderNo);
				}
				if (usePointNum.compareTo(BigDecimal.ZERO) == 1) {
					logMapper.insertPointRecord(userId, curDate, usePointNum, "-", "购买商品，订单号：" + orderNo);
				}
				if (remainVIPAmount.compareTo(BigDecimal.ZERO) == 1) {
					logMapper.insertVipRemainRecord(userId, curDate, useVIPRemainNum, "-", "购买商品，订单号：" + orderNo);
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean asyncHandleOrder(String orderNo, String totalAmount, String seller_id, String app_id) {
		// 查询对应的子订单信息
		if (!seller_id.equals(alipayConfig.BUSINESSID) || !app_id.equals(alipayConfig.APPID)) {
			return false;
		}
		if (orderNo.startsWith("CB")) {
			Map<String, Object> map = orderMapper.getDetailsFormorderheadC(orderNo);
			if (!map.get("TolAmount").toString().equals(totalAmount)) {
				LoggingUtil.w("订单实付额为" + map.get("TolAmount").toString() + "，支付宝实付" + totalAmount + "总金额不对！！！！");
				return false;
			} else {
				if (orderMapper.updateOrderHeadCoCur() == 1) {
					String orderNoCName = (String) map.get("OrderNoC");

					String[] orderNoC = orderNoCName.split("|");
					int orderNoClenth = orderNoC.length;
					for (int i = 0; i < orderNoClenth; i++) {
						// 查看订单积分，现金币，购物币，红包使用情况去扣
						// 需要扣除的
						Map<String, Object> needSubMap = orderMapper.getNeedSubDetailsOfOrderHead(orderNoC[i]);
						Integer userId = (Integer) needSubMap.get("UserId");
						BigDecimal useCoupon = (BigDecimal) needSubMap.get("UseCoupon");
						BigDecimal usePointNum = (BigDecimal) needSubMap.get("UsePointNum");
						BigDecimal useElecNum = (BigDecimal) needSubMap.get("UseElecNum");
						BigDecimal useVIPRemainNum = (BigDecimal) needSubMap.get("UseVIPRemainNum");
						Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(userId);
						BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).subtract(useCoupon);
						BigDecimal remainPoints = ((BigDecimal) curQuota.get("RemainPoints")).subtract(usePointNum);
						BigDecimal remainElecNum = ((BigDecimal) curQuota.get("RemainElecNum")).subtract(useElecNum);
						BigDecimal remainVIPAmount = ((BigDecimal) curQuota.get("RemainVIPAmount"))
								.subtract(useVIPRemainNum);
						curQuota.put("Coupon", remainCoupon);
						curQuota.put("RemainPoints", remainPoints);
						curQuota.put("RemainElecNum", remainElecNum);
						curQuota.put("RemainVIPAmount", remainVIPAmount);
						curQuota.put("UserId", userId);
						userMapper.updateUserQuota(curQuota);
						// 记录日志
						Date curDate = new Date();
						if (useCoupon.compareTo(BigDecimal.ZERO) == 1) {
							logMapper.insertCouponRecord(userId, curDate, useCoupon, "-", "购买商品，订单号：" + orderNoC[i]);
						}
						if (useElecNum.compareTo(BigDecimal.ZERO) == 1) {
							logMapper.insertElecRecord(userId, curDate, useElecNum, "-", "购买商品，订单号：" + orderNoC[i]);
						}
						if (usePointNum.compareTo(BigDecimal.ZERO) == 1) {
							logMapper.insertPointRecord(userId, curDate, usePointNum, "-", "购买商品，订单号：" + orderNoC[i]);
						}
						if (remainVIPAmount.compareTo(BigDecimal.ZERO) == 1) {
							logMapper.insertVipRemainRecord(userId, curDate, useVIPRemainNum, "-",
									"购买商品，订单号：" + orderNoC[i]);
						}
						// 更改订单状态
						orderMapper.updateOrderHeadStatus(0, orderNoC[i]);
					}
				}
				return true;
			}

		} else if (orderNo.startsWith("B")) {
			if (orderMapper.updateOrderHeadStatusToWP(orderNo) == 1) {
				// 查看订单积分，现金币，购物币，红包使用情况去扣
				// 需要扣除的
				Map<String, Object> needSubMap = orderMapper.getNeedSubDetailsOfOrderHead(orderNo);
				Integer userId = (Integer) needSubMap.get("UserId");
				BigDecimal useCoupon = (BigDecimal) needSubMap.get("UseCoupon");
				BigDecimal usePointNum = (BigDecimal) needSubMap.get("UsePointNum");
				BigDecimal useElecNum = (BigDecimal) needSubMap.get("UseElecNum");
				BigDecimal useVIPRemainNum = (BigDecimal) needSubMap.get("UseVIPRemainNum");
				Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(userId);
				BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).subtract(useCoupon);
				BigDecimal remainPoints = ((BigDecimal) curQuota.get("RemainPoints")).subtract(usePointNum);
				BigDecimal remainElecNum = ((BigDecimal) curQuota.get("RemainElecNum")).subtract(useElecNum);
				BigDecimal remainVIPAmount = ((BigDecimal) curQuota.get("RemainVIPAmount")).subtract(useVIPRemainNum);
				curQuota.put("Coupon", remainCoupon);
				curQuota.put("RemainPoints", remainPoints);
				curQuota.put("RemainElecNum", remainElecNum);
				curQuota.put("RemainVIPAmount", remainVIPAmount);
				curQuota.put("UserId", userId);
				userMapper.updateUserQuota(curQuota);
				// 记录日志
				Date curDate = new Date();
				if (useCoupon.compareTo(BigDecimal.ZERO) == 1) {
					logMapper.insertCouponRecord(userId, curDate, useCoupon, "-", "购买商品，订单号：" + orderNo);
				}
				if (useElecNum.compareTo(BigDecimal.ZERO) == 1) {
					logMapper.insertElecRecord(userId, curDate, useElecNum, "-", "购买商品，订单号：" + orderNo);
				}
				if (usePointNum.compareTo(BigDecimal.ZERO) == 1) {
					logMapper.insertPointRecord(userId, curDate, usePointNum, "-", "购买商品，订单号：" + orderNo);
				}
				if (remainVIPAmount.compareTo(BigDecimal.ZERO) == 1) {
					logMapper.insertVipRemainRecord(userId, curDate, useVIPRemainNum, "-", "购买商品，订单号：" + orderNo);
				}
			}
			return true;
		} else if (orderNo.startsWith("VO")) {
			if (memberShipMapper.changeVipOrderToPaid(orderNo) == 1) {
				Map<String, Object> data = memberShipMapper.selectViporderByOrderNo(orderNo);
				Integer lev = (Integer) data.get("Lev");
				BigDecimal useMoney = (BigDecimal) data.get("TolAmout");
				Integer UserId = (Integer) data.get("UserId");
				// 积分红包个人分值的变化
				Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
				BigDecimal remainVIPAmount = ((BigDecimal) curQuota.get("RemainVIPAmount")).add(useMoney);
				BigDecimal remainCoupon = ((BigDecimal) curQuota.get("Coupon")).add(useMoney);
				BigDecimal remianTotalCost = ((BigDecimal) curQuota.get("TotalCost")).add(useMoney);
				curQuota.put("RemainVIPAmount", remainVIPAmount);
				curQuota.put("Coupon", remainCoupon);
				curQuota.put("TotalCost", remianTotalCost);
				userMapper.updateUserQuota(curQuota);
				// 升级vip gc
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
				logMapper.insertCouponRecord(UserId, new Date(), useMoney, "+", memo);
				logMapper.insertVipRemainRecord(UserId, new Date(), useMoney, "+", memo);
				// 分润
				tasks.shareBenitTask(1, UserId, 0, "购买会员", useMoney);
			}
			return true;
		} else if (orderNo.startsWith("CZ"))

		{
			// 现金币到账
			if (memberShipMapper.changeCOrderToPaid(orderNo) == 1) {
				Map<String, Object> data = memberShipMapper.selectCOrderByOrderNo(orderNo);
				BigDecimal amount = (BigDecimal) data.get("Amount");
				Integer UserId = (Integer) data.get("UserId");
				Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(UserId);
				BigDecimal remainElecNum = ((BigDecimal) curQuota.get("RemainElecNum")).add(amount);
				curQuota.put("RemainElecNum", remainElecNum);
				userMapper.updateUserQuota(curQuota);
				// 充值记录
				logMapper.insertElecRecord(UserId, new Date(), amount, "+", "现金币充值");
			}

		} else {
			return false;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String createAliSignature(String out_trade_no, String totalAmount) throws AlipayApiException {
		Map<String, String> orderMap = new LinkedHashMap<String, String>(); // 订单实体
		/****** 2.商品参数封装开始 *****/ // 手机端用
		// 商户订单号，商户网站订单系统中唯一订单号，必填
		orderMap.put("out_trade_no", out_trade_no);
		// 订单名称，必填
		orderMap.put("subject", "众健商城订单");
		// 付款金额，必填
		orderMap.put("total_amount", totalAmount);
		// 商品描述，可空
		orderMap.put("body", "本次订单花费" + totalAmount + "元");
		// 超时时间 可空
		orderMap.put("timeout_express", "30m");
		// 销售产品码 必填
		orderMap.put("product_code", "QUICK_WAP_PAY");
		// 实例化客户端
		AlipayClient client = new DefaultAlipayClient(alipayConfig.URL, alipayConfig.APPID,
				alipayConfig.RSA_PRIVATE_KEY, alipayConfig.FORMAT, alipayConfig.CHARSET, alipayConfig.ALIPAY_PUBLIC_KEY,
				alipayConfig.SIGNTYPE);
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest ali_request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setPassbackParams(URLEncoder.encode((String) orderMap.get("body").toString()));
		; // 描述信息 添加附加数据
		model.setBody(orderMap.get("body")); // 商品信息
		model.setSubject(orderMap.get("subject")); // 商品名称
		model.setOutTradeNo(orderMap.get("out_trade_no")); // 商户订单号(自动生成)
		model.setTimeoutExpress(orderMap.get("timeout_express")); // 交易超时时间
		model.setTotalAmount(orderMap.get("total_amount")); // 支付金额
		model.setProductCode(orderMap.get("product_code")); // 销售产品码
		model.setSellerId(alipayConfig.BUSINESSID); // 商家id
		ali_request.setBizModel(model);
		ali_request.setNotifyUrl(alipayConfig.notify_url); // 回调地址
		AlipayTradeAppPayResponse response = null;
		response = client.sdkExecute(ali_request);
		String orderString = response.getBody();
		return orderString;
	}

	@Override
	public Integer getUserIdByOrderC(String orderNoC) {
		return orderMapper.getUserIdByOrderC(orderNoC);
	}

	@Override
	public Integer getUserIdByOrder(String orderNo) {
		Integer userId = orderMapper.getUserIdByOrder(orderNo);
		return userId;
	}

	@Override
	public void cancelOrder(String orderNo) {
		orderMapper.updateOrderHeadStatus(-2, orderNo);

	}

	@Override
	@Transactional
	public void confirmOrder(String orderNo) {
		// 改状态（防止重复确认）
		if (orderMapper.updateOrderHeadStatusToWC(orderNo) == 1) {
			// 分润
			Map<String, Object> orderMap = orderMapper.getOrderDetailsByOrderNo(orderNo);
			Integer userId = (Integer) orderMap.get("UserId");
			BigDecimal useCoupon = (BigDecimal) orderMap.get("UseCoupon");
			BigDecimal useVIPRemainNum = (BigDecimal) orderMap.get("UseVIPRemainNum");
			Integer score = (Integer) orderMap.get("Score");
			BigDecimal bigDecimalScore = new BigDecimal(score);
			BigDecimal ElecNum = bigDecimalScore.subtract(useCoupon).subtract(useVIPRemainNum);
			tasks.shareBenitTask(1, userId, 0, "订单消费", ElecNum);
			// 累计分值增加
			Map<String, Object> curQuota = userMapper.selectUserQuotaForUpdate(userId);
			BigDecimal remianTotalCost = ((BigDecimal) curQuota.get("TotalCost")).add(bigDecimalScore);
			curQuota.put("TotalCost", remianTotalCost);
			userMapper.updateUserQuota(curQuota);
			// 把saleNum增加

		}
	}

	@Override
	public void autoCancelOrder() {
		// 自动取消两天未支付订单
		orderMapper.autoCancelOrder(DateUtil.DateToStr(new Date()), 172800);
	}

	@Override
	@Transactional
	public void autoConfirmOrder() {
		List<String> orderNos = orderMapper.getWROrderNo(DateUtil.DateToStr(new Date()), 604800);
		for (int i = 0; i < orderNos.size(); i++) {
			confirmOrder(orderNos.get(i));
		}
	}

	@Override
	public List<OrderHeadDto> handleOrderHeadDtoByAdressId(OrderHeadEXDto orderHeadEXDto) {
		Integer addressId = orderHeadEXDto.getAdrressId();
		// 根据地址Id查询地址信息
		Map<String, Object> address = addressManagerService.getAddressById(addressId);
		if (address == null) {
			return null;
		}
		List<OrderHeadDto> orderHeadDtos = orderHeadEXDto.getOrderHeads();
		Integer orderHeadDtosSize = orderHeadDtos.size();
		for (int i = 0; i < orderHeadDtosSize; i++) {
			OrderHeadDto orderHeadDto = orderHeadDtos.get(i);
			orderHeadDto.setAddress((String) address.get("DetailAddress"));
			orderHeadDto.setCityId((Integer) address.get("CityId"));
			orderHeadDto.setCountryId(0);
			orderHeadDto.setProvinceId((Integer) address.get("ProvinceId"));
			orderHeadDto.setRegionId((Integer) address.get("RegionId"));
			orderHeadDto.setReceiverName((String) address.get("Name"));
			orderHeadDto.setReceiverPhone((String) address.get("Phone"));
		}
		return orderHeadDtos;
	}

	@Override
	public Map<String, BigDecimal> getMoneyUserOfOrderhead(String orderNo) {
		return orderMapper.getMoneyUseOfOrderhead(orderNo);
	}
}

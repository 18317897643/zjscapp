package com.zhongjian.webserver.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import com.mysql.fabric.xmlrpc.base.Data;
import com.zhongjian.webserver.alipay.AlipayConfig;
import com.zhongjian.webserver.common.RandomUtil;
import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderLineDto;
import com.zhongjian.webserver.mapper.LogMapper;
import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.service.OrderHandleService;

@Service
public class OrderHandleServiceImpl implements OrderHandleService {

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	LogMapper logMapper;

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
					Integer specElecNum = orderMapper.getSpecElecNumById(f.getSpecId());
					Integer thisTimeScore = productNum * specElecNum;
					score.set(0, score.get(0) + thisTimeScore);
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
		orderMapper.insertOrderHeadCo(theCurrentOrderNoCollectionName, theCurrentOrderNoCollectionsStr,
				TotalEveryAmountCoList.get(0),new Date(),UserId);
		// 预扣
		if (TotalEveryAmountCoList.get(0).compareTo(BigDecimal.ZERO) != 0) {
			orderMapper.insertPreSubQuata(UserId, TotalEveryAmountCoList.get(1), TotalEveryAmountCoList.get(2),
					TotalEveryAmountCoList.get(3), TotalEveryAmountCoList.get(4), new Date());
		}
		HashMap<String, Object> result = new HashMap<>();
		result.put("orderNoCollectionName", theCurrentOrderNoCollectionsStr);
		result.put("totalRealPayCo", TotalEveryAmountCoList.get(0));
		result.put("totalNotRealPayCo", TotalEveryAmountCoList.get(0));
		return result;
	}

	@Override
	@Transactional
	public boolean handleOrder(String orderNo, String totalAmount, String seller_id, String app_id) {
		// 查询对应的子订单信息
		if (!seller_id.equals(AlipayConfig.BUSINESSID) || !app_id.equals(AlipayConfig.APPID)) {
			return false;
		}
		if (orderNo.startsWith("CB")) {
			Map<String, Object> map = orderMapper.getDetailsFormorderheadC(orderNo);
			if (!map.get("TolAmount").toString().equals(totalAmount)) {
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
				} else {
					// 让支付宝不要再回调
					return true;
				}
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
			} else {
				// 让支付宝不要再回调
				return true;
			}

		} else if (orderNo.startsWith("VO")) {

		} else if (orderNo.startsWith("CZ")) {

		} else if (orderNo.startsWith("GS")) {

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
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
				com.zhongjian.webserver.alipay.AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
				AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
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
		model.setSellerId(AlipayConfig.BUSINESSID); // 商家id
		ali_request.setBizModel(model);
		ali_request.setNotifyUrl(AlipayConfig.notify_url); // 回调地址
		AlipayTradeAppPayResponse response = null;
		response = client.sdkExecute(ali_request);
		String orderString = response.getBody();
		return orderString;
	}
}

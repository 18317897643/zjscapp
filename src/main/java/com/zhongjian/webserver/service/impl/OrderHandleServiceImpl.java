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
import com.zhongjian.webserver.alipay.AlipayConfig;
import com.zhongjian.webserver.common.RandomUtil;
import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderLineDto;
import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.service.OrderHandleService;

@Service
public class OrderHandleServiceImpl implements OrderHandleService {

	@Autowired
	OrderMapper orderMapper;

	@Override
	@Transactional
	public HashMap<String, Object> createOrder(List<OrderHeadDto> orderHeads, Integer UserId) {
		String theCurrentOrderNoCollectionName = "CB" + RandomUtil.getFlowNumber();
		ArrayList<String> theCurrentOrderNoCollections = new ArrayList<>();
		ArrayList<BigDecimal> TotalAmountCoList = new ArrayList<>();
		BigDecimal TotalAmountCo = new BigDecimal("0.00");
		TotalAmountCoList.add(TotalAmountCo);
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
				TotalAmountCoList.set(0, TotalAmountCoList.get(0).add(e.getTotalAmount()));
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
		String theCurrentOrderNoCollectionsStr = "";
		for (int i = 0; i < theCurrentOrderNoCollections.size(); i++) {
			if (theCurrentOrderNoCollections.size() - 1 == i) {
				theCurrentOrderNoCollectionsStr = theCurrentOrderNoCollectionsStr + theCurrentOrderNoCollections.get(i);
			}
			theCurrentOrderNoCollectionsStr = theCurrentOrderNoCollectionsStr + theCurrentOrderNoCollections.get(i)
					+ "|";
		}
		orderMapper.insertOrderHeadCo(theCurrentOrderNoCollectionName, theCurrentOrderNoCollectionsStr,TotalAmountCoList.get(0));
		HashMap<String, Object> result = new HashMap<>();
		result.put("orderNoCollectionName", theCurrentOrderNoCollectionsStr);
		result.put("totalAmountCo", TotalAmountCoList.get(0));
		return result;
	}

	@Override
	public void test(String orderNo) {
		// 查询对应的子订单信息
		if (orderNo.startsWith("CB")) {

		} else if (orderNo.startsWith("B")) {

		} else if (orderNo.startsWith("VO")) {

		} else if (orderNo.startsWith("CZ")) {

		} else if (orderNo.startsWith("GS")) {

		} else {
		}
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
		model.setSellerId(AlipayConfig.BusinessId); // 商家id
		ali_request.setBizModel(model);
		ali_request.setNotifyUrl(AlipayConfig.notify_url); // 回调地址
		AlipayTradeAppPayResponse response = null;
		response = client.sdkExecute(ali_request);
		String orderString = response.getBody();
		return orderString;
	}
}

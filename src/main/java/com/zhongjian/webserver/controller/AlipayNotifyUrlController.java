package com.zhongjian.webserver.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.internal.util.AlipaySignature;
import com.zhongjian.webserver.alipay.AlipayConfig;

import io.swagger.annotations.ApiOperation;

@RestController
public class AlipayNotifyUrlController {

	/**
	 * 支付宝异步通知
	 * 
	 */
	@ApiOperation(httpMethod = "POST", notes = "支付宝异步通知接口", value = "支付宝异步通知接口")
	@RequestMapping(value = "/notify_url", method = RequestMethod.POST)
	public String notifyUrl(HttpServletRequest request) {
		// 1.从支付宝回调的request域中取值
		Map<String, String[]> requestParams = request.getParameterMap();
		HashMap<String, String> paramsMap = new HashMap<>();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = iter.next();
			String[] values = requestParams.get(name);
			paramsMap.put(name, values[0]);
		}
		boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.ALIPAY_PUBLIC_KEY,
				AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
		if (signVerified) {
			if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
				String out_trade_no = request.getParameter("out_trade_no"); // 商户订单号
				String total_amount = request.getParameter("total_amount"); // 订单金额
				String seller_id = request.getParameter("seller_id"); // 商家app_id
				String app_id = request.getParameter("app_id"); // 商家seller_id
			}
		} else {
			return "failure";
		}

	}

}

package com.zhongjian.webserver.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alipay.api.internal.util.AlipaySignature;
import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.alipay.AlipayConfig;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.mapper.ShoppingCartMapper;
import com.zhongjian.webserver.service.MemberShipService;
import com.zhongjian.webserver.service.OrderHandleService;

import io.swagger.annotations.ApiOperation;

@RestController
public class AlipayNotifyUrlController {

	@Autowired
	OrderHandleService orderHandleService;
	
	@Autowired
	AlipayConfig alipayConfig;

	@Autowired
	MemberShipService memberShipService;
	
	@Autowired
	ShoppingCartMapper shoppingCartMapper;
	/**
	 * 支付宝异步通知
	 * 
	 * @throws BusinessException
	 * 
	 */
	@ApiOperation(httpMethod = "POST", notes = "支付宝异步通知接口", value = "支付宝异步通知接口")
	@RequestMapping(value = "/notify_url", method = RequestMethod.POST)
	public String notifyUrl(HttpServletRequest request) throws BusinessException {
		try {
			// 1.从支付宝回调的request域中取值
			Map<String, String[]> requestParams = request.getParameterMap();
			HashMap<String, String> paramsMap = new HashMap<>();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String[] values = requestParams.get(name);
				paramsMap.put(name, values[0]);
			}
			boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, alipayConfig.ALIPAY_PUBLIC_KEY,
					alipayConfig.CHARSET, alipayConfig.SIGNTYPE);
			if (signVerified) {
				if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
					String out_trade_no = request.getParameter("out_trade_no"); // 商户订单号
					String total_amount = request.getParameter("total_amount"); // 订单金额
					String seller_id = request.getParameter("seller_id"); // 商家app_id
					String app_id = request.getParameter("app_id"); // 商家seller_id
					LoggingUtil.i("订单号为 " + out_trade_no + " 支付金额为 " + total_amount + " 开始回调啦！");
					// 校验四项
					if (orderHandleService.asyncHandleOrder(out_trade_no, total_amount, seller_id, app_id)) {
						return "success";
					} else {
						return "failure";
					}
				} else {
					return "failure";
				}
			} else {
				return "failure";
			}
		} catch (Exception e) {
			LoggingUtil.e("支付宝异步通知发生异常，请注意处理 " + e );
			return "failure";
		}
	}

	/**
	 * 支付宝异步通知
	 * 
	 * @throws BusinessException
	 * 
	 */
	@ApiOperation(httpMethod = "GET", notes = "测试接口", value = "测试接口")
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	Result<Object> test(HttpServletRequest request) {
		System.out.println(1);
		return null;

		// Map<String, Object>map =
		// orderMapper.getNeedSubDetailsOfOrderHead("B20170528220557868263633");
		//
		// System.out.println(map.get("UserId").getClass());
		// logMapper.insertCouponRecord(1, new Date(), new BigDecimal("2.00"),
		// "-", "购买商品，订单号：" + "B214324342432432");
	}
}

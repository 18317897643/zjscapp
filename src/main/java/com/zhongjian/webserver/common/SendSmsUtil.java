package com.zhongjian.webserver.common;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import java.util.Random;

public class SendSmsUtil {
	private static final String API_URL = "http://gw.api.taobao.com/router/rest";
	private static final String API_APPKEY = "23831132";
	private static final String API_SECRET = "f7d177050b7c6719724059a1e5bd8203";
	private static final String PRODUCT = "众健商城";

	public static String sendCaptcha(String phoneNo,String captcha) throws ApiException {
		// 阿里大于
		TaobaoClient client = new DefaultTaobaoClient(API_URL, API_APPKEY, API_SECRET);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType("normal");
		req.setSmsFreeSignName(PRODUCT);
		req.setSmsParamString("{code:'" + captcha + "'}");
		req.setRecNum(phoneNo);
		req.setSmsTemplateCode("SMS_67251183");
		client.execute(req);
		return captcha;
	}

	public static String randomCaptcha(int length) {
		int maxNum = 10;
		int maxLength = 10;
		// 最大长度为10,最小长度为4
		if (length > maxLength || length < 4) {
			return null;
		}
		int i;
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuilder code = new StringBuilder("");
		Random r = new Random();
		while (count < length) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				code.append(str[i]);
				count++;
			}
		}
		return code.toString();
	}
}
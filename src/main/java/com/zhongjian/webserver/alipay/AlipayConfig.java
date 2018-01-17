package com.zhongjian.webserver.alipay;

public class AlipayConfig {
	// 1.商户appid
	public String APPID = "";

	// 1.商家id
	public String BUSINESSID = "";

	// 2.私钥 pkcs8格式的
	public String RSA_PRIVATE_KEY = "";

	// 3.支付宝公钥
	public String ALIPAY_PUBLIC_KEY = "";

	// 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public String notify_url = "";

	// 6.请求网关地址
	public String URL = "";

	// 7.编码
	public String CHARSET = "";

	// 8.返回格式
	public String FORMAT = "";

	// 9.加密类型
	public String SIGNTYPE = "";

	public void setAPPID(String aPPID) {
		APPID = aPPID;
	}

	public void setBUSINESSID(String bUSINESSID) {
		BUSINESSID = bUSINESSID;
	}

	public void setRSA_PRIVATE_KEY(String rSA_PRIVATE_KEY) {
		RSA_PRIVATE_KEY = rSA_PRIVATE_KEY;
	}

	public void setALIPAY_PUBLIC_KEY(String aLIPAY_PUBLIC_KEY) {
		ALIPAY_PUBLIC_KEY = aLIPAY_PUBLIC_KEY;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public void setCHARSET(String cHARSET) {
		CHARSET = cHARSET;
	}

	public void setFORMAT(String fORMAT) {
		FORMAT = fORMAT;
	}

	public void setSIGNTYPE(String sIGNTYPE) {
		SIGNTYPE = sIGNTYPE;
	}

}

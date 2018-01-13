package com.zhongjian.webserver.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MallData {
	@Value("${malldata.productTag}")
	private Integer productTag;

	@Value("${malldata.vipNeedPay}")
	private Integer vipNeedPay;
	
	@Value("${malldata.gcNeedPay}")
	private Integer gcNeedPay;

	@Value("${malldata.subProxyNeedPay}")
	private Integer subProxyNeedPay;

	public Integer getProductTag() {
		return productTag;
	}

	public void setProductTag(Integer productTag) {
		this.productTag = productTag;
	}

	public Integer getVipNeedPay() {
		return vipNeedPay;
	}

	public void setVipNeedPay(Integer vipNeedPay) {
		this.vipNeedPay = vipNeedPay;
	}

	public Integer getSubProxyNeedPay() {
		return subProxyNeedPay;
	}

	public void setSubProxyNeedPay(Integer subProxyNeedPay) {
		this.subProxyNeedPay = subProxyNeedPay;
	}

	public Integer getGcNeedPay() {
		return gcNeedPay;
	}

	public void setGcNeedPay(Integer gcNeedPay) {
		this.gcNeedPay = gcNeedPay;
	}

	public MallData(){
		
	}
}
package com.zhongjian.webserver.beanconfiguration;
public class MallData {
	private Integer productTag;

	private Integer vipNeedPay;
	
	private Integer gcNeedPay;

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

	public MallData(Integer productTag, Integer vipNeedPay, Integer subProxyNeedPay,Integer gcNeedPay) {
		super();
		this.productTag = productTag;
		this.vipNeedPay = vipNeedPay;
		this.subProxyNeedPay = subProxyNeedPay;
		this.gcNeedPay = gcNeedPay;
	}
}

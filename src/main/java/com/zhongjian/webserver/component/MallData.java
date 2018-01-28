package com.zhongjian.webserver.component;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zhongjian.webserver.mapper.ProductMapper;

@Component
public class MallData {
	
	@Autowired
	private ProductMapper productMapper;
	
	private Integer productTag;

	@Value("${malldata.vipNeedPay}")
	private BigDecimal vipNeedPay;

	@Value("${malldata.gcNeedPay}")
	private BigDecimal gcNeedPay;

	@Value("${malldata.subProxyNeedPay}")
	private BigDecimal subProxyNeedPay;

	public Integer getProductTag() {
		return productTag;
	}

	public void setProductTag(Integer productTag) {
		this.productTag = productTag;
	}


	public BigDecimal getVipNeedPay() {
		return vipNeedPay;
	}

	public void setVipNeedPay(BigDecimal vipNeedPay) {
		this.vipNeedPay = vipNeedPay;
	}

	public BigDecimal getGcNeedPay() {
		return gcNeedPay;
	}

	public void setGcNeedPay(BigDecimal gcNeedPay) {
		this.gcNeedPay = gcNeedPay;
	}

	public BigDecimal getSubProxyNeedPay() {
		return subProxyNeedPay;
	}

	public void setSubProxyNeedPay(BigDecimal subProxyNeedPay) {
		this.subProxyNeedPay = subProxyNeedPay;
	}

	public MallData() {
	}

	@PostConstruct
	public void init() {
		setProductTag(productMapper.getMemberTag());
	}
}

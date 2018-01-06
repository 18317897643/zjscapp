package com.zhongjian.webserver.beanconfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MallDataBean {
	@Value("${malldata.productTag}")
	private Integer productTag;

	@Value("${malldata.vipNeedPay}")
	private Integer vipNeedPay;

	@Value("${malldata.subProxyNeedPay}")
	private Integer subProxyNeedPay;

	@Bean
	public MallData createMallData() {
		return new MallData(productTag, vipNeedPay, subProxyNeedPay);
	}
}
package com.zhongjian.webserver.beanconfiguration;
/**
 * Spring manage ExiryMap
 * @author chen_di
 *
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zhongjian.webserver.common.ExpiryMap;

@Configuration
public class SingExiryMapConfiguration {

	@Bean(name="verifyCodeExiryMap")
	@Primary
	public ExpiryMap<String, String> createVerifyCodeExiryMap() {
		return new ExpiryMap<String,String>();
	}
	
	@Bean(name="payPasswordModifyMap")
	public ExpiryMap<String, String> createPayPasswordModifyMap() {
		return new ExpiryMap<String,String>();
	}
	
	@Bean(name="mayNotUpdateMap")
	public ExpiryMap<String, String> createMayNotUpdateMap() {
		return new ExpiryMap<String,String>(1000 * 60 * 360);
	}
}

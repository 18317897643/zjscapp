package com.zhongjian.webserver.beanconfiguration;
/**
 * Spring manage ExiryMap
 * @author chen_di
 *
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SingExiryMapConfiguration {

	@Bean
	public ExpiryMap<String, String> createExiryMap() {
		return new ExpiryMap<String,String>();
	}
}

package com.zhongjian.webserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//Spring boot做web服务必要的一些自动配置
@Import({ DispatcherServletAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.class,
		HttpEncodingAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
		ServerPropertiesAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class,
		WebMvcAutoConfiguration.class, })
//扫包的，默认扫描所在包的子包
@ComponentScan
// swageer2的配置你
@EnableSwagger2
//Spring boot开启定时任务序列化token
@EnableScheduling
//线程池开启
@EnableAsync
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
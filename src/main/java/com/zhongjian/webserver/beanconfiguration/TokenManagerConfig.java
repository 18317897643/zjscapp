package com.zhongjian.webserver.beanconfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zhongjian.webserver.common.GsonUtil;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.TokenManager;

@Configuration
public class TokenManagerConfig {

	@Value("${token.largeLenth}")
	private int listLargeLenth;

	@Value("${token.jsonFile}")
	private String jsonFile;

	/**
	 * Create TokenManager
	 * *******************
	 * if file exists, then we read from file
	 * else create it.
	 * 
	 */
	@Bean
	public TokenManager createTokenManager() {
		File file = new File(jsonFile);
		if (file.exists()) {
			// 读取json文件实例化json
			FileReader fr = null;
			BufferedReader br = null;
			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String tokenJson = br.readLine();
				System.out.println("读取到的json是" + tokenJson);
				TokenManager tokenManager = GsonUtil.GsonToBean(tokenJson, TokenManager.class);
				return tokenManager;
			} catch (Exception e) {
				LoggingUtil.e("读取token文件失败");
				return TokenManager.getTokenManager(listLargeLenth);
			}finally {
				try {
					br.close();
				} catch (IOException e) {
					LoggingUtil.e("缓冲读关闭异常");
				}
				try {
					fr.close();
				} catch (IOException e) {
					LoggingUtil.e("读关闭异常");
				}
			}
		}
		return TokenManager.getTokenManager(listLargeLenth);
	}
	}

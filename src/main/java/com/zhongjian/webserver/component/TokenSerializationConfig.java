package com.zhongjian.webserver.component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zhongjian.webserver.common.GsonUtil;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.TokenManager;

@Component
public class TokenSerializationConfig {
	@Value("${token.jsonFile}")
	private String jsonFile;

	@Autowired
	private TokenManager tokenManager;

	@Scheduled(cron = "0 0/1 * * * ?") // 每30分钟执行一次
	public void saveToken() {
		String tokenJson = GsonUtil.GsonString(tokenManager);
		if (tokenJson == null) {
			LoggingUtil.e("系统存储token异常");
		} else {
			File file = new File(jsonFile);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}
			FileWriter fileWriter = null;
			BufferedWriter bw = null;
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				bw = new BufferedWriter(fileWriter);
			    bw.write(tokenJson);
			    bw.flush();
			} catch (IOException e) {
				LoggingUtil.e("系统存储token异常时创建文件异常");
			}finally {
			    try {
					bw.close();
				} catch (IOException e) {
					LoggingUtil.e("缓冲写关闭异常");
				}
			    try {
					fileWriter.close();
				} catch (IOException e) {
					LoggingUtil.e("写关闭异常");
				}
			}
		}
	}
}

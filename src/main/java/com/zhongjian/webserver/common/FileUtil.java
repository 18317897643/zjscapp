package com.zhongjian.webserver.common;

import java.util.UUID;

public class FileUtil {
	public static String changeFileNameToRandom(String originalFilename){
		return UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
	};
}

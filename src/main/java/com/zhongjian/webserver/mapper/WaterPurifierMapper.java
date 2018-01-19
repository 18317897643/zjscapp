package com.zhongjian.webserver.mapper;

import java.util.Map;

public interface WaterPurifierMapper {

	Map<String, Object> getCodeInfoByCodeNo(String codeNo);
	
	Integer deleteCode(String codeNo);
}

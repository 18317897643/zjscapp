package com.zhongjian.webserver.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AddressMapper {

	Map<String, Object> queryAddress(@Param("condition") String condition, @Param("param") Integer param,@Param("additionalCondition") String additionalCondition);

	List<Map<String, Object>> queryAllAddress(@Param("param") Integer param);
	
	Integer addAddress(Map<String, Object> paramMap);

	Integer deleteAddressById(Integer id, Integer UserId);

	Integer updateAddressById(Map<String, Object> paramMap);
}

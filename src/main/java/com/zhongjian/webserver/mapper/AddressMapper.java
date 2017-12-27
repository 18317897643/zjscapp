package com.zhongjian.webserver.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AddressMapper {

	Map<String, Object> queryAddress(@Param("condition") String condition, @Param("param") Integer param,@Param("additionalCondition") String additionalCondition);

	Integer addAddress(Map<String, Object> paramMap);

	Integer deleteAddressById(Integer id, Integer UserId);

	Integer updateAddressById(Map<String, Object> paramMap);
}

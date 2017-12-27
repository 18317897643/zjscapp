package com.zhongjian.webserver.service;

import java.util.Map;

public interface AddressManagerService {

	Map<String, Object> getAllAddressByUserId(Integer userId);
	
	Map<String, Object> getAddressById(Integer id);
	
	Map<String, Object> getDefaultAddressById(Integer userId);
	
	Integer addAddress(Map<String, Object> paramMap);
	
	Integer deleteAddressById(Integer id,Integer UserId);
	
	Integer updateAddressById(Map<String, Object> paramMap);
}

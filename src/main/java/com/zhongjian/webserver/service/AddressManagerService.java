package com.zhongjian.webserver.service;

import java.util.Map;

public interface AddressManagerService {

	Map<String, Object> getAllAdressByUserId(Integer userId);
	
	Map<String, Object> getAdressById(Integer id);
	
	Integer addAddress(Map<String, Object> paramMap);
	
	Integer deleteAddressById(Integer id,Integer UserId);
	
	Integer updateAddressById(Map<String, Object> paramMap);
}

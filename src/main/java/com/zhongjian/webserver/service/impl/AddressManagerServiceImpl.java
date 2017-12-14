package com.zhongjian.webserver.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.mapper.AddressMapper;
import com.zhongjian.webserver.service.AddressManagerService;

@Service
public class AddressManagerServiceImpl implements AddressManagerService {
	
	@Autowired
	AddressMapper addressMapper;
	
	@Override
	public Map<String, Object> getAllAdressByUserId(Integer userId) {
		return addressMapper.queryAddress("a.UserId", userId);
	}

	@Override
	public Map<String, Object> getAdressById(Integer id) {
		return addressMapper.queryAddress("a.Id", id);
	}

	@Override
	public Integer addAddress(Map<String, Object> paramMap) {
		return addressMapper.addAddress(paramMap);
		
	}

	@Override
	public Integer deleteAddressById(Integer id,Integer UserId) {
		return addressMapper.deleteAddressById(id,UserId);
	}

	@Override
	public Integer updateAddressById(Map<String, Object> paramMap) {
		return addressMapper.updateAddressById(paramMap);
	}

	
	
}

package com.zhongjian.webserver.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.mapper.ShoppingCartMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.pojo.ShoppingCart;
import com.zhongjian.webserver.service.PersonalCenterService;

@Service
public class PersonalCenterServiceImpl implements PersonalCenterService {

	@Autowired
	UserMapper userMapper;
	
	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	ShoppingCartMapper shoppingCartMapper;
	
	@Override
	public Map<String, Object> getInformOfConsumption(String userName) {
    		return userMapper.selectPersonalInform(userName);
	}

	@Override
	public List<Integer> getUserOrderStatus(Integer userId) {
		return orderMapper.getUserOrderStatus(userId);
	}

	@Override
	public  List<ShoppingCart>  getShoppingCartInfo(Integer userId) {
		return shoppingCartMapper.getShoppingCartInfo(userId);
	}
}
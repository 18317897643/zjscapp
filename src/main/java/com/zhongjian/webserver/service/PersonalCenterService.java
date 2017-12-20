package com.zhongjian.webserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhongjian.webserver.pojo.ShoppingCart;

public interface PersonalCenterService {

	//查看钱包信息
	Map<String, Object> getInformOfConsumption(String userName);
	
	//查看待付款，待发货，待收货，待评价
	List<Integer> getUserOrderStatus(Integer userId);
	
	//查看具体
	
	
	//查看个人购物车信息
	List<HashMap<String, Object>> getShoppingCartInfo(Integer userId);
	
	//删除购物车数据
	boolean delShoppingCartInfoById(Integer userId ,Integer id);
}

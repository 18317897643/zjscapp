package com.zhongjian.webserver.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhongjian.webserver.pojo.ProxyApply;

public interface PersonalCenterService {

	//查看钱包信息
	Map<String, Object> getInformOfConsumption(String userName);
	
	//查看待付款，待发货，待收货，待评价
	List<Integer> getUserOrderStatus(Integer userId);
	
	//查看具体
	
	//查看个人购物车信息
	List<HashMap<String, Object>> getShoppingCartInfo(Integer userId);
	
	//购物车新增
	Integer addShoppingCartInfo(Integer userId,Integer productId, Integer specId, Integer productNum,Date CreateTime);
	
	//购物车信息更新（数量更新）
	Integer setShoppingCartInfo(Integer userId,Integer shoppingCartId, Integer productNum);
	
	//删除购物车数据
	boolean delShoppingCartInfoById(Integer userId ,Integer id);
	
	Integer getProductIdByShoppingId(Integer shoppingCartId);
	
	//判断代理申请是否已经申请过了
	boolean isAlreadyApply(Integer UserId);
	
	ProxyApply getProxyApply(Integer UserId);
	
}

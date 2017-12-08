package com.zhongjian.webserver.mapper;

import java.util.List;

import com.zhongjian.webserver.pojo.ShoppingCart;

public interface ShoppingCartMapper {

	
	 List<ShoppingCart>  getShoppingCartInfo(Integer userId);
}

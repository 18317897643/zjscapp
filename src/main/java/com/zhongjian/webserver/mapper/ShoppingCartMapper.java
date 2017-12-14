package com.zhongjian.webserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.ShoppingCart;

public interface ShoppingCartMapper {

	
	 List<ShoppingCart>  getShoppingCartInfo(Integer userId);
	 
	 Integer delShoppingCartInfoById(@Param("UserId") Integer userId ,@Param("id") Integer id);
}

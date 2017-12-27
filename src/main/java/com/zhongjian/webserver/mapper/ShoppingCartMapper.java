package com.zhongjian.webserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.ShoppingCart;

public interface ShoppingCartMapper {

	 List<ShoppingCart>  getShoppingCartInfo(Integer userId);
	 
	 Integer delShoppingCartInfoById(@Param("UserId") Integer userId ,@Param("id") Integer id);
	 
	 Integer addShoppingCartInfo(@Param("UserId") Integer userId,@Param("productId") Integer productId,@Param("specId") Integer specId ,@Param("productNum") Integer productNum);
	 
	 Integer setShoppingCartInfo(@Param("UserId") Integer userId,@Param("shoppingCartId") Integer shoppingCartId,@Param("productNum") Integer productNum);
	 
	 Integer getProductIdByShoppingId(@Param("shoppingCartId") Integer shoppingCartId);
}

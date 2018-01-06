package com.zhongjian.webserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderLineDto;

public interface OrderMapper {

	List<Integer> getUserOrderStatus(Integer userId);

	Integer insertOrderHead(OrderHeadDto orderHeadDto);

	Integer insertOrderLine(OrderLineDto orderLineDto);
	
	Integer getSpecElecNumById(Integer specId);
	
	void updateOrderHeadScore(@Param("Score") Integer score,@Param("Id") Integer id);

}

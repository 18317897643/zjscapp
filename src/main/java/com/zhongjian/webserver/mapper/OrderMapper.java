package com.zhongjian.webserver.mapper;

import java.util.List;

public interface OrderMapper {

	
    List<Integer> getUserOrderStatus(Integer userId);
    
    
}

package com.zhongjian.webserver.mapper;

import java.util.List;

import com.zhongjian.webserver.pojo.ProductCategory;

public interface ProductMapper {
        
	List<ProductCategory> getProductOfCategory();
	
	
}

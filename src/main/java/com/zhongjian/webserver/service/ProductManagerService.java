package com.zhongjian.webserver.service;

import java.util.List;

import com.zhongjian.webserver.pojo.ProductCategory;

public interface ProductManagerService {
	
	List<ProductCategory> getProductOfCategory();
	
}

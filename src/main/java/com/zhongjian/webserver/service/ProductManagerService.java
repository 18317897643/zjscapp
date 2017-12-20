package com.zhongjian.webserver.service;

import java.util.HashMap;
import java.util.List;

import com.zhongjian.webserver.pojo.ProductCategory;
import com.zhongjian.webserver.pojo.ProductComment;

public interface ProductManagerService {
	
	List<ProductCategory> getProductOfCategory();
	
	HashMap<String, Object> getProductDetails(Integer productId);
	
	List<ProductComment> getProductComment(Integer productId, Integer page, Integer pageNum);
	
	String getProductHtmlText(Integer id);
	
}

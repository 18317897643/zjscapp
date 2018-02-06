package com.zhongjian.webserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhongjian.webserver.dto.PANRequestMap;
import com.zhongjian.webserver.dto.PANResponseMap;
import com.zhongjian.webserver.pojo.ProductCategory;
import com.zhongjian.webserver.pojo.ProductCommentShow;

public interface ProductManagerService {
	
	List<ProductCategory> getProductOfCategory();
	
	HashMap<String, Object> getProductDetails(Integer productId);
	
	List<ProductCommentShow> getProductComment(Integer productId, Integer page, Integer pageNum);
	
	String getProductHtmlText(Integer id);
	
	List<PANResponseMap> checkProductStoreNum(List<PANRequestMap> pANRequestMaps);
	
	List<HashMap<String, Object>> getSubProductOfCategory(Integer subCategoryId,String condition,Integer page,Integer pageNum);
	
	List<Map<String, Object>> searchProduct(String key);
	
	void addProductComment(Integer userId, List<Map<String, Object>> productCommentMaps);

	
}

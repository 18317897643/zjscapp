package com.zhongjian.webserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.Product;
import com.zhongjian.webserver.pojo.ProductCategory;
import com.zhongjian.webserver.pojo.ProductComment;

public interface ProductMapper {
        
	List<ProductCategory> getProductOfCategory();
	
	Product findById(Integer id);
	
	List<ProductComment> selectProductcommentById(@Param("productId") Integer productId,@Param("page") Integer page,@Param("pageNum") Integer pageNum);
	
	String findProductHtmlTextById(Integer id);
	
	Product getProductNumById(Integer id);
    	
}

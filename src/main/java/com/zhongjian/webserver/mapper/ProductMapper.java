package com.zhongjian.webserver.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.Product;
import com.zhongjian.webserver.pojo.ProductCategory;
import com.zhongjian.webserver.pojo.ProductCommentShow;
import com.zhongjian.webserver.pojo.Tag;

public interface ProductMapper {
        
	List<ProductCategory> getCategory();
	
	Product findById(Integer id);
	
	List<ProductCommentShow> selectProductcommentById(@Param("productId") Integer productId,@Param("page") Integer page,@Param("pageNum") Integer pageNum);
	
	String findProductHtmlTextById(Integer id);
	
	Product getProductNumById(Integer id);
	
    List<Product> getProductOfCategory(Integer SubCategoryId);
    
    List<Tag> getAllTagProduct(Integer tag);
    
    List<Product> getProductsOfSubCategory(@Param("SubCategoryId") Integer subCategoryId,@Param("Condition") String condition,@Param("page") Integer page,@Param("pageNum") Integer pageNum);
    
    List<Product> getProductsOfTag(@Param("Tag") Integer tag,@Param("Condition") String condition,@Param("page") Integer page,@Param("pageNum") Integer pageNum);
    
	Integer getMemberTag();
	
	List<Map<String, Object>> searchProduct(String key);
    	
}

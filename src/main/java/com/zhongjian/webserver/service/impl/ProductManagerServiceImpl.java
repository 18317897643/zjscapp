package com.zhongjian.webserver.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.mapper.ProductMapper;
import com.zhongjian.webserver.pojo.Product;
import com.zhongjian.webserver.pojo.ProductCategory;
import com.zhongjian.webserver.pojo.ProductComment;
import com.zhongjian.webserver.service.ProductManagerService;

@Service
public class ProductManagerServiceImpl implements ProductManagerService {

	@Autowired
	private ProductMapper productMapper;
	
	@Override
	public List<ProductCategory> getProductOfCategory() {
		return productMapper.getProductOfCategory();
	}

	@Override
	public HashMap<String, Object> getProductDetails(Integer productId) {
		Product product = productMapper.findById(productId);
		ProductComment productComment = productMapper.selectProductcommentById(productId, 0, 1).get(0);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("product", product);
		map.put("productComment", productComment);
		return map;
	}
	@Override
	public List<ProductComment> getProductComment(Integer productId, Integer page, Integer pageNum) {
		return productMapper.selectProductcommentById(productId, page, pageNum);
	}

	@Override
	public String getProductHtmlText(Integer id) {
		return productMapper.findProductHtmlTextById(id);
	}

}

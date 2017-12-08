package com.zhongjian.webserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.mapper.ProductMapper;
import com.zhongjian.webserver.pojo.ProductCategory;
import com.zhongjian.webserver.service.ProductManagerService;

@Service
public class ProductManagerServiceImpl implements ProductManagerService {

	@Autowired
	private ProductMapper productMapper;
	
	@Override
	public List<ProductCategory> getProductOfCategory() {
		return productMapper.getProductOfCategory();
	}


}

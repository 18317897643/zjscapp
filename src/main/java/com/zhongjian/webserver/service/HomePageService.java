package com.zhongjian.webserver.service;

import java.util.HashMap;
import java.util.List;

import com.zhongjian.webserver.pojo.Product;

public interface HomePageService {

	HashMap<String, Object>initHomePage(Integer productNum);
	
	List<Product> getAreaProducts(Integer tag,String condition,Integer page,Integer pageNum);
	
}

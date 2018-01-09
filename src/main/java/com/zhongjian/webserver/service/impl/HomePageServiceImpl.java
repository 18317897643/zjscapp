package com.zhongjian.webserver.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.mapper.AdvMapper;
import com.zhongjian.webserver.mapper.ProductMapper;
import com.zhongjian.webserver.service.HomePageService;

@Service
public class HomePageServiceImpl implements HomePageService{

	@Autowired
	AdvMapper advMapper;
	
	@Autowired
	ProductMapper productMapper;
	
	@Override
	public HashMap<String, Object> initHomePage() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("picList", advMapper.getHomePagePic());
		result.put("tile", advMapper.getHomePageTitle());
		result.put("productOfTag",productMapper.getAllTagProduct());
		return result;
	}

}

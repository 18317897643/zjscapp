package com.zhongjian.webserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.mapper.AdvMapper;
import com.zhongjian.webserver.pojo.Article;
import com.zhongjian.webserver.service.WebTitleService;

@Service
public class WebTitleServiceImpl implements WebTitleService {

	@Autowired
	AdvMapper advMapper;
	
	@Override
	public Article getProtocol(Integer id) {
		return advMapper.getArticle(id);
	}

}

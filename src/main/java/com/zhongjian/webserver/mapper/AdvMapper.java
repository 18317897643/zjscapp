package com.zhongjian.webserver.mapper;

import java.util.HashMap;
import java.util.List;

import com.zhongjian.webserver.pojo.Article;

public interface AdvMapper {

	List<HashMap<String,Object>> getHomePagePic();
	
	HashMap<String,Object> getHomePageTitle();
	
	Article getArticle(Integer id);
}

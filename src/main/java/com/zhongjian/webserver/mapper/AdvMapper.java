package com.zhongjian.webserver.mapper;

import java.util.HashMap;
import java.util.List;

public interface AdvMapper {

	List<HashMap<String,Object>> getHomePagePic();
	
	HashMap<String,Object> getHomePageTitle();
}

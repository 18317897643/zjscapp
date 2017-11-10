package com.zhongjian.webserver.service;

import com.zhongjian.webserver.pojo.User;

public interface TestService {

	public boolean test();
	
	public User findUserById(Integer id)  throws Exception;
}

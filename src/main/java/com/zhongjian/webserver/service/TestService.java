package com.zhongjian.webserver.service;

import com.zhongjian.webserver.pojo.User;

public interface TestService {

	public boolean test();
	
	public User findUserById(Integer id);
	
	public void testUpdate();
	
	public void testToken(String userName);
		
	public boolean checkToken(String token);
}

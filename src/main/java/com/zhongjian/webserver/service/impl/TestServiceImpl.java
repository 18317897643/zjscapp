package com.zhongjian.webserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.pojo.User;
import com.zhongjian.webserver.service.TestService;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public boolean test() {
		return true;
	}

	@Override
	public User findUserById(Integer id) throws Exception {
		return userMapper.findUserById(id);
	}

}

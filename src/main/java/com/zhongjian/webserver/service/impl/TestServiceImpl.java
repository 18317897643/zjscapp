package com.zhongjian.webserver.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.common.TokenManager;
import com.zhongjian.webserver.mapper.StudentMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.pojo.Student;
import com.zhongjian.webserver.pojo.User;
import com.zhongjian.webserver.service.TestService;

@Service
@Transactional(readOnly=true) 
public class TestServiceImpl implements TestService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private TokenManager tokenManager;
	
	@Override
	public boolean test() {
		return true;
	}

	@Override
	public User findUserById(Integer id)  {
		System.out.println(userMapper.findUserById(id));
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
		}
		System.out.println(userMapper.findUserNameById(id));
		return null;
	}

	@Override
	@Transactional
	public void testUpdate() {
		Student student = new Student();
		student.setId(1);
		student.setName("陈帝");
		student.setSex("男");
		studentMapper.updateStudent(student);
		userMapper.updateUser("王二");	
	}

	@Override
	public void testToken(String userName) {
		tokenManager.storeOrUpdateToken(userName);
	}


	@Override
	public boolean checkToken(String token) {
		return tokenManager.checkToken(token);
		
	}

}

package com.zhongjian.webserver.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.pojo.User;
import com.zhongjian.webserver.service.TestService;

@RestController
public class TestController {

	@Autowired
	private TestService testService;


	@RequestMapping(value = "old", method = RequestMethod.POST)
	Map<String, String> old() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("1", "你好");
		return map;
	}

	@RequestMapping("/three")
	List<String> three() {
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("发");
		return list;
	}

	@RequestMapping("/four")
	Set<String> four() {
		Set<String> set = new HashSet<String>();
		set.add("搭建");
		set.add("搭建2");
		return set;
	}

	@RequestMapping("/error")
	Set<String> error() throws BusinessException {
		throw new BusinessException(100, "业务异常");
	}

	@RequestMapping("/error1")
	int error1() {
		String a = "abc";
		int b = Integer.parseInt(a);
		return b;
	}

	@RequestMapping("/log")
	void log() {
		LoggingUtil.d("测试d");
		LoggingUtil.i("测试i");
		LoggingUtil.w("测试w");
		LoggingUtil.e("测试e");
	}

	@RequestMapping("/service")
	boolean service() {
		return testService.test();
	}

	@RequestMapping("/findUserById")
	public User findUserById(@RequestParam(value = "id") Integer id) {
		User user = null;
		try {
			user = testService.findUserById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
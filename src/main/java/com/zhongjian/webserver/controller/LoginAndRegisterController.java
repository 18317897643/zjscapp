package com.zhongjian.webserver.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.common.ResultUtil;
import com.zhongjian.webserver.service.LoginAndRegisterService;

@RestController
public class LoginAndRegisterController {

	@Autowired
	private LoginAndRegisterService loginAndRegisterService;
	
	@RequestMapping(value = "/zhongjian/LoginAndRegister/SendRegisterVerifyCode", method = RequestMethod.POST)
	Result<Object> sendRegisterVerifyCode(@RequestBody Map<String, String> phoneMap) throws BusinessException {
		// receive the phone
		String phoneNum = phoneMap.get("phoneNum");
		try {
			loginAndRegisterService.sendRegisterVerifyCode(phoneNum);
		} catch (Exception e) {
			throw new BusinessException(1, "短信发送异常");
		}
		return ResultUtil.success();
	}
	
	@RequestMapping(value = "/zhongjian/LoginAndRegister/RegisterUser", method = RequestMethod.POST)
	Result<Object> registerUser(@RequestBody Map<String, String> userMap) throws BusinessException {
		//receive the args
		String phoneNum = userMap.get("phoneNum");
		//
		try {
			loginAndRegisterService.sendRegisterVerifyCode(phoneNum);
		} catch (Exception e) {
			throw new BusinessException(1, "短信发送异常");
		}
		return ResultUtil.success();
	}

}
package com.zhongjian.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.common.ResultUtil;
import com.zhongjian.webserver.common.Status;
import com.zhongjian.webserver.service.WebTitleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/TitleManager/", description = "协议相关")
@RequestMapping(value = "/TitleManager")
public class WebTitleController {

	@Autowired
	WebTitleService webTitleService;
	
	@ApiOperation(httpMethod = "GET", notes = "获取各种协议", value = "获取各种协议")
	@RequestMapping(value = "/getProtocol", method = RequestMethod.GET)
	Result<Object> getProtocol(@RequestParam Integer id) throws BusinessException {
		try {
			return ResultUtil.success(webTitleService.getProtocol(id));
		} catch (Exception e) {
			LoggingUtil.e("获取各种协议异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取各种协议异常");
		}
	}
}

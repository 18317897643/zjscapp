package com.zhongjian.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.common.ResultUtil;
import com.zhongjian.webserver.common.Status;
import com.zhongjian.webserver.common.TokenManager;
import com.zhongjian.webserver.service.HomePageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/HomePage/", description = "首页相关")
@RequestMapping(value = "/HomePage")
public class HomePageController {

	@Autowired
	TokenManager tokenManager;
	
	@Autowired
	HomePageService homePageService;
	
	//  "picList": [],"tile": null 有可能出现
	@ApiOperation(httpMethod = "GET", notes = "首页初始化数据", value = "首页初始化数据")
	@RequestMapping(value = "/initHomePage", method = RequestMethod.GET)
	Result<Object> initHomePage() throws BusinessException {
		try {
			return ResultUtil.success(homePageService.initHomePage());
		} catch (Exception e) {
			LoggingUtil.e("首页初始化数据异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "首页初始化数据异常");
		}
	}

}

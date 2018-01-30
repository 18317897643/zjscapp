package com.zhongjian.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.component.MallData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/AppVersion", description = "应用版本")
public class AppVersionManagerController {

	@Autowired
	private MallData mallData;

	@ApiOperation(httpMethod = "GET", notes = "获取支持的最低版本基数", value = "获取支持的最低版本基数")
	@RequestMapping(value = "/GetLowVersion/",method = RequestMethod.GET)
	public Integer getAppVersion() {
		return mallData.getVersion();
	}

}

package com.zhongjian.webserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.common.ResultUtil;
import com.zhongjian.webserver.common.Status;
import com.zhongjian.webserver.common.TokenManager;
import com.zhongjian.webserver.service.ProductManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/ProductManager/", description = "商品相关")
public class ProductManagerController {
	@Autowired
	TokenManager tokenManager;
	
	@Autowired
	private ProductManagerService productManagerService;
	
	@ApiOperation(httpMethod = "GET", notes = "获取所有商品分类", value = "获取所有商品分类")
	@RequestMapping(value = "/ProductManager/getProductOfCategory/{token}", method = RequestMethod.GET)
	Result<Object> getProductOfCategory(@PathVariable("token") String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			return ResultUtil.success(productManagerService.getProductOfCategory());
		} catch (Exception e) {
			LoggingUtil.e("获取所有商品并分类处异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取所有商品并分类处异常");
		}
	}
}

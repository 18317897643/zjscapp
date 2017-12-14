package com.zhongjian.webserver.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.zhongjian.webserver.pojo.ShoppingCart;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.PersonalCenterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/PersonalCenter/", description = "个人中心")
public class PersonalCenterController {

	@Autowired
	TokenManager tokenManager;

	@Autowired
	PersonalCenterService personalCenterService;

	@Autowired
	LoginAndRegisterService loginAndRegisterService;

	@ApiOperation(httpMethod = "GET", notes = "根据token获取个人中心数据", value = "初始化个人中心数据")
	@RequestMapping(value = "/PersonalCenter/initPersonalCenterData/{token}", method = RequestMethod.GET)
	Result<Object> initPersonalCenterData(@PathVariable("token") String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			Integer id = loginAndRegisterService.getUserIdByUserName(phoneNum);
			List<Integer> list = personalCenterService.getUserOrderStatus(id);
			resultMap.put("personDataMap", map);
			resultMap.put("orderStatusList", list);
			return ResultUtil.success(resultMap);
		} catch (Exception e) {
			LoggingUtil.e("个人中心数据初始化异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "个人中心数据初始化异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "根据token获取待付款订单", value = "待付款")
	@RequestMapping(value = "/PersonalCenter/itemsToBePaidFor/{token}", method = RequestMethod.GET)
	Result<Object> itemsToBePaidFor(@PathVariable("token") String token) throws BusinessException {
		return null;
	
	}
	
	@ApiOperation(httpMethod = "GET", notes = "根据token获取购物车信息", value = "我的购物车")
	@RequestMapping(value = "/PersonalCenter/getShoppingCartInfo/{token}", method = RequestMethod.GET)
	Result<Object> getShoppingCartInfo(@PathVariable("token") String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			//获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			//根据用户Id获取购物车信息
			
			List<ShoppingCart> shoppingCartList = personalCenterService.getShoppingCartInfo(userId);
			return ResultUtil.success(shoppingCartList);
		} catch (Exception e) {
			LoggingUtil.e("获取购物车信息异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取购物车信息异常");
		} 
	}
	
	@ApiOperation(httpMethod = "POST", notes = "根据token和shoppingCartId来删除", value = "删除购物车数据")
	@RequestMapping(value = "/PersonalCenter/delShoppingCartInfo/{token}/{shoppingCartId}", method = RequestMethod.POST)
	Result<Object> delShoppingCartById(@PathVariable("token") String token ,Integer shoppingCartId) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			//获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			//根据用户Id和shoppingCartId清理购物车数据
			if (personalCenterService.delShoppingCartInfoById(userId,shoppingCartId)) {
				return ResultUtil.success();
			}else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "shoppingCartId有误");
			}
		
		} catch (Exception e) {
			LoggingUtil.e("删除购物车物件异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "删除购物车物件异常");
		} 
	}
}
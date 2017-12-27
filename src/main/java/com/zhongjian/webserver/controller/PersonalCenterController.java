package com.zhongjian.webserver.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.common.ResultUtil;
import com.zhongjian.webserver.common.Status;
import com.zhongjian.webserver.common.TokenManager;
import com.zhongjian.webserver.dto.PANRequestMap;
import com.zhongjian.webserver.dto.PANResponseMap;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.PersonalCenterService;
import com.zhongjian.webserver.service.ProductManagerService;

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
	
	@Autowired
	ProductManagerService productManagerService;

	@ApiOperation(httpMethod = "GET", notes = "根据token获取个人中心数据", value = "初始化个人中心数据")
	@RequestMapping(value = "/PersonalCenter/initPersonalCenterData", method = RequestMethod.GET)
	Result<Object> initPersonalCenterData(@RequestParam String token) throws BusinessException {
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
//	@ApiOperation(httpMethod = "GET", notes = "根据token获取账户信息", value = "初始化个人中心数据")
//	@RequestMapping(value = "/PersonalCenter/initPersonalCenterData", method = RequestMethod.GET)
//	Result<Object> initPersonalCenterData(@RequestParam String token) throws BusinessException {
//		try {
//			// 检查token通过
//			String phoneNum = tokenManager.checkTokenGetUser(token);
//			if (phoneNum == null) {
//				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
//			}
//			HashMap<String, Object> resultMap = new HashMap<String, Object>();
//			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
//			Integer id = loginAndRegisterService.getUserIdByUserName(phoneNum);
//			List<Integer> list = personalCenterService.getUserOrderStatus(id);
//			resultMap.put("personDataMap", map);
//			resultMap.put("orderStatusList", list);
//			return ResultUtil.success(resultMap);
//		} catch (Exception e) {
//			LoggingUtil.e("个人中心数据初始化异常:" + e);
//			throw new BusinessException(Status.SeriousError.getStatenum(), "个人中心数据初始化异常");
//		}
//	}
	@ApiOperation(httpMethod = "POST", notes = "根据token获取该用户手机号", value = "根据token获取该用户手机号")
	@RequestMapping(value = "/PersonalCenter/getUserPhoneNum", method = RequestMethod.POST)
	Result<Object> initPersonalCenterData(@RequestBody Map<String, String> tokenMap) throws BusinessException {
		try {
			// receive the args
			String toKen = tokenMap.get("token");
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			return ResultUtil.success(phoneNum);
		} catch (Exception e) {
			LoggingUtil.e("根据token获取该用户手机号异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "根据token获取该用户手机号异常");
		}
	}
	@ApiOperation(httpMethod = "GET", notes = "根据token获取待付款订单", value = "待付款")
	@RequestMapping(value = "/PersonalCenter/itemsToBePaidFor", method = RequestMethod.GET)
	Result<Object> itemsToBePaidFor(@RequestParam String token) throws BusinessException {
		return null;
	
	}
	@ApiOperation(httpMethod = "GET", notes = "根据token获取购物车信息", value = "我的购物车")
	@RequestMapping(value = "/PersonalCenter/getShoppingCartInfo", method = RequestMethod.GET)
	Result<Object> getShoppingCartInfo(@RequestParam String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			//获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			//根据用户Id获取购物车信息
			List<HashMap<String, Object>> shoppingCartList = personalCenterService.getShoppingCartInfo(userId);
			return ResultUtil.success(shoppingCartList);
		} catch (Exception e) {
			LoggingUtil.e("获取购物车信息异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取购物车信息异常");
		} 
	}
	@ApiOperation(httpMethod = "POST", notes = "购物车新增", value = "新增购物车商品规格")
	@RequestMapping(value = "/PersonalCenter/updateShoppingCartInfo/{productId}/{SpecId}", method = RequestMethod.POST)
	Result<Object> addForShoppingCart(@PathVariable("productId") Integer productId,@PathVariable("SpecId") Integer specId, @RequestParam String token,@RequestParam Integer productNum) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			List<PANRequestMap> pANRequestMaps = new ArrayList<>();
			PANRequestMap pANRequestMap = new PANRequestMap();
			pANRequestMap.setProductId(productId);
			pANRequestMap.setProductNum(productNum);
			List<PANResponseMap> PANResponseMaps = productManagerService.checkProductStoreNum(pANRequestMaps);
			if (PANResponseMaps.size()>0) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "商品库存不够",PANResponseMaps);
			}
			//获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (personalCenterService.addShoppingCartInfo(userId, productId, specId,productNum) == 1) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "数据库添加购物车记录失败");
			}
		} catch (Exception e) {
			LoggingUtil.e("添加购物车记录异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "添加购物车记录异常");
		} 
	}
	@ApiOperation(httpMethod = "POST", notes = "购物车更新", value = "购物车具体物品数量更新")
	@RequestMapping(value = "/PersonalCenter/updateShoppingCartInfo/{shoppingCartId}", method = RequestMethod.POST)
	Result<Object> updateForShoppingCart(@PathVariable("shoppingCartId") Integer shoppingCartId, @RequestParam String token,@RequestParam Integer productNum) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			//根据shoppingCartId查出productId
			Integer productId = personalCenterService.getProductIdByShoppingId(shoppingCartId);
			List<PANRequestMap> pANRequestMaps = new ArrayList<>();
			PANRequestMap pANRequestMap = new PANRequestMap();
			pANRequestMap.setProductId(productId);
			pANRequestMap.setProductNum(productNum);
			List<PANResponseMap> PANResponseMaps = productManagerService.checkProductStoreNum(pANRequestMaps);
			if (PANResponseMaps.size()>0) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "商品库存不够",PANResponseMaps);
			}
			//获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (personalCenterService.setShoppingCartInfo(userId,shoppingCartId, productNum) == 1){
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "数据库添加购物车记录失败");
			}
		} catch (Exception e) {
			LoggingUtil.e("添加购物车记录异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "添加购物车记录异常");
		} 
	}
	@ApiOperation(httpMethod = "POST", notes = "根据token和shoppingCartId来删除", value = "删除购物车数据")
	@RequestMapping(value = "/PersonalCenter/delShoppingCartInfo", method = RequestMethod.POST)
	Result<Object> delShoppingCartById(@RequestParam String token ,Integer shoppingCartId) throws BusinessException {
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
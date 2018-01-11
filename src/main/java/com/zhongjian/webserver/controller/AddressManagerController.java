package com.zhongjian.webserver.controller;

import java.util.Date;
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
import com.zhongjian.webserver.mapper.UtilMapper;
import com.zhongjian.webserver.service.AddressManagerService;
import com.zhongjian.webserver.service.LoginAndRegisterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/AddressManager/", description = "收货地址管理")
public class AddressManagerController {

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private LoginAndRegisterService loginAndRegisterService;

	@Autowired
	private AddressManagerService addressManagerService;

	@Autowired
	private UtilMapper utilMapper;

	@ApiOperation(httpMethod = "GET", notes = "获取该用户所有收货地址", value = "获取该用户所有收货地址")
	@RequestMapping(value = "/AddressManager/getAllAddressOfUser/{token}", method = RequestMethod.GET)
	Result<Object> getAllAddressOfUser(@PathVariable String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			return ResultUtil.success(addressManagerService.getAllAddressByUserId(userId));
		} catch (Exception e) {
			LoggingUtil.e("获取该用户所有收货地址发生异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取该用户所有收货地址发生异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "根据ID获取收货地址详情", value = "根据ID获取收货地址详情")
	@RequestMapping(value = "/AddressManager/getAddressOfUserById/{token}/{id}", method = RequestMethod.GET)
	Result<Object> getAddressOfUserById(@PathVariable("token") String token, @PathVariable("id") Integer id) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			return ResultUtil.success(addressManagerService.getAddressById(id));
		} catch (Exception e) {
			LoggingUtil.e("获取收货地址详情发生异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取收货地址详情发生异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "获取用户默认收货地址", value = "获取用户默认收货地址")
	@RequestMapping(value = "/AddressManager/getDefaultAddressOfUser", method = RequestMethod.POST)
	Result<Object> getDefaultAddressOfUser(@RequestParam String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			return ResultUtil.success(addressManagerService.getDefaultAddressById(userId));
		} catch (Exception e) {
			LoggingUtil.e("获取用户默认收货地址异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取用户默认收货地址生异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "添加收货地址", value = "添加收货地址")
	@RequestMapping(value = "/AddressManager/addAddressByUser", method = RequestMethod.POST)
	Result<Object> addAddressByUser(@RequestBody Map<String, Object> addressMap) throws BusinessException {
		try {
			String token = (String) addressMap.get("token");
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			addressMap.put("UserId", userId);
			addressMap.put("CreateTime", new Date());
			// 添加收货地址
			addressManagerService.addAddress(addressMap);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("添加收货地址异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "添加收货地址发生异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "删除收货地址", value = "删除收货地址")
	@RequestMapping(value = "/AddressManager/deleteAddressById", method = RequestMethod.POST)
	Result<Object> deleteAddressById(@RequestBody Map<String, Object> map) throws BusinessException {
		try {
			String token = (String) map.get("token");
			Integer id = (Integer) map.get("id");
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 删除收货地址
			if (addressManagerService.deleteAddressById(id, userId) == 1) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "数据库删除收货地址记录失败,传入的Id有误");
			}
		} catch (Exception e) {
			LoggingUtil.e("删除收货地址发生异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "删除收货地址发生异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "更新收货地址", value = "更新收货地址")
	@RequestMapping(value = "/AddressManager/updateAddressById", method = RequestMethod.POST)
	Result<Object> updateAddressById(@RequestBody Map<String, Object> addressMap) throws BusinessException {
		try {
			String token = (String) addressMap.get("token");
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 更新收货地址
			addressMap.put("UserId", userId);
			addressManagerService.updateAddressById(addressMap);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("更新收货地址发生异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "更新收货地址发生异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "获取plist文件", value = "获取plist文件")
	@RequestMapping(value = "/AddressManager/getPlist", method = RequestMethod.GET)
	Result<Object> getPlist() {
		return ResultUtil.success(utilMapper.getPlist());

	}
}

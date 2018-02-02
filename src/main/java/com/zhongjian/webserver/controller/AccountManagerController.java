package com.zhongjian.webserver.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.zhongjian.webserver.service.AccountManagerService;
import com.zhongjian.webserver.service.LoginAndRegisterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/v1/AccountManager/", description = "体现账号绑定")
@RequestMapping(value = "/v1/AccountManager/")
public class AccountManagerController {

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private AccountManagerService accountManagerService;

	@Autowired
	private LoginAndRegisterService loginAndRegisterService;

	@ApiOperation(httpMethod = "POST", notes = "添加支付宝账号", value = "添加支付宝账号")
	@RequestMapping(value = "/AccountManager/AddAliAccount/{token}", method = RequestMethod.POST)
	Result<Object> addAliAccount(@PathVariable("token") String token, @RequestParam String account,
			@RequestParam String name) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他地方登录");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			accountManagerService.addAliAccount(userId, account, name);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("获取收货地址详情发生异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "添加支付宝账号");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "删除支付宝账号", value = "删除支付宝账号")
	@RequestMapping(value = "/AccountManager/DeleteAliAccount/{token}", method = RequestMethod.POST)
	Result<Object> deleteAliAccount(@PathVariable("token") String token, @RequestParam Integer id)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他地方登录");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (accountManagerService.deleteAliAccount(id, userId) == 1) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "支付宝账号删除失败");
			}

		} catch (Exception e) {
			LoggingUtil.e("删除支付宝账号异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "删除支付宝账号异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "添加银行卡账号", value = "添加银行卡账号")
	@RequestMapping(value = "/AccountManager/AddBankAccount/{token}", method = RequestMethod.POST)
	Result<Object> addBankAccount(@PathVariable("token") String token, @RequestParam String account,
			@RequestParam String bankName, @RequestParam String name) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他地方登录");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			accountManagerService.addBankAccount(userId, account, bankName, name);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("添加银行卡账号发生异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "添加银行卡账号发生异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "删除银行卡账号", value = "删除银行卡账号")
	@RequestMapping(value = "/AccountManager/DeleteBankAccount/{token}", method = RequestMethod.POST)
	Result<Object> deleteBankAccount(@PathVariable("token") String token, @RequestParam Integer id)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他地方登录");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (accountManagerService.deleteBankAccount(id, userId) == 1) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "删除银行卡账号失败");
			}

		} catch (Exception e) {
			LoggingUtil.e("删除银行卡账号异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "删除银行卡账号异常");
		}
	}

	
	@ApiOperation(httpMethod = "GET", notes = "获取体现账号", value = "获取体现账号")
	@RequestMapping(value = "/AccountManager/GetAllAccount/{token}", method = RequestMethod.GET)
	Result<Object> fAllAccount(@PathVariable("token") String token)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他地方登录");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put("aliAccount", accountManagerService.getAllAliAccount(userId));
			resultMap.put("bankAccount", accountManagerService.getAllBankAccount(userId));
			return ResultUtil.success(resultMap);
		} catch (Exception e) {
			LoggingUtil.e("获取体现账号号异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取体现账号异常");
		}
	}
}

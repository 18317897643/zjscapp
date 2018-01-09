package com.zhongjian.webserver.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.beanconfiguration.MallData;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.common.ResultUtil;
import com.zhongjian.webserver.common.Status;
import com.zhongjian.webserver.common.TokenManager;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.PersonalCenterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/MemberShip/", description = "会员以及分润相关")
@RequestMapping(value = "/MemberShip")
public class MemberShipController {
	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	LoginAndRegisterService loginAndRegisterService;
	
	@Autowired
	PersonalCenterService personalCenterService;
	
	@Autowired
	MallData mallData;
	/**
	 * 会员入口
	 * @param token
	 * @return Result<Object>
	 * @throws BusinessException
	 */
	@ApiOperation(httpMethod = "GET", notes = "会员升级界面初始数据", value = "会员升级界面初始数据")
	@RequestMapping(value = "/entrance/{token}", method = RequestMethod.GET)
	Result<Object> entrance(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			//获取用户等级信息
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			Integer lev = (Integer)map.get("Lev");
//			Integer IsSubProxy = (Integer)map.get("IsSubProxy");
			HashMap<String, HashMap<String, Integer>> result = new HashMap<>();
			HashMap<String, Integer> vipResult = new HashMap<>();
//			HashMap<String, Integer> subProxyResult = new HashMap<>();
			Integer vipNeedPay = mallData.getVipNeedPay();
//			Integer subProxyNeedPay = mallData.getSubProxyNeedPay();
//			if (lev == 0) {
//				vipResult.put("isExit", 1);
//				vipResult.put("needPay", vipNeedPay);
//				subProxyResult.put("isExit", 1);
//				subProxyResult.put("needPay", subProxyNeedPay);
//			}else if(lev == 1){
//				vipResult.put("isExit", 0);
//				subProxyResult.put("isExit", 1);
//				subProxyResult.put("needPay", subProxyNeedPay - vipNeedPay);
//			}else if(lev == 2 && IsSubProxy == 0){
//				vipResult.put("isExit", 0);
//				subProxyResult.put("isExit", 1);
//				subProxyResult.put("needPay", subProxyNeedPay - 9000);
//			}else {
//				vipResult.put("isExit", 0);
//				subProxyResult.put("isExit", 0);
//			}
			if (lev == 0) {
				vipResult.put("isExit", 1);
				vipResult.put("needPay", vipNeedPay);
			}
			result.put("VIP", vipResult);
//			result.put("SubProxy", subProxyResult);
			return ResultUtil.success(result);
		} catch (Exception e) {
			LoggingUtil.e("会员升级界面初始数据异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "会员升级界面初始数据异常");
		}
	}
	
	@ApiOperation(httpMethod = "GET", notes = "升级申请资料判断", value = "升级申请资料判断")
	@RequestMapping(value = "/isAlreadyApply/{token}", method = RequestMethod.GET)
	Result<Object> isAlreadyApply(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			return ResultUtil.success(personalCenterService.isAlreadyApply(UserId));
		} catch (Exception e) {
			LoggingUtil.e("升级申请资料判断异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "升级申请资料判断异常");
		}
	}
	
	@ApiOperation(httpMethod = "GET", notes = "已提交的申请资料获取", value = "已提交的申请资料获取")
	@RequestMapping(value = "/getProxyApply/{token}", method = RequestMethod.GET)
	Result<Object> getProxyApply(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			return ResultUtil.success(personalCenterService.getProxyApply(UserId));
		} catch (Exception e) {
			LoggingUtil.e("升级申请资料判断异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "升级申请资料判断异常");
		}
	}
	
	@ApiOperation(httpMethod = "POST", notes = "提交申请资料", value = "提交申请资料")
	@RequestMapping(value = "/proxyApply/{token}", method = RequestMethod.POST)
	Result<Object> proxyApply(@PathVariable("token") String toKen,@RequestBody HashMap<String, Object> map) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			return ResultUtil.success(personalCenterService.getProxyApply(UserId));
		} catch (Exception e) {
			LoggingUtil.e("升级申请资料判断异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "升级申请资料判断异常");
		}
	}
}

package com.zhongjian.webserver.controller;

import java.text.ParseException;

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
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.SignInService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/SignIn/", description = "签到模块")
@RequestMapping(value = "/SignIn")
public class SignInController {
	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private LoginAndRegisterService loginAndRegisterService;

	@Autowired
	private SignInService signInService;

	/**
	 * 获取签到数据
	 * 
	 * @param token
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(httpMethod = "GET", notes = "获取签到数据", value = "获取签到数据")
	@RequestMapping(value = "/init/{token}", method = RequestMethod.GET)
	public Result<Object> init(@PathVariable("token") String toKen)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			return ResultUtil.success(signInService.initSignData(UserId));
		} catch (Exception e) {
			LoggingUtil.e("获取签到数据异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取签到数据异常");
		}

	}
	/**
	 * 签到
	 * 
	 * @param token
	 * @return
	 * @throws BusinessException
	 * @throws ParseException
	 */
	@ApiOperation(httpMethod = "POST", notes = "签到", value = "签到")
	@RequestMapping(value = "/Signing/{token}", method = RequestMethod.POST)
	public Result<Object> Signing(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			// 获取UserId
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 签到核心逻辑
			if (signInService.Signing(UserId)) {
				return ResultUtil.success("签到成功");
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "今天你已经签到过了");
			}
		} catch (Exception e) {
			LoggingUtil.e("签到异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "签到异常");
		}

	}

	/**
	 * 领取奖励
	 * 
	 * @param token
	 * @param awardType
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(httpMethod = "POST", notes = "领取奖励", value = "领取奖励")
	@RequestMapping(value = "/drawAward/{token}", method = RequestMethod.POST)
	public Result<Object> drawAward(@PathVariable("token") String toKen, @RequestParam String awardType)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (signInService.markAreadyAward(UserId, awardType)) {
				return ResultUtil.success("领取成功");
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "领取错误");
			}
		} catch (Exception e) {
			LoggingUtil.e("领取签到奖励异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "领取签到奖励异常");
		}

	}
}

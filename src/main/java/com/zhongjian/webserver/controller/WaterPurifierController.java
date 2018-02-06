package com.zhongjian.webserver.controller;

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
import com.zhongjian.webserver.service.WaterPurifierService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/v1/waterPurifier/", description = "水机模块")
@RequestMapping(value = "/v1/waterPurifier")
public class WaterPurifierController {

	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private LoginAndRegisterService loginAndRegisterService;
	
	@Autowired
	private WaterPurifierService waterPurifierService;
	
	@ApiOperation(httpMethod = "POST", notes = "水机领取红包", value = "水机领取红包")
	@RequestMapping(value = "/drawCupon/{token}", method = RequestMethod.POST)
	public Result<Object> drawWaterPurifierCupon(@PathVariable("token") String toKen,@RequestParam String codeNo) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			String flag = waterPurifierService.drawWaterPurifierCupon(UserId, codeNo.toUpperCase());
			if ("0".equals(flag)) {
				return ResultUtil.success();
			}else if ("-1".equals(flag)) {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "无效的奖券");
			}else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "请输入正确的奖券");
			}
		} catch (Exception e) {
			LoggingUtil.e("水机领取红包异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "水机领取红包异常");
		}

	}

}

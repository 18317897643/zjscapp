package com.zhongjian.webserver.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.common.ExpiryMap;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.common.ResultUtil;
import com.zhongjian.webserver.common.Status;
import com.zhongjian.webserver.common.TokenManager;
import com.zhongjian.webserver.pojo.User;
import com.zhongjian.webserver.service.LoginAndRegisterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/LoginAndRegister/", description = "用户登录注册模块")
public class LoginAndRegisterController {

	@Autowired
	@Qualifier("payPasswordModifyMap")
	private ExpiryMap<String, String> payPasswordModifyMap;

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private LoginAndRegisterService loginAndRegisterService;

	@ApiOperation(httpMethod = "POST", notes = "发送验证码", value = "发送验证码")
	@RequestMapping(value = "/LoginAndRegister/SendRegisterVerifyCode", method = RequestMethod.POST)
	Result<Object> sendRegisterVerifyCode(@RequestBody Map<String, String> phoneMap) throws BusinessException {
		try {
			// receive the phone
			String phoneNum = phoneMap.get("phoneNum");
			loginAndRegisterService.sendRegisterVerifyCode(phoneNum);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("短信发送异常:" + e.getMessage());
			throw new BusinessException(Status.SMSError.getStatenum(), e.getMessage());
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "用户注册", value = "用户注册")
	@RequestMapping(value = "/LoginAndRegister/RegisterUser", method = RequestMethod.POST)
	Result<Object> registerUser(@RequestBody Map<String, String> userMap) throws BusinessException {
		try {
			// receive the args
			String phoneNum = userMap.get("phoneNum");
			if (!loginAndRegisterService.checkUserExists(phoneNum)) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "该用户已存在");
			}
			String password = userMap.get("password");
			String code = userMap.get("verifyCode");
			String inviteCode = userMap.get("inviteCode");
			Integer inviteCodeInteger = null;
			if (inviteCode != null) {
				inviteCodeInteger = Integer.parseInt(inviteCode);
			}
			if (loginAndRegisterService.checkVerifyCode(phoneNum, code)) {
				if (inviteCodeInteger == null) {
					loginAndRegisterService.registerUser(phoneNum, password, inviteCodeInteger);
				} else {
					if (loginAndRegisterService.InviteCodeIsExists(inviteCodeInteger)) {
						loginAndRegisterService.registerUser(phoneNum, password, inviteCodeInteger);
						loginAndRegisterService.sendCouponByInviteCode(new BigDecimal("100.00"), inviteCodeInteger);
					} else {
						return ResultUtil.error(Status.BussinessError.getStatenum(), "邀请码不存在");
					}
				}
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "验证码有误");
			}
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("注册异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "注册异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "用户登录", value = "用户登录")
	@RequestMapping(value = "/LoginAndRegister/UserLogin", method = RequestMethod.POST)
	Result<Object> userLogin(@RequestBody Map<String, String> userMap, HttpServletResponse response)
			throws BusinessException {
		try {
			// receive the args
			String phoneNum = userMap.get("phoneNum");
			String password = userMap.get("password");
			if (loginAndRegisterService.checkUserExists(phoneNum)) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "该用户不存在");
			}
			if (loginAndRegisterService.userIsFreeze(phoneNum)) {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "该用户已经被冻结");
			}
			if (!loginAndRegisterService.checkUserNameAndPassword(phoneNum, password)) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "密码输入有误");
			}
			// token获取
			String token = tokenManager.storeOrUpdateToken(phoneNum);
			return ResultUtil.success(token);
		} catch (Exception e) {
			LoggingUtil.e("登录异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "登录异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "密码修改", value = "密码修改")
	@RequestMapping(value = "/LoginAndRegister/modifyPassword", method = RequestMethod.POST)
	Result<Object> modifyPassword(@RequestBody Map<String, String> passwordMap) throws BusinessException {
		try {
			// receive the args
			String toKen = passwordMap.get("token");
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			String oldPassword = passwordMap.get("oldPassword");
			String newPassword = passwordMap.get("newPassword");
			String newPasswordAgain = passwordMap.get("newPasswordAgain");
			if (loginAndRegisterService.checkUserNameAndPassword(phoneNum, oldPassword)) {
				if (newPassword.equals(newPasswordAgain)) {
					loginAndRegisterService.modifyPassword(phoneNum, newPassword);
					return ResultUtil.success();
				} else {
					return ResultUtil.error(Status.GeneralError.getStatenum(), "两次输入的新密码不一致");
				}
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "原密码输入有误");
			}
		} catch (Exception e) {
			LoggingUtil.e("修改密码异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "修改密码异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "忘记密码后重新设置密码", value = "忘记密码")
	@RequestMapping(value = "/LoginAndRegister/forgetPassword", method = RequestMethod.POST)
	Result<Object> forgetPassword(@RequestBody Map<String, String> userMap) throws BusinessException {
		try {
			// receive the args
			String phoneNum = userMap.get("phoneNum");
			String verifyCode = userMap.get("verifyCode");
			String password = userMap.get("password");
			String passwordAgain = userMap.get("passwordAgain");
			if (loginAndRegisterService.checkVerifyCode(phoneNum, verifyCode)) {
				if (password.equals(passwordAgain)) {
					loginAndRegisterService.modifyPassword(phoneNum, password);
					return ResultUtil.success();
				} else {
					return ResultUtil.error(Status.GeneralError.getStatenum(), "两次输入的新密码不一致");
				}
			} else {
				return ResultUtil.error(Status.SeriousError.getStatenum(), "验证码校验异常");
			}
		} catch (Exception e) {
			LoggingUtil.e("忘记密码相关操作发生异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "忘记密码相关操作发生异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "发送验证码并登录", value = "手机动态密码登录")
	@RequestMapping(value = "/LoginAndRegister/dynamicLogin", method = RequestMethod.POST)
	Result<Object> dynamicLogin(@RequestBody Map<String, String> userMap) throws BusinessException {
		// receive the args
		try {
			String phoneNum = userMap.get("phoneNum");
			String verifyCode = userMap.get("verifyCode");
			if (!loginAndRegisterService.checkUserExists(phoneNum)) {
				if (loginAndRegisterService.checkVerifyCode(phoneNum, verifyCode)) {
					// token获取
					String token = tokenManager.storeOrUpdateToken(phoneNum);
					return ResultUtil.success(token);
				} else {
					return ResultUtil.error(Status.SeriousError.getStatenum(), "验证码校验异常");
				}
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "该用户不存在");
			}
		} catch (Exception e) {
			LoggingUtil.e("手机动态登录异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "手机动态登录异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "支付密码设置校验验证码", value = "支付密码设置校验验证码")
	@RequestMapping(value = "/LoginAndRegister/verify", method = RequestMethod.POST)
	Result<Object> setPayPassword(@RequestBody Map<String, String> userMap) throws BusinessException {
		try {
			// receive the args
			String toKen = userMap.get("token");
			String verifyCode = userMap.get("verifyCode");
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			if (loginAndRegisterService.checkVerifyCode(phoneNum, verifyCode)) {
				// 更改支付密码凭证
				String payPasswordCertificate = UUID.randomUUID().toString();
				payPasswordModifyMap.put(toKen, payPasswordCertificate);
				return ResultUtil.success(payPasswordCertificate);
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "验证码错误");
			}
		} catch (Exception e) {
			LoggingUtil.e("支付密码校验验证码异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "支付密码校验验证码异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "支付密码设置", value = "支付密码设置")
	@RequestMapping(value = "/LoginAndRegister/verify/{payPasswordCertificate}", method = RequestMethod.POST)
	Result<Object> setPayPassword(@PathVariable("payPasswordCertificate") String payPasswordCertificate,
			@RequestBody User user) throws BusinessException {
		try {
			// receive the args
			String toKen = user.getToken();
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			// 如果凭证不对
			if (!payPasswordCertificate.equals(payPasswordModifyMap.get(toKen))) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "凭证已失效");
			}
			payPasswordModifyMap.remove(toKen);
			User userForUpdate = new User();
			userForUpdate.setPaypassword(user.getPaypassword());
			userForUpdate.setUsername(phoneNum);
			loginAndRegisterService.updateUser(userForUpdate);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("支付密码设置异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "支付密码设置异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "退出登录", value = "退出登录")
	@RequestMapping(value = "/LoginAndRegister/logout", method = RequestMethod.POST)
	Result<Object> logout(@RequestBody Map<String, String> tokenMap) throws BusinessException {
		try {
			// receive the args
			String toKen = tokenMap.get("token");
			tokenManager.releaseToken(toKen);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("支付密码设置异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "支付密码设置异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "剔除token", value = "剔除token")
	@RequestMapping(value = "/LoginAndRegister/getRidOf/{userId}", method = RequestMethod.POST)
	Result<Object> logout(@PathVariable("userId") Integer userId) throws BusinessException {
		try {
			tokenManager.releaseUserName(loginAndRegisterService.getUserNameByUserId(userId));
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("剔除token异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "剔除token异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "推送", value = "推送")
	@RequestMapping(value = "/LoginAndRegister/Jpush/{userId}", method = RequestMethod.POST)
	Result<Object> logout(@PathVariable("userId") Integer userId,@RequestParam String message) throws BusinessException {
		try {
			loginAndRegisterService.jPush(userId, message);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("推送异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "推送异常");
		}
	}
}
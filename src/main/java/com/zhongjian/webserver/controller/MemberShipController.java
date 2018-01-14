package com.zhongjian.webserver.controller;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.zhongjian.webserver.component.AsyncTasks;
import com.zhongjian.webserver.component.MallData;
import com.zhongjian.webserver.service.CoreService;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.MemberShipService;
import com.zhongjian.webserver.service.OrderHandleService;
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
	MemberShipService memberShipService;

	@Autowired 
	OrderHandleService orderHandleService;
	
	@Autowired
	MallData mallData;

	@Autowired
	CoreService coreService;
	
	@Autowired
	AsyncTasks tasks;

	/**
	 * 会员入口
	 * 
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

			// 获取用户等级信息
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			Integer lev = (Integer) map.get("Lev");
			HashMap<String, HashMap<String, Integer>> result = new HashMap<>();
			HashMap<String, Integer> vipResult = new HashMap<>();
			HashMap<String, Integer> greenChanelResult = new HashMap<>();
			Integer vipNeedPay = mallData.getVipNeedPay();
			Integer gcNeedPay = mallData.getGcNeedPay();
			if (lev == 0) {
				vipResult.put("isExit", 1);
				vipResult.put("id", 1);
				vipResult.put("needPay", vipNeedPay);
				Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
				if (personalCenterService.isGCMember(UserId)) {
					greenChanelResult.put("isExit", 0);
				} else {
					greenChanelResult.put("isExit", 1);
					greenChanelResult.put("id", 2);
					greenChanelResult.put("gcNeedPay", gcNeedPay);
				}
			} else {
				vipResult.put("isExit", 0);
				greenChanelResult.put("isExit", 0);
			}
			result.put("GC", greenChanelResult);
			result.put("VIP", vipResult);
			return ResultUtil.success(result);
		} catch (Exception e) {
			LoggingUtil.e("会员升级界面初始数据异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "会员升级界面初始数据异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "生成VIP订单", value = "生成VIP订单")
	@RequestMapping(value = "/createVOrder/{token}", method = RequestMethod.POST)
	Result<Object> createVOrder(@PathVariable("token") String toKen, @RequestParam Integer type,Integer lev)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			// recive args
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// type=0现金支付 type =1 支付宝支付
			// lev=0绿色通道 lev=1 vip通道
			BigDecimal needPay = null;
			if (lev == 0) {
				needPay = new BigDecimal(mallData.getGcNeedPay());
			} else {
				needPay = new BigDecimal(mallData.getVipNeedPay());
			}
			if (type == 0) {
				BigDecimal reaminElec = (BigDecimal) personalCenterService.getInformOfConsumption(phoneNum);
				if (needPay.compareTo(reaminElec) == 1) {
					return ResultUtil.error(Status.GeneralError.getStatenum(), "余额不足");
				}
				Map<String, Object> map = memberShipService.createVOrder(lev,needPay, UserId, type);
				String orderNo = (String) map.get("OrderNo");
				// vip订单购买成功
				return ResultUtil.success(orderNo);
			} else  {
				//支付宝支付
				Map<String, Object> map = memberShipService.createVOrder(lev,needPay, UserId, type);
				return ResultUtil.success(orderHandleService.createAliSignature((String) map.get("OrderNo"), ((BigDecimal)map.get("TolAmout")).toString()));
			}
		} catch (Exception e) {
			LoggingUtil.e("生成VIP订单并支付异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "生成VIP订单并支付异常");
		}
	}
	
	@ApiOperation(httpMethod = "POST", notes = "现金支付处理vip", value = "现金支付处理vip")
	@RequestMapping(value = "/syncHandleVipOrder/{token}", method = RequestMethod.POST)
	Result<Object> syncHandleVipOrder(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			// recive args
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			memberShipService.syncHandleVipOrder(UserId, orderNo);
			return null;
		} catch (Exception e) {
			LoggingUtil.e("生成VIP订单并支付异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "生成VIP订单并支付异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "生成充值现金币订单", value = "生成充值现金币订单")
	@RequestMapping(value = "/createCOrder/{token}", method = RequestMethod.POST)
	Result<Object> createCOrder(@PathVariable("token") String toKen, @RequestParam BigDecimal money)
			throws BusinessException {
		try {
			if (money.compareTo(BigDecimal.ZERO) == -1) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "金额不能小于零");
			}
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
		    //生成订单
			String orderNo = memberShipService.createCOrder(UserId, money);
			return ResultUtil.success(orderHandleService.createAliSignature(orderNo, money.toString()));
		} catch (Exception e) {
			LoggingUtil.e("生成充值现金币订单异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "生成充值现金币订单异常");
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
	Result<Object> proxyApply(@PathVariable("token") String toKen, @RequestBody HashMap<String, Object> map)
			throws BusinessException {
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

	@ApiOperation(httpMethod = "POST", notes = "分润接口", value = "分润接口")
	@RequestMapping(value = "/shareBenit", method = RequestMethod.POST)
	Result<Object> shareBenit(@RequestParam Integer type, @RequestParam Integer masterUserId, @RequestParam String memo,
			@RequestParam BigDecimal ElecNum) throws BusinessException {
		try {
			if (type != 1 && type != 2) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "参数异常");
			}
			if (!loginAndRegisterService.checkUserIdExits(masterUserId)) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "用户不存在，请好好传");
			}
			tasks.shareBenitTask(type, masterUserId, 0, memo, ElecNum);
			return ResultUtil.success();
		} catch (Exception e) {
			throw new BusinessException(Status.SeriousError.getStatenum(), "分润接口异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "升级到vip或准代或代理时产生赠送名额", value = "升级到vip或准代或代理时产生赠送名额")
	@RequestMapping(value = "/present", method = RequestMethod.POST)
	Result<Object> present(@RequestParam Integer type, @RequestParam Integer userId) throws BusinessException {
		// 1为vip 2准代和代理
		try {
			if (type != 1 && type != 2) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "参数异常");
			}
			tasks.presentTask(type, userId);
			return ResultUtil.success();
		} catch (Exception e) {
			throw new BusinessException(Status.SeriousError.getStatenum(), "升级到vip或准代或代理时产生赠送名额异常");
		}
	}
	@ApiOperation(httpMethod = "GET", notes = "根据众健号搜索会员", value = "根据众健号搜索会员")
	@RequestMapping(value = "/getMemberBySysId", method = RequestMethod.GET)
	Result<Object> getMemberBySysId(@RequestParam Integer type, @RequestParam Integer userId) throws BusinessException {
		try {
			return null;
		} catch (Exception e) {
			throw new BusinessException(Status.SeriousError.getStatenum(), "根据众健号搜索会员异常");
		}
		
	}
}

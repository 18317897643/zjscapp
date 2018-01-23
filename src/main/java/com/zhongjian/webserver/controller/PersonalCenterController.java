package com.zhongjian.webserver.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.zhongjian.webserver.dto.OrderHeadDto;
import com.zhongjian.webserver.dto.OrderHeadEXDto;
import com.zhongjian.webserver.dto.PANRequestMap;
import com.zhongjian.webserver.dto.PANResponseMap;
import com.zhongjian.webserver.pojo.Orderhead;
import com.zhongjian.webserver.pojo.User;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.OrderHandleService;
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

	@Autowired
	OrderHandleService orderHandleService;

	@ApiOperation(httpMethod = "GET", notes = "根据token获取个人中心数据", value = "初始化个人中心数据")
	@RequestMapping(value = "/PersonalCenter/initPersonalCenterData/{token}", method = RequestMethod.GET)
	Result<Object> initPersonalCenterData(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			Integer id = loginAndRegisterService.getUserIdByUserName(phoneNum);
			List<Integer> list = personalCenterService.getUserOrderStatus(id);
			Integer lev = (Integer) map.get("Lev");
			Integer IsSubProxy = (Integer) map.get("IsSubProxy");
			if (lev == 3) {
				resultMap.put("canDistribute", 1);
			} else {
				resultMap.put("canDistribute", 0);
			}
			HashMap<String, Object> proxyApply = new HashMap<>();
			if (lev == 2 && IsSubProxy == 1 || lev == 3) {
				proxyApply.put("canProxyApply", 1);
				Boolean isAlreadyApply = personalCenterService.isAlreadyApply(id);
				if (isAlreadyApply) {
					proxyApply.put("isAlreadyApply", 1);
				} else {
					proxyApply.put("isAlreadyApply", 0);
				}
			} else {
				proxyApply.put("canProxyApply", 0);
			}
			resultMap.put("ProxyApply", proxyApply);
			resultMap.put("personDataMap", map);
			resultMap.put("orderStatusList", list);
			return ResultUtil.success(resultMap);
		} catch (Exception e) {
			LoggingUtil.e("个人中心数据初始化异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "个人中心数据初始化异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "我的钱包", value = "我的钱包数据")
	@RequestMapping(value = "/PersonalCenter/initPersonalWallet/{token}", method = RequestMethod.GET)
	Result<Object> initPersonalWallet(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			boolean isGCmemeber = personalCenterService.isGCMember(UserId);
			resultMap.put("personDataMap", map);
			resultMap.put("isGCmemeber", isGCmemeber);
			return ResultUtil.success(resultMap);
		} catch (Exception e) {
			LoggingUtil.e("我的钱包异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "我的钱包异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "根据token获取该用户手机号", value = "根据token获取该用户手机号")
	@RequestMapping(value = "/PersonalCenter/getUserPhoneNum/{token}", method = RequestMethod.POST)
	Result<Object> getUserPhoneNum(@PathVariable("token") String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			return ResultUtil.success(phoneNum);
		} catch (Exception e) {
			LoggingUtil.e("根据token获取该用户手机号异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "根据token获取该用户手机号异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "获取订单", value = "获取订单")
	@RequestMapping(value = "/PersonalCenter/getOrder/{token}/{type}", method = RequestMethod.GET)
	Result<Object> itemsToBePaidFor(@PathVariable("token") String token, @PathVariable("type") String type,
			@RequestParam Integer page, @RequestParam Integer pageNum) throws BusinessException {
		try {
			// all全部 wp待付款ws待发货wr待收货wc待评价
			if (!"all".equals(type) && !"wp".equals(type) && !"ws".equals(type) && !"wr".equals(type)
					&& !"wc".equals(type)) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "参数异常");
			}
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			List<Orderhead> orderHead = null;
			if ("all".equals(type)) {
				orderHead = personalCenterService.getOrderDetailsByCurStatus(UserId, "", page, pageNum);
			} else if ("wp".equals(type)) {
				orderHead = personalCenterService.getOrderDetailsByCurStatus(UserId, "and CurStatus = -1", page,
						pageNum);
			} else if ("ws".equals(type)) {
				orderHead = personalCenterService.getOrderDetailsByCurStatus(UserId, "and CurStatus = 0", page,
						pageNum);
			} else if ("wr".equals(type)) {
				orderHead = personalCenterService.getOrderDetailsByCurStatus(UserId, "and CurStatus = 1", page,
						pageNum);
			} else {
				orderHead = personalCenterService.getOrderDetailsByCurStatus(UserId, "and CurStatus = 2", page,
						pageNum);
			}
			return ResultUtil.success(orderHead);
		} catch (Exception e) {
			LoggingUtil.e("获取订单异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取订单异常");
		}

	}

	@ApiOperation(httpMethod = "GET", notes = "获取订单详情", value = "获取订单详情")
	@RequestMapping(value = "/PersonalCenter/getOrderDetails/{token}/{orderId}", method = RequestMethod.GET)
	Result<Object> getOrderDetails(@PathVariable("token") String token, @PathVariable("orderId") Integer orderId)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			return ResultUtil.success(personalCenterService.getOrderDetailsById(orderId));
		} catch (Exception e) {
			LoggingUtil.e("获取订单详情异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取订单详情异常");
		}
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
			// 获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 根据用户Id获取购物车信息
			List<HashMap<String, Object>> shoppingCartList = personalCenterService.getShoppingCartInfo(userId);
			return ResultUtil.success(shoppingCartList);
		} catch (Exception e) {
			LoggingUtil.e("获取购物车信息异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取购物车信息异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "购物车新增", value = "新增购物车商品规格")
	@RequestMapping(value = "/PersonalCenter/updateShoppingCartInfo/{productId}/{SpecId}", method = RequestMethod.POST)
	Result<Object> addForShoppingCart(@PathVariable("productId") Integer productId,
			@PathVariable("SpecId") Integer specId, @RequestParam String token, @RequestParam Integer productNum)
			throws BusinessException {
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
			if (PANResponseMaps.size() > 0) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "商品库存不够", PANResponseMaps);
			}
			// 获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			personalCenterService.addShoppingCartInfo(userId, productId, specId, productNum, new Date());
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("添加购物车记录异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "添加购物车记录异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "购物车更新", value = "购物车具体物品数量更新")
	@RequestMapping(value = "/PersonalCenter/updateShoppingCartInfo/{shoppingCartId}", method = RequestMethod.POST)
	Result<Object> updateForShoppingCart(@PathVariable("shoppingCartId") Integer shoppingCartId,
			@RequestParam String token, @RequestParam Integer productNum) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			// 根据shoppingCartId查出productId
			Integer productId = personalCenterService.getProductIdByShoppingId(shoppingCartId);
			List<PANRequestMap> pANRequestMaps = new ArrayList<>();
			PANRequestMap pANRequestMap = new PANRequestMap();
			pANRequestMap.setProductId(productId);
			pANRequestMap.setProductNum(productNum);
			List<PANResponseMap> PANResponseMaps = productManagerService.checkProductStoreNum(pANRequestMaps);
			if (PANResponseMaps.size() > 0) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "商品库存不够", PANResponseMaps);
			}
			// 获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (personalCenterService.setShoppingCartInfo(userId, shoppingCartId, productNum) == 1) {
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
	Result<Object> delShoppingCartById(@RequestParam String token, Integer shoppingCartId) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			// 获取用户Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 根据用户Id和shoppingCartId清理购物车数据
			if (personalCenterService.delShoppingCartInfoById(userId, shoppingCartId)) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "shoppingCartId有误");
			}

		} catch (Exception e) {
			LoggingUtil.e("删除购物车物件异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "删除购物车物件异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "更新头像", value = "更新头像")
	@RequestMapping(value = "/PersonalCenter/updateHeadPhoto/{token}", method = RequestMethod.POST)
	Result<Object> updateHeadPhoto(@PathVariable("token") String toKen, @RequestParam String headPhoto)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			User userForUpdate = new User();
			userForUpdate.setHeadphoto(headPhoto);
			userForUpdate.setUsername(phoneNum);
			loginAndRegisterService.updateUser(userForUpdate);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("更新头像异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "更新头像异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "更新昵称", value = "更新昵称")
	@RequestMapping(value = "/PersonalCenter/updateNickName/{token}", method = RequestMethod.POST)
	Result<Object> updateNickName(@PathVariable("token") String toKen, @RequestParam String nickName)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			User userForUpdate = new User();
			userForUpdate.setNickname(nickName);
			userForUpdate.setUsername(phoneNum);
			loginAndRegisterService.updateUser(userForUpdate);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("更新昵称异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "更新昵称异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "验证支付密码", value = "验证支付密码")
	@RequestMapping(value = "/PersonalCenter/verifyPayPassword", method = RequestMethod.POST)
	Result<Object> verifyPayPassword(@RequestParam String toKen, String payPassword) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			if (loginAndRegisterService.userFundsIsFreeze(phoneNum)) {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "平台币值已冻结");
			}
			if (loginAndRegisterService.checkUserNameAndPayPassword(phoneNum, payPassword)) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "请输入正确的支付密码");
			}
		} catch (Exception e) {
			LoggingUtil.e("验证支付密码:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "验证支付密码异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "设置推荐人", value = "设置推荐人")
	@RequestMapping(value = "/PersonalCenter/updateInviteCode/{token}", method = RequestMethod.POST)
	Result<Object> updateInviteCode(@PathVariable("token") String toKen, @RequestParam Integer inviteCode)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			// 判断一下inviteCode是否存在系统
			if (loginAndRegisterService.InviteCodeIsExists(inviteCode)) {
				User userForUpdate = new User();
				userForUpdate.setBeinvitecode(inviteCode);
				userForUpdate.setUsername(phoneNum);
				loginAndRegisterService.updateUser(userForUpdate);
				loginAndRegisterService.sendCouponByUserId(new BigDecimal("100.00"), inviteCode);
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "邀请码不存在");
			}

		} catch (Exception e) {
			LoggingUtil.e("设置推荐人异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "设置推荐人异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "生成购物订单", value = "生成购物订单")
	@RequestMapping(value = "/PersonalCenter/createBOrder/{token}", method = RequestMethod.POST)
	Result<Object> createBOrder(@PathVariable("token") String toKen, @RequestBody OrderHeadEXDto orderHeadEXDto)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// result中有总金额和单号，再去拼接签名返回给客户端
			List<OrderHeadDto> orderHeadDtos = orderHandleService.handleOrderHeadDtoByAdressId(orderHeadEXDto);
			if (orderHeadDtos == null) {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "收货地址ID不存在");
			}
			HashMap<String, Object> result = orderHandleService.createOrder(orderHeadDtos, UserId);
			HashMap<String, Object> exceptResult = new HashMap<>();
			BigDecimal totalRealPayCo = (BigDecimal) result.get("totalRealPayCo");
			BigDecimal totalNotRealPayCo = (BigDecimal) result.get("totalNotRealPayCo");
			String orderNoCollectionName = (String) result.get("orderNoCollectionName");
			if (totalRealPayCo.compareTo(BigDecimal.ZERO) == 0) {
				// 不需要通过支付宝付款
				exceptResult.put("type", "1");
				exceptResult.put("orderNoC", orderNoCollectionName);
			} else {
				if (totalNotRealPayCo.compareTo(BigDecimal.ZERO) == 0) {
					// 不需要平台币值
					exceptResult.put("type", "2");
					exceptResult.put("singData",
							orderHandleService.createAliSignature(orderNoCollectionName, totalRealPayCo.toString()));
				} else {
					// 混合支付
					exceptResult.put("type", "3");
					exceptResult.put("singData",
							orderHandleService.createAliSignature(orderNoCollectionName, totalRealPayCo.toString()));
				}
			}
			return ResultUtil.success(exceptResult);
		} catch (RuntimeException e) {
			throw new BusinessException(Status.GeneralError.getStatenum(), e.getMessage());
		} catch (Exception e) {
			LoggingUtil.e("生成订单异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "生成订单异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "同步处理支付父订单", value = "同步处理支付父订单")
	@RequestMapping(value = "/PersonalCenter/syncHandleOrderC/{token}", method = RequestMethod.POST)
	Result<Object> syncHandleOrderC(@PathVariable("token") String toKen, @RequestParam String orderNoC)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 通过订单查询
			if (UserId == orderHandleService.getUserIdByOrderC(orderNoC)) {
				// 继续处理订单
				// 直接订单同步处理
				if (orderHandleService.syncHandleOrder(orderNoC)) {
					return ResultUtil.success();
				} else {
					return ResultUtil.error(Status.GeneralError.getStatenum(), "金额有误或是订单已支付过了");
				}
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "您确定是该用户生成的订单吗");
			}

		} catch (Exception e) {
			LoggingUtil.e("支付订单异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "支付订单异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "同步处理支付子订单", value = "同步处理支付子订单")
	@RequestMapping(value = "/PersonalCenter/syncHandleOrder/{token}", method = RequestMethod.POST)
	Result<Object> syncHandleOrder(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 通过订单查询
			if (UserId == orderHandleService.getUserIdByOrder(orderNo)) {
				// 继续处理订单
				// 直接订单同步处理
				if (orderHandleService.syncHandleOrder(orderNo)) {
					return ResultUtil.success();
				} else {
					return ResultUtil.error(Status.GeneralError.getStatenum(), "金额有误或是订单已支付过了");
				}
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "您确定是该用户生成的订单吗");
			}

		} catch (Exception e) {
			LoggingUtil.e("支付订单异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "支付订单异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "查看该用户是否领取新人红包", value = "查看该用户是否领取新人红包")
	@RequestMapping(value = "/PersonalCenter/checkNewExclusive/{token}", method = RequestMethod.GET)
	Result<Object> checkNewExclusive(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (loginAndRegisterService.userNewExclusiveIsDraw(userId)) {
				// 领过了
				return ResultUtil.success(1);
			} else {
				// 可以领取
				return ResultUtil.success(0);
			}
		} catch (Exception e) {
			LoggingUtil.e("查看该用户是否领取新人红包异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "查看该用户是否领取新人红包异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "领取新人红包", value = "领取新人红包")
	@RequestMapping(value = "/PersonalCenter/drawNewExclusive/{token}", method = RequestMethod.POST)
	Result<Object> drawNewExclusive(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (loginAndRegisterService.drawNewExclusive(userId) == 1) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "已经领取过");
			}
		} catch (Exception e) {
			LoggingUtil.e("领取新人红包异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "领取新人红包异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "金额明细", value = "金额明细")
	@RequestMapping(value = "/PersonalCenter/accountBill/{token}", method = RequestMethod.GET)
	Result<Object> accountBill(@PathVariable("token") String toKen, @RequestParam String type,
			@RequestParam Integer page, @RequestParam Integer pageNum) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 业务层返回各类型消费明细
			return ResultUtil.success(personalCenterService.accountBill(UserId, type, page, pageNum));
		} catch (Exception e) {
			LoggingUtil.e("金额明细异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "金额明细异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "取消订单", value = "取消订单")
	@RequestMapping(value = "/PersonalCenter/cancelOrder/{token}", method = RequestMethod.POST)
	Result<Object> accountBill(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (UserId == orderHandleService.getUserIdByOrder(orderNo)) {
				orderHandleService.cancelOrder(orderNo);
			}
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("取消订单异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "取消订单异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "确认收货", value = "确认收货")
	@RequestMapping(value = "/PersonalCenter/confirmOrder/{token}", method = RequestMethod.POST)
	Result<Object> confirmOrder(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (UserId == orderHandleService.getUserIdByOrder(orderNo)) {
				orderHandleService.confirmOrder(orderNo);
			}
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("确认收货异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "确认收货异常");
		}
	}
	@ApiOperation(httpMethod = "POST", notes = "现金体现", value = "现金体现")
	@RequestMapping(value = "/PersonalCenter/TxElecNum/{token}", method = RequestMethod.POST)
	Result<Object> txElecNum(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (UserId == orderHandleService.getUserIdByOrder(orderNo)) {
				orderHandleService.cancelOrder(orderNo);
			}
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("现金体现异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "现金体现异常");
		}
	}
}
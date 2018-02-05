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
import com.zhongjian.webserver.common.DateUtil;
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
import com.zhongjian.webserver.service.OrderApplyService;
import com.zhongjian.webserver.service.OrderHandleService;
import com.zhongjian.webserver.service.PersonalCenterService;
import com.zhongjian.webserver.service.ProductManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/v1/PersonalCenter/", description = "个人中心")
@RequestMapping(value = "/v1/PersonalCenter")
public class PersonalCenterController {

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private PersonalCenterService personalCenterService;

	@Autowired
	private LoginAndRegisterService loginAndRegisterService;

	@Autowired
	private ProductManagerService productManagerService;

	@Autowired
	private OrderHandleService orderHandleService;

	@Autowired
	private OrderApplyService orderApplyService;

	@ApiOperation(httpMethod = "GET", notes = "根据token获取个人中心数据", value = "初始化个人中心数据")
	@RequestMapping(value = "/initPersonalCenterData/{token}", method = RequestMethod.GET)
	Result<Object> initPersonalCenterData(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			Integer id = loginAndRegisterService.getUserIdByUserName(phoneNum);
			List<Integer> list = personalCenterService.getUserOrderStatus(id);
			Integer lev = (Integer) map.get("Lev");
			Integer IsSubProxy = (Integer) map.get("IsSubProxy");
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
	@RequestMapping(value = "/initPersonalWallet/{token}", method = RequestMethod.GET)
	Result<Object> initPersonalWallet(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			boolean isGCmember = personalCenterService.isGCMember(UserId);
			resultMap.put("personDataMap", map);
			resultMap.put("isGCmember", isGCmember);
			if (isGCmember) {
				resultMap.put("GCmemberExpireTime", DateUtil.DateToStr(personalCenterService.getGCMemberExpireTime(UserId)));
			}
			return ResultUtil.success(resultMap);
		} catch (Exception e) {
			LoggingUtil.e("我的钱包异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "我的钱包异常");
		}
	}

	
	@ApiOperation(httpMethod = "GET", notes = "查询账户个人信息", value = "查询账户个人信息")
	@RequestMapping(value = "/GetPersonalInfo/{token}", method = RequestMethod.GET)
	Result<Object> getPersonalInfo(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			resultMap.put("personDataMap", map);
			return ResultUtil.success(resultMap);
		} catch (Exception e) {
			LoggingUtil.e("查询账户个人信息异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "查询账户个人信息异常");
		}
	}
	@ApiOperation(httpMethod = "POST", notes = "根据token获取该用户手机号", value = "根据token获取该用户手机号")
	@RequestMapping(value = "/getUserPhoneNum/{token}", method = RequestMethod.POST)
	Result<Object> getUserPhoneNum(@PathVariable("token") String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			return ResultUtil.success(phoneNum);
		} catch (Exception e) {
			LoggingUtil.e("根据token获取该用户手机号异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "根据token获取该用户手机号异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "获取订单", value = "获取订单")
	@RequestMapping(value = "/getOrder/{token}/{type}", method = RequestMethod.GET)
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
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/getOrderDetails/{token}/{orderId}", method = RequestMethod.GET)
	Result<Object> getOrderDetails(@PathVariable("token") String token, @PathVariable("orderId") Integer orderId)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/updateShoppingCartInfo/{productId}/{SpecId}", method = RequestMethod.POST)
	Result<Object> addForShoppingCart(@PathVariable("productId") Integer productId,
			@PathVariable("SpecId") Integer specId, @RequestParam String token, @RequestParam Integer productNum)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
			if (personalCenterService.addShoppingCartInfo(userId, productId, specId, productNum, new Date())) {
				return ResultUtil.success();
			}else {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "未达到起售数量");
			}

		} catch (Exception e) {
			LoggingUtil.e("添加购物车记录异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "添加购物车记录异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "购物车更新", value = "购物车具体物品数量更新")
	@RequestMapping(value = "/updateShoppingCartInfo/{shoppingCartId}", method = RequestMethod.POST)
	Result<Object> updateForShoppingCart(@PathVariable("shoppingCartId") Integer shoppingCartId,
			@RequestParam String token, @RequestParam Integer productNum) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
			personalCenterService.setShoppingCartInfo(userId, shoppingCartId, productNum);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("添加购物车记录异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "添加购物车记录异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "根据token和shoppingCartId来删除", value = "删除购物车数据")
	@RequestMapping(value = "/delShoppingCartInfo", method = RequestMethod.POST)
	Result<Object> delShoppingCartById(@RequestParam String token, Integer shoppingCartId) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/updateHeadPhoto/{token}", method = RequestMethod.POST)
	Result<Object> updateHeadPhoto(@PathVariable("token") String toKen, @RequestParam String headPhoto)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/updateNickName/{token}", method = RequestMethod.POST)
	Result<Object> updateNickName(@PathVariable("token") String toKen, @RequestParam String nickName)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/verifyPayPassword", method = RequestMethod.POST)
	Result<Object> verifyPayPassword(@RequestParam String toKen, String payPassword) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			if (loginAndRegisterService.userFundsIsFreeze(phoneNum)) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "平台币值已冻结");
			}
			String flag = loginAndRegisterService.checkUserNameAndPayPassword(phoneNum, payPassword);
			if ("0".equals(flag)) {
				return ResultUtil.success();
			} else if ("2".equals(flag)){
				return ResultUtil.error(Status.GeneralError.getStatenum(), "请输入正确的支付密码");
			}else {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "请设置支付密码");
			}
		} catch (Exception e) {
			LoggingUtil.e("验证支付密码:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "验证支付密码异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "设置推荐人", value = "设置推荐人")
	@RequestMapping(value = "/updateInviteCode/{token}", method = RequestMethod.POST)
	Result<Object> updateInviteCode(@PathVariable("token") String toKen, @RequestParam Integer inviteCode)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			// 判断一下inviteCode是否存在系统
			if (loginAndRegisterService.InviteCodeIsExists(inviteCode)) {
				User userForUpdate = new User();
				userForUpdate.setBeinvitecode(inviteCode);
				userForUpdate.setUsername(phoneNum);
				loginAndRegisterService.updateUser(userForUpdate);
				loginAndRegisterService.sendCouponByInviteCode(new BigDecimal("100.00"), inviteCode,"新人推荐红包");
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
	@RequestMapping(value = "/createBOrder/{token}", method = RequestMethod.POST)
	Result<Object> createBOrder(@PathVariable("token") String toKen, @RequestBody OrderHeadEXDto orderHeadEXDto)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/syncHandleOrderC/{token}", method = RequestMethod.POST)
	Result<Object> syncHandleOrderC(@PathVariable("token") String toKen, @RequestParam String orderNoC)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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

	@ApiOperation(httpMethod = "POST", notes = "子订单处理", value = "子订单处理")
	@RequestMapping(value = "/HandleEOrder/{token}", method = RequestMethod.POST)
	Result<Object> handleEOrder(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 通过订单查询
			if (UserId.equals(orderHandleService.getUserIdByOrder(orderNo))) {
				// 继续处理订单
				Map<String, BigDecimal> moneyUseMap = orderHandleService.getMoneyUserOfOrderhead(orderNo);
				BigDecimal platformMoney = moneyUseMap.get("PlatformMoney");
				BigDecimal realPay = moneyUseMap.get("RealPay");
				HashMap<String, Object> exceptResult = new HashMap<>();
				if (realPay.compareTo(BigDecimal.ZERO) == 0) {
					// 不需要通过支付宝付款
					exceptResult.put("type", "1");
					exceptResult.put("orderNo", orderNo); 
				}else {
					if (platformMoney.compareTo(BigDecimal.ZERO) == 0) {
						// 不需要平台币值
						exceptResult.put("type", "2");
						exceptResult.put("singData",
								orderHandleService.createAliSignature(orderNo,realPay.toString()));
					} else {
						// 混合支付
						exceptResult.put("type", "3");
						exceptResult.put("singData",
								orderHandleService.createAliSignature(orderNo, realPay.toString()));
					}
				}
				return ResultUtil.success(exceptResult);
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "您确定是该用户生成的订单吗");
			}

		} catch (Exception e) {
			LoggingUtil.e("子订单处理异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "子订单处理异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "同步处理支付子订单", value = "同步处理支付子订单")
	@RequestMapping(value = "/syncHandleOrder/{token}", method = RequestMethod.POST)
	Result<Object> syncHandleOrder(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 通过订单查询
			if (UserId.equals(orderHandleService.getUserIdByOrder(orderNo))) {
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
	@RequestMapping(value = "/checkNewExclusive/{token}", method = RequestMethod.GET)
	Result<Object> checkNewExclusive(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/drawNewExclusive/{token}", method = RequestMethod.POST)
	Result<Object> drawNewExclusive(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/accountBill/{token}", method = RequestMethod.GET)
	Result<Object> accountBill(@PathVariable("token") String toKen, @RequestParam String type,
			@RequestParam Integer page, @RequestParam Integer pageNum) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
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
	@RequestMapping(value = "/cancelOrder/{token}", method = RequestMethod.POST)
	Result<Object> accountBill(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (UserId.equals(orderHandleService.getUserIdByOrder(orderNo))) {
				orderHandleService.cancelOrder(orderNo);
			}
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("取消订单异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "取消订单异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "确认收货", value = "确认收货")
	@RequestMapping(value = "/confirmOrder/{token}", method = RequestMethod.POST)
	Result<Object> confirmOrder(@PathVariable("token") String toKen, @RequestParam String orderNo)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (UserId.equals(orderHandleService.getUserIdByOrder(orderNo))) {
				orderHandleService.confirmOrder(orderNo);
			}
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("确认收货异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "确认收货异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "现金提现", value = "现金提现")
	@RequestMapping(value = "/TxElecNum/{token}", method = RequestMethod.POST)
	Result<Object> txElecNum(@PathVariable("token") String toKen, @RequestParam BigDecimal money,
			@RequestParam(required = false, defaultValue = "提现") String memo, @RequestParam String txType,
			@RequestParam String cardNo, @RequestParam String trueName,
			@RequestParam(required = false, defaultValue = "支付宝") String bankName) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (!personalCenterService.isAlreadyAuth(UserId)) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "未通过实名认证");
			}
			if (personalCenterService.txElecNum(UserId, money, memo, txType, cardNo, trueName, bankName)) {
				return ResultUtil.success();
			}else{
				return ResultUtil.error(Status.BussinessError.getStatenum(), "提现金额不够");
			}
		} catch (Exception e) {
			LoggingUtil.e("现金提现异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "现金提现异常");
		}

	}

	@ApiOperation(httpMethod = "GET", notes = "实名认证界面", value = "实名认证界面")
	@RequestMapping(value = "/GetCertificationInfo/{token}", method = RequestMethod.GET)
	Result<Object> getCertificationInfo(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			return ResultUtil.success(personalCenterService.getCertificationInfo(UserId));
		} catch (Exception e) {
			LoggingUtil.e("实名认证界面异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "实名认证界面异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "提交实名认证申请", value = "提交实名认证申请")
	@RequestMapping(value = "/PostCertificationInfo/{token}", method = RequestMethod.POST)
	Result<Object> postCertificationInfo(@PathVariable("token") String toKen, @RequestBody User user)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (personalCenterService.isAlreadyAuth(UserId)) {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "已经审核通过的用户");
			}
			User userForUpdate = new User();
			userForUpdate.setTruename(user.getTruename());
			userForUpdate.setPhone(user.getPhone());
			userForUpdate.setIdcardno(user.getIdcardno());
			userForUpdate.setIdcardphoto(user.getIdcardphoto());
			userForUpdate.setIdcardphoto2(user.getIdcardphoto2());
			userForUpdate.setIdcardphoto3(user.getIdcardphoto3());
			userForUpdate.setUsername(phoneNum);
			loginAndRegisterService.updateUser(userForUpdate);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("提交实名认证申请异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "提交实名认证申请异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "申请退款", value = "申请退款")
	@RequestMapping(value = "/ApplyRefund/{token}", method = RequestMethod.POST)
	Result<Object> applyRefund(@PathVariable("token") String toKen, @RequestParam String orderNo,
			@RequestParam String memo, @RequestParam() String photo1) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			Integer userId = orderHandleService.getUserIdByOrder(orderNo);
			if (UserId.equals(userId)) {
				if (orderApplyService.applyCancelOrder(orderNo, memo)) {
					return ResultUtil.success();
				} else {
					return ResultUtil.error(Status.BussinessError.getStatenum(), "您已经取消过了");
				}
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "您确定是该用户生成的订单吗");
			}
		} catch (Exception e) {
			LoggingUtil.e("申请退款异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "申请退款异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "申请退货", value = "申请退货")
	@RequestMapping(value = "/ApplySaleReturn/{token}", method = RequestMethod.POST)
	Result<Object> applySaleReturn(@PathVariable("token") String toKen, @RequestParam String orderNo,
			@RequestParam String memo, @RequestParam(required = false, defaultValue = "") String photo1,
			@RequestParam(required = false, defaultValue = "") String photo2,
			@RequestParam(required = false, defaultValue = "") String photo3) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (UserId.equals(orderHandleService.getUserIdByOrder(orderNo))) {
				if (orderApplyService.applySaleReturn(orderNo, memo, photo1, photo2, photo3)) {
					return ResultUtil.success();
				} else {
					return ResultUtil.error(Status.BussinessError.getStatenum(), "您已经取消过了");
				}
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "您确定是该用户生成的订单吗");
			}
		} catch (Exception e) {
			LoggingUtil.e("申请退货异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "申请退货异常");
		}
	}
	
	@ApiOperation(httpMethod = "POST", notes = "投诉建议", value = "投诉建议")
	@RequestMapping(value = "/ComplaintAndAdvice/{token}", method = RequestMethod.POST)
	Result<Object> complaintAndAdvice(@PathVariable("token") String toKen, @RequestParam String memo) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			personalCenterService.complaintAndAdvice( UserId, memo);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("投诉建议异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "投诉建议异常");
		}
	}
}
package com.zhongjian.webserver.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.zhongjian.webserver.dto.PANRequestMap;
import com.zhongjian.webserver.dto.PANResponseMap;
import com.zhongjian.webserver.pojo.User;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.OrderHandleService;
import com.zhongjian.webserver.service.PersonalCenterService;
import com.zhongjian.webserver.service.ProductManagerService;

import io.netty.handler.codec.http.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/PersonalCenter/", description = "涓汉涓績")
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

	@ApiOperation(httpMethod = "GET", notes = "鏍规嵁token鑾峰彇涓汉涓績鏁版嵁", value = "鍒濆鍖栦釜浜轰腑蹇冩暟鎹�")
	@RequestMapping(value = "/PersonalCenter/initPersonalCenterData/{token}", method = RequestMethod.GET)
	Result<Object> initPersonalCenterData(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
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
			LoggingUtil.e("涓汉涓績鏁版嵁鍒濆鍖栧紓甯�:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "涓汉涓績鏁版嵁鍒濆鍖栧紓甯�");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "鎴戠殑閽卞寘", value = "鎴戠殑閽卞寘鏁版嵁")
	@RequestMapping(value = "/PersonalCenter/initPersonalWallet/{token}", method = RequestMethod.GET)
	Result<Object> initPersonalWallet(@PathVariable("token") String toKen) throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			Map<String, Object> map = personalCenterService.getInformOfConsumption(phoneNum);
			return ResultUtil.success(map);
		} catch (Exception e) {
			LoggingUtil.e("涓汉涓績鏁版嵁鍒濆鍖栧紓甯�:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "涓汉涓績鏁版嵁鍒濆鍖栧紓甯�");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "鏍规嵁token鑾峰彇璇ョ敤鎴锋墜鏈哄彿", value = "鏍规嵁token鑾峰彇璇ョ敤鎴锋墜鏈哄彿")
	@RequestMapping(value = "/PersonalCenter/getUserPhoneNum/{token}", method = RequestMethod.POST)
	Result<Object> getUserPhoneNum(@PathVariable("token") String token) throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			return ResultUtil.success(phoneNum);
		} catch (Exception e) {
			LoggingUtil.e("鏍规嵁token鑾峰彇璇ョ敤鎴锋墜鏈哄彿寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鏍规嵁token鑾峰彇璇ョ敤鎴锋墜鏈哄彿寮傚父");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "鏍规嵁token鑾峰彇寰呬粯娆捐鍗�", value = "寰呬粯娆�")
	@RequestMapping(value = "/PersonalCenter/itemsToBePaidFor", method = RequestMethod.GET)
	Result<Object> itemsToBePaidFor(@PathVariable("token") String token) throws BusinessException {
		return null;

	}

	@ApiOperation(httpMethod = "GET", notes = "鑾峰彇璁㈠崟璇︽儏", value = "鑾峰彇璁㈠崟璇︽儏")
	@RequestMapping(value = "/PersonalCenter/getOrderDetails/{token}/{orderId}", method = RequestMethod.GET)
	Result<Object> getOrderDetails(@PathVariable("token") String token, @PathVariable("orderId") Integer orderId)
			throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			return ResultUtil.success(personalCenterService.getOrderDetailsById(orderId));
		} catch (Exception e) {
			LoggingUtil.e("鑾峰彇璁㈠崟璇︽儏寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鑾峰彇璁㈠崟璇︽儏寮傚父");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "鏍规嵁token鑾峰彇璐墿杞︿俊鎭�", value = "鎴戠殑璐墿杞�")
	@RequestMapping(value = "/PersonalCenter/getShoppingCartInfo", method = RequestMethod.GET)
	Result<Object> getShoppingCartInfo(@RequestParam String token) throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			// 鑾峰彇鐢ㄦ埛Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 鏍规嵁鐢ㄦ埛Id鑾峰彇璐墿杞︿俊鎭�
			List<HashMap<String, Object>> shoppingCartList = personalCenterService.getShoppingCartInfo(userId);
			return ResultUtil.success(shoppingCartList);
		} catch (Exception e) {
			LoggingUtil.e("鑾峰彇璐墿杞︿俊鎭紓甯�:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鑾峰彇璐墿杞︿俊鎭紓甯�");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "璐墿杞︽柊澧�", value = "鏂板璐墿杞﹀晢鍝佽鏍�")
	@RequestMapping(value = "/PersonalCenter/updateShoppingCartInfo/{productId}/{SpecId}", method = RequestMethod.POST)
	Result<Object> addForShoppingCart(@PathVariable("productId") Integer productId,
			@PathVariable("SpecId") Integer specId, @RequestParam String token, @RequestParam Integer productNum)
					throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			List<PANRequestMap> pANRequestMaps = new ArrayList<>();
			PANRequestMap pANRequestMap = new PANRequestMap();
			pANRequestMap.setProductId(productId);
			pANRequestMap.setProductNum(productNum);
			List<PANResponseMap> PANResponseMaps = productManagerService.checkProductStoreNum(pANRequestMaps);
			if (PANResponseMaps.size() > 0) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "鍟嗗搧搴撳瓨涓嶅", PANResponseMaps);
			}
			// 鑾峰彇鐢ㄦ埛Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (personalCenterService.addShoppingCartInfo(userId, productId, specId, productNum, new Date()) == 1) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "鏁版嵁搴撴坊鍔犺喘鐗╄溅璁板綍澶辫触");
			}
		} catch (Exception e) {
			LoggingUtil.e("娣诲姞璐墿杞﹁褰曞紓甯�:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "娣诲姞璐墿杞﹁褰曞紓甯�");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "璐墿杞︽洿鏂�", value = "璐墿杞﹀叿浣撶墿鍝佹暟閲忔洿鏂�")
	@RequestMapping(value = "/PersonalCenter/updateShoppingCartInfo/{shoppingCartId}", method = RequestMethod.POST)
	Result<Object> updateForShoppingCart(@PathVariable("shoppingCartId") Integer shoppingCartId,
			@RequestParam String token, @RequestParam Integer productNum) throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			// 鏍规嵁shoppingCartId鏌ュ嚭productId
			Integer productId = personalCenterService.getProductIdByShoppingId(shoppingCartId);
			List<PANRequestMap> pANRequestMaps = new ArrayList<>();
			PANRequestMap pANRequestMap = new PANRequestMap();
			pANRequestMap.setProductId(productId);
			pANRequestMap.setProductNum(productNum);
			List<PANResponseMap> PANResponseMaps = productManagerService.checkProductStoreNum(pANRequestMaps);
			if (PANResponseMaps.size() > 0) {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "鍟嗗搧搴撳瓨涓嶅", PANResponseMaps);
			}
			// 鑾峰彇鐢ㄦ埛Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			if (personalCenterService.setShoppingCartInfo(userId, shoppingCartId, productNum) == 1) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "鏁版嵁搴撴坊鍔犺喘鐗╄溅璁板綍澶辫触");
			}
		} catch (Exception e) {
			LoggingUtil.e("娣诲姞璐墿杞﹁褰曞紓甯�:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "娣诲姞璐墿杞﹁褰曞紓甯�");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "鏍规嵁token鍜宻hoppingCartId鏉ュ垹闄�", value = "鍒犻櫎璐墿杞︽暟鎹�")
	@RequestMapping(value = "/PersonalCenter/delShoppingCartInfo", method = RequestMethod.POST)
	Result<Object> delShoppingCartById(@RequestParam String token, Integer shoppingCartId) throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			// 鑾峰彇鐢ㄦ埛Id
			Integer userId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 鏍规嵁鐢ㄦ埛Id鍜宻hoppingCartId娓呯悊璐墿杞︽暟鎹�
			if (personalCenterService.delShoppingCartInfoById(userId, shoppingCartId)) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "shoppingCartId鏈夎");
			}

		} catch (Exception e) {
			LoggingUtil.e("鍒犻櫎璐墿杞︾墿浠跺紓甯�:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鍒犻櫎璐墿杞︾墿浠跺紓甯�");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "鏇存柊澶村儚", value = "鏇存柊澶村儚")
	@RequestMapping(value = "/PersonalCenter/updateHeadPhoto/{token}", method = RequestMethod.POST)
	Result<Object> updateHeadPhoto(@PathVariable("token") String toKen, @RequestParam String headPhoto)
			throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			User userForUpdate = new User();
			userForUpdate.setHeadphoto(headPhoto);
			userForUpdate.setUsername(phoneNum);
			loginAndRegisterService.updateUser(userForUpdate);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("鏇存柊澶村儚寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鏇存柊澶村儚寮傚父");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "鏇存柊鏄电О", value = "鏇存柊鏄电О")
	@RequestMapping(value = "/PersonalCenter/updateNickName/{token}", method = RequestMethod.POST)
	Result<Object> updateNickName(@PathVariable("token") String toKen, @RequestParam String nickName)
			throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			User userForUpdate = new User();
			userForUpdate.setNickname(nickName);
			userForUpdate.setUsername(phoneNum);
			loginAndRegisterService.updateUser(userForUpdate);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("鏇存柊鏄电О寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鏇存柊鏄电О寮傚父");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "楠岃瘉鏀粯瀵嗙爜", value = "楠岃瘉鏀粯瀵嗙爜")
	@RequestMapping(value = "/PersonalCenter/verifyPayPassword", method = RequestMethod.POST)
	Result<Object> verifyPayPassword(@RequestParam String toKen, String payPassword) throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			if (loginAndRegisterService.userFundsIsFreeze(phoneNum)) {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "骞冲彴甯佸�煎凡鍐荤粨");
			}
			if (loginAndRegisterService.checkUserNameAndPayPassword(phoneNum, payPassword)) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "璇疯緭鍏ユ纭殑鏀粯瀵嗙爜");
			}
		} catch (Exception e) {
			LoggingUtil.e("楠岃瘉鏀粯瀵嗙爜:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "楠岃瘉鏀粯瀵嗙爜寮傚父");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "璁剧疆閭�璇风爜", value = "璁剧疆閭�璇风爜")
	@RequestMapping(value = "/PersonalCenter/updateInviteCode/{token}", method = RequestMethod.POST)
	Result<Object> updateInviteCode(@PathVariable("token") String toKen, @RequestParam Integer inviteCode)
			throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			// 鍒ゆ柇涓�涓媔nviteCode鏄惁瀛樺湪绯荤粺
			if (loginAndRegisterService.InviteCodeIsExists(inviteCode)) {
				User userForUpdate = new User();
				userForUpdate.setBeinvitecode(inviteCode);
				userForUpdate.setUsername(phoneNum);
				loginAndRegisterService.updateUser(userForUpdate);
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "閭�璇风爜涓嶅瓨鍦�");
			}

		} catch (Exception e) {
			LoggingUtil.e("鏇存柊鏄电О寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鏇存柊鏄电О寮傚父");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "鐢熸垚璐墿璁㈠崟", value = "鐢熸垚璐墿璁㈠崟")
	@RequestMapping(value = "/PersonalCenter/createBOrder/{token}", method = RequestMethod.POST)
	Result<Object> createBOrder(@PathVariable("token") String toKen, @RequestBody List<OrderHeadDto> orderHeads)
			throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// result涓湁鎬婚噾棰濆拰鍗曞彿锛屽啀鍘绘嫾鎺ョ鍚嶈繑鍥炵粰瀹㈡埛绔�
			HashMap<String, Object> result = orderHandleService.createOrder(orderHeads, UserId);
			HashMap<String, Object> exceptResult = new HashMap<>();
			BigDecimal totalRealPayCo = (BigDecimal) result.get("totalRealPayCo");
			BigDecimal totalNotRealPayCo = (BigDecimal) result.get("totalNotRealPayCo");
			String orderNoCollectionName = (String) result.get("orderNoCollectionName");
			if (totalRealPayCo.compareTo(BigDecimal.ZERO) == 0) {
				// 涓嶉渶瑕侀�氳繃鏀粯瀹濅粯娆�
				exceptResult.put("type", "1");
				exceptResult.put("orderNoC", result.get("orderNoCollectionName"));
			} else {
				if (totalNotRealPayCo.compareTo(BigDecimal.ZERO) == 0) {
					// 涓嶉渶瑕佸钩鍙板竵鍊�
					exceptResult.put("type", "2");
					exceptResult.put("singData",
							orderHandleService.createAliSignature(orderNoCollectionName, totalRealPayCo.toString()));
				} else {
					// 娣峰悎鏀粯
					exceptResult.put("type", "3");
					exceptResult.put("singData",
							orderHandleService.createAliSignature(orderNoCollectionName, totalRealPayCo.toString()));
				}
			}
			return ResultUtil.success(exceptResult);
		} catch (RuntimeException e) {
			throw new BusinessException(Status.GeneralError.getStatenum(), e.getMessage());
		} catch (Exception e) {
			LoggingUtil.e("鐢熸垚璁㈠崟寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鐢熸垚璁㈠崟寮傚父");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "鐢熸垚VIP璁㈠崟", value = "鐢熸垚VIP璁㈠崟")
	@RequestMapping(value = "/PersonalCenter/createVOrder/{token}", method = RequestMethod.POST)
	Result<Object> createVOrder(@PathVariable("token") String toKen, @RequestBody List<OrderHeadDto> orderHeads)
			throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// result涓湁鎬婚噾棰濆拰鍗曞彿锛屽啀鍘绘嫾鎺ョ鍚嶈繑鍥炵粰瀹㈡埛绔�
			HashMap<String, Object> result = orderHandleService.createOrder(orderHeads, UserId);
			return ResultUtil
					.success(orderHandleService.createAliSignature((String) result.get("orderNoCollectionName"),
							((BigDecimal) result.get("totalAmountCo")).toString()));
		} catch (RuntimeException e) {
			throw new BusinessException(Status.GeneralError.getStatenum(), e.getMessage());
		} catch (Exception e) {
			LoggingUtil.e("鐢熸垚璁㈠崟寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鐢熸垚璁㈠崟寮傚父");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "鍚屾澶勭悊鏀粯璁㈠崟", value = "鍚屾澶勭悊鏀粯璁㈠崟")
	@RequestMapping(value = "/PersonalCenter/syncHandleOrderC/{token}", method = RequestMethod.POST)
	Result<Object> syncHandleOrderC(@PathVariable("token") String toKen, @RequestParam String orderNoC,
			@RequestParam Integer platformMoneyAmount) throws BusinessException {
		try {
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			// 閫氳繃璁㈠崟鏌ヨ
			if (UserId == orderHandleService.getUserIdByOrderC(orderNoC)) {
				// 缁х画澶勭悊璁㈠崟
				// 鐩存帴璁㈠崟鍚屾澶勭悊
				if (orderHandleService.syncHandleOrder(orderNoC, platformMoneyAmount)) {
					return ResultUtil.success();
				} else {
					return ResultUtil.error(Status.GeneralError.getStatenum(), "閲戦鏈夎鎴栨槸璁㈠崟宸叉敮浠樿繃浜�");
				}
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "鎮ㄧ‘瀹氭槸璇ョ敤鎴风敓鎴愮殑璁㈠崟鍚�");
			}

		} catch (Exception e) {
			LoggingUtil.e("鏀粯璁㈠崟寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鏀粯璁㈠崟寮傚父");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "鏌ョ湅璇ョ敤鎴锋槸鍚﹂鍙栨柊浜虹孩鍖�", value = "鏌ョ湅璇ョ敤鎴锋槸鍚﹂鍙栨柊浜虹孩鍖�")
	@RequestMapping(value = "/PersonalCenter/checkNewExclusive/{token}", method = RequestMethod.GET)
	Result<Object> checkNewExclusive(@PathVariable("token") String toKen,HttpServletResponse response) throws BusinessException {
		try {
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "*");
			response.addHeader("Access-Control-Max-Age", "100");
			response.addHeader("Access-Control-Allow-Headers", "Content-Type");
			response.addHeader("Access-Control-Allow-Credentials", "false");
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			if (loginAndRegisterService.userNewExclusiveIsDraw(phoneNum)) {
				// 棰嗚繃浜�
				return ResultUtil.success(1);
			} else {
				// 鍙互棰嗗彇
				return ResultUtil.success(0);
			}
		} catch (Exception e) {
			LoggingUtil.e("鏌ョ湅璇ョ敤鎴锋槸鍚﹂鍙栨柊浜虹孩鍖呭紓甯�:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "鏌ョ湅璇ョ敤鎴锋槸鍚﹂鍙栨柊浜虹孩鍖呭紓甯�");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "棰嗗彇鏂颁汉绾㈠寘", value = "棰嗗彇鏂颁汉绾㈠寘")
	@RequestMapping(value = "/PersonalCenter/drawNewExclusive/{token}", method = RequestMethod.POST)
	Result<Object> drawNewExclusive(@PathVariable("token") String toKen,HttpServletResponse response) throws BusinessException {
		try {
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "*");
			response.addHeader("Access-Control-Max-Age", "100");
			response.addHeader("Access-Control-Allow-Headers", "Content-Type");
			response.addHeader("Access-Control-Allow-Credentials", "false");
			// 妫�鏌oken閫氳繃
			String phoneNum = tokenManager.checkTokenGetUser(toKen);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token宸茶繃鏈�");
			}
			if (loginAndRegisterService.drawNewExclusive(phoneNum) == 1) {
				return ResultUtil.success();
			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "宸茬粡棰嗗彇杩�");
			}
		} catch (Exception e) {
			LoggingUtil.e("棰嗗彇鏂颁汉绾㈠寘寮傚父:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "棰嗗彇鏂颁汉绾㈠寘寮傚父");
		}
	}
}
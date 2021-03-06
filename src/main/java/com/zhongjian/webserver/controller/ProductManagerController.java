package com.zhongjian.webserver.controller;

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
import com.zhongjian.webserver.dto.PANRequestMap;
import com.zhongjian.webserver.pojo.ProductCommentShow;
import com.zhongjian.webserver.service.LoginAndRegisterService;
import com.zhongjian.webserver.service.ProductManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/v1/ProductManager/", description = "商品相关")
@RequestMapping(value = "/v1/ProductManager")
public class ProductManagerController {
	@Autowired
	private TokenManager tokenManager;
	
    @Autowired
    private LoginAndRegisterService loginAndRegisterService;
	
	@Autowired
	private ProductManagerService productManagerService;

	@ApiOperation(httpMethod = "GET", notes = "获取所有商品分类", value = "获取所有商品分类")
	@RequestMapping(value = "/getProductOfCategory", method = RequestMethod.GET)
	Result<Object> getProductOfCategory() throws BusinessException {
		try {
			return ResultUtil.success(productManagerService.getProductOfCategory());
		} catch (Exception e) {
			LoggingUtil.e("获取所有商品并分类处异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取所有商品并分类处异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "获取二级分类商品", value = "获取二级分类商品")
	@RequestMapping(value = "/getProductOfCategory/{SubCategoryId}", method = RequestMethod.GET)
	Result<Object> getProductsOfSubCategory(@PathVariable("SubCategoryId") Integer subCategoryId,
			@RequestParam Integer type, @RequestParam Integer page, @RequestParam Integer pageNum)
			throws BusinessException {
		try {
			String condition = "";
			if (type == 1) {
				condition = "SaleNum DESC";
			} else if (type == 2) {
				condition = "Price ASC";
			} else if (type == 3) {
				condition = "Price DESC";
			} else if (type == 4) {
				condition = "CommentNum DESC";
			} else if (type == 5) {
				condition = "ElecNum ASC";
			} else if (type == 6) {
				condition = "ElecNum DESC";
			}
			return ResultUtil
					.success(productManagerService.getSubProductOfCategory(subCategoryId, condition, page, pageNum));
		} catch (Exception e) {
			LoggingUtil.e("获取二级分类商品异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取二级分类商品异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "获取商品详情", value = "获取商品详情")
	@RequestMapping(value = "/getProductDetails", method = RequestMethod.GET)
	Result<Object> getProductDetails(@RequestParam Integer productId) throws BusinessException {
		try {
			// 获取商品详情
			return ResultUtil.success(productManagerService.getProductDetails(productId));
		} catch (Exception e) {
			LoggingUtil.e("获取商品详情异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取商品详情异常");
		}

	}

	@ApiOperation(httpMethod = "GET", notes = "获取商品评价详情", value = "获取商品评价详情")
	@RequestMapping(value = "/getProductCommentById", method = RequestMethod.GET)
	Result<Object> getProductCommentById(@RequestParam Integer productId, @RequestParam Integer page,
			@RequestParam Integer pageNum) throws BusinessException {
		try {
			List<ProductCommentShow> productComments = productManagerService.getProductComment(productId, page, pageNum);
			return ResultUtil.success(productComments);
		} catch (Exception e) {
			LoggingUtil.e("获取商品评价详情异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取商品详情异常");
		}
	}

	// web页面get请求获取productHtmlText
	@ApiOperation(httpMethod = "GET", notes = "获取商品图文详情", value = "获取商品图文详情")
	@RequestMapping(value = "/getProductImgAndText", method = RequestMethod.GET)
	Result<Object> getProductImgAndText(@RequestParam Integer productId) throws BusinessException {
		try {

			String productComments = productManagerService.getProductHtmlText(productId);
			return ResultUtil.success(productComments);
		} catch (Exception e) {
			LoggingUtil.e("获取商品图文详情异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取商品图文详情异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "检查商品库存", value = "检查商品库存")
	@RequestMapping(value = "/checkProductStoreNum", method = RequestMethod.POST)
	Result<Object> checkProductStoreNum(@RequestBody List<PANRequestMap> pANRequestMaps) throws BusinessException {
		try {
			return ResultUtil.success(productManagerService.checkProductStoreNum(pANRequestMaps));
		} catch (Exception e) {
			LoggingUtil.e("获取商品详情异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取商品详情异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "商品搜索", value = "商品搜索")
	@RequestMapping(value = "/searchProduct", method = RequestMethod.POST)
	Result<Object> searchProduct(@RequestParam String key) throws BusinessException {
		try {
			List<Map<String, Object>> result = productManagerService.searchProduct(key);
			if (result == null) {
				return ResultUtil.error(Status.BussinessError.getStatenum(), "无搜索结果");
			} else {
				return ResultUtil.success(result);
			}
		} catch (Exception e) {
			LoggingUtil.e("商品搜索异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "商品搜索异常");
		}
	}

	@ApiOperation(httpMethod = "POST", notes = "商品评价", value = "商品评价")
	@RequestMapping(value = "/ProductCommet/{token}", method = RequestMethod.POST)
	Result<Object> productCommet(@PathVariable("token") String token, @RequestBody List<Map<String, Object>> productCommentMaps)
			throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			Integer UserId = loginAndRegisterService.getUserIdByUserName(phoneNum);
			productManagerService.addProductComment(UserId, productCommentMaps);
			return ResultUtil.success();
		} catch (Exception e) {
			LoggingUtil.e("商品评价异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "商品评价异常");
		}
	}

}

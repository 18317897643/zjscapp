package com.zhongjian.webserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.zhongjian.webserver.pojo.ProductComment;
import com.zhongjian.webserver.service.ProductManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/ProductManager/", description = "商品相关")
public class ProductManagerController {
	@Autowired
	TokenManager tokenManager;
	
	@Autowired
	private ProductManagerService productManagerService;
	
	
	@ApiOperation(httpMethod = "POST", notes = "获取所有商品分类", value = "获取所有商品分类")
	@RequestMapping(value = "/ProductManager/getProductOfCategory", method = RequestMethod.POST)
	Result<Object> getProductOfCategory(@RequestParam String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			return ResultUtil.success(productManagerService.getProductOfCategory());
		} catch (Exception e) {
			LoggingUtil.e("获取所有商品并分类处异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取所有商品并分类处异常");
		}
	}
	
	@ApiOperation(httpMethod = "POST", notes = "获取商品详情", value = "获取商品详情")
	@RequestMapping(value = "/ProductManager/getProductDetails", method = RequestMethod.POST)
	Result<Object> getProductDetails(@RequestParam Integer productId,String token) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			//获取商品详情
			return ResultUtil.success(productManagerService.getProductDetails(productId));
		} catch (Exception e) {
			LoggingUtil.e("获取商品详情异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取商品详情异常");
		}
	
	}
	
	@ApiOperation(httpMethod = "POST", notes = "获取商品评价详情", value = "获取商品评价详情")
	@RequestMapping(value = "/ProductManager/getProductCommentById", method = RequestMethod.POST)
	Result<Object> getProductCommentById(@RequestParam Integer productId,String token,Integer page,Integer pageNum) throws BusinessException {
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "token已过期");
			}
			List<ProductComment> productComments = productManagerService.getProductComment(productId, page, pageNum);
			return ResultUtil.success(productComments);
		} catch (Exception e) {
			LoggingUtil.e("获取商品评价详情异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取商品详情异常");
		}
	}
	//web页面get请求获取productHtmlText
	@ApiOperation(httpMethod = "GET", notes = "获取商品图文详情", value = "获取商品图文详情")
	@RequestMapping(value = "/ProductManager/getProductImgAndText", method = RequestMethod.GET)
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
	@RequestMapping(value = "/ProductManager/checkProductStoreNum", method = RequestMethod.POST)
	Result<Object> checkProductStoreNum(@RequestBody List<PANRequestMap> pANRequestMaps) throws BusinessException {
		try {
			return ResultUtil.success(productManagerService.checkProductStoreNum(pANRequestMaps));
		} catch (Exception e) {
			LoggingUtil.e("获取商品详情异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "获取商品详情异常");
		}
 	}
}

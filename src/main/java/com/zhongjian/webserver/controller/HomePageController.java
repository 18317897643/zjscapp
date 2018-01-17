package com.zhongjian.webserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.zhongjian.webserver.component.MallData;
import com.zhongjian.webserver.service.HomePageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "/HomePage/", description = "首页相关")
@RequestMapping(value = "/HomePage")
public class HomePageController {

	@Autowired
	TokenManager tokenManager;

	@Autowired
	HomePageService homePageService;
	
	@Autowired
	MallData mallData;

	// "picList": [],"tile": null 有可能出现
	@ApiOperation(httpMethod = "GET", notes = "首页初始化数据", value = "首页初始化数据")
	@RequestMapping(value = "/initHomePage", method = RequestMethod.GET)
	Result<Object> initHomePage(@RequestParam Integer productNum) throws BusinessException {
		try {
			return ResultUtil.success(homePageService.initHomePage(productNum));
		} catch (Exception e) {
			LoggingUtil.e("首页初始化数据异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "首页初始化数据异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "会员专区", value = "会员专区")
	@RequestMapping(value = "/MemberArea", method = RequestMethod.GET)
	Result<Object> getMemberArea(@RequestParam Integer type, @RequestParam Integer page, @RequestParam Integer pageNum)
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
			return ResultUtil.success(homePageService.getAreaProducts(mallData.getProductTag(),condition, page, pageNum));
		} catch (Exception e) {
			LoggingUtil.e("会员专区异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "会员专区异常");
		}
	}

	@ApiOperation(httpMethod = "GET", notes = "具体专区数据", value = "具体专区数据")
	@RequestMapping(value = "/AreaProducts", method = RequestMethod.GET)
	Result<Object> AreaProducts(@RequestParam Integer tag, @RequestParam Integer type, @RequestParam Integer page,
			@RequestParam Integer pageNum) throws BusinessException {
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
			return ResultUtil.success(homePageService.getAreaProducts(tag,condition, page, pageNum));
		} catch (Exception e) {
			LoggingUtil.e("具体专区数据异常:" + e);
			throw new BusinessException(Status.SeriousError.getStatenum(), "具体专区数据异常");
		}
	}

}

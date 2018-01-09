package com.zhongjian.webserver.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.beanconfiguration.MallData;
import com.zhongjian.webserver.dto.PANRequestMap;
import com.zhongjian.webserver.dto.PANResponseMap;
import com.zhongjian.webserver.mapper.ProductMapper;
import com.zhongjian.webserver.pojo.Product;
import com.zhongjian.webserver.pojo.ProductCategory;
import com.zhongjian.webserver.pojo.ProductComment;
import com.zhongjian.webserver.service.ProductManagerService;

@Service
public class ProductManagerServiceImpl implements ProductManagerService {

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	MallData mallData;

	@Override
	public List<ProductCategory> getProductOfCategory() {
		return productMapper.getCategory();
	}

	@Override
	public HashMap<String, Object> getProductDetails(Integer productId) {
		Product product = productMapper.findById(productId);
		List<ProductComment> productComments = productMapper.selectProductcommentById(productId, 0, 1);
		ProductComment productComment = null;
		if (productComments.size() > 0) {
			productComment = productComments.get(0);
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		if (product.getTag().equals(mallData.getProductTag())) {
			map.put("beLongToVIP", true);
		} else {
			map.put("beLongToVIP", false);
		}
		map.put("product", product);
		map.put("productComment", productComment);

		return map;
	}

	@Override
	public List<ProductComment> getProductComment(Integer productId, Integer page, Integer pageNum) {
		return productMapper.selectProductcommentById(productId, page, pageNum);
	}

	@Override
	public String getProductHtmlText(Integer id) {
		return productMapper.findProductHtmlTextById(id);
	}

	@Override
	public List<PANResponseMap> checkProductStoreNum(List<PANRequestMap> pANRequestMaps) {
		List<PANResponseMap> pANResponseMaps = new ArrayList<PANResponseMap>();
		for (PANRequestMap pANRequestMap : pANRequestMaps) {
			Product product = productMapper.getProductNumById(pANRequestMap.getProductId());
			Integer currentProductNum = product.getStocknum();
			if (currentProductNum < pANRequestMap.getProductNum()) {
				PANResponseMap pANResponseMap = new PANResponseMap();
				pANResponseMap.setProductName(product.getProductname());
				pANResponseMap.setProductId(product.getId());
				pANResponseMap.setProductNum(currentProductNum);
				pANResponseMaps.add(pANResponseMap);
			}
		}
		return pANResponseMaps;
	}

	@Override
	public List<Product> getSubProductOfCategory(Integer subCategoryId) {
		return productMapper.getProductsOfSubCategory(subCategoryId);
	}

}

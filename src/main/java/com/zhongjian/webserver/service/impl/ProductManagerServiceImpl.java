package com.zhongjian.webserver.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.component.MallData;
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
		page = page * pageNum;
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
	public List<HashMap<String, Object>> getSubProductOfCategory(Integer subCategoryId,String condition,Integer page,Integer pageNum) {
		page = page * pageNum; 
		List<Product> subProducts = productMapper.getProductsOfSubCategory(subCategoryId,condition,page,pageNum);
		List<HashMap<String, Object>> datas = new ArrayList<>();
		for (int i = 0; i < subProducts.size(); i++) {
			HashMap<String, Object> data = new HashMap<>();
			data.put("product", subProducts.get(i));
			if (subProducts.get(i).getTag() == mallData.getProductTag()) {
				data.put("beLongToVIP", true);
			}else{
				data.put("beLongToVIP", false);
			}
           	datas.add(data);		
		}
		return datas;
	}

	@Override
	public List<Map<String, Object>> searchProduct(String key) {
		List<Map<String, Object>> productMapList = productMapper.searchProduct("%" + key + "%");
		Integer productMapListSize = productMapList.size();
		for (int i = 0; i < productMapListSize; i++) {
			Integer theTag = (Integer) productMapList.get(i).get("Tag");
			if (theTag != null && theTag == mallData.getProductTag()) {
				productMapList.get(i).put("beLongToVIP", true);
			}else {
				productMapList.get(i).put("beLongToVIP", false);
			}
		}
		return productMapList;
	}
}

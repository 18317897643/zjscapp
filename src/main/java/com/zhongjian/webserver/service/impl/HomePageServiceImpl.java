package com.zhongjian.webserver.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.component.MallData;
import com.zhongjian.webserver.mapper.AdvMapper;
import com.zhongjian.webserver.mapper.ProductMapper;
import com.zhongjian.webserver.pojo.Product;
import com.zhongjian.webserver.pojo.Tag;
import com.zhongjian.webserver.service.HomePageService;

@Service
public class HomePageServiceImpl implements HomePageService{

	@Autowired
	AdvMapper advMapper;
	
	@Autowired
	ProductMapper productMapper;
	
	@Autowired
	MallData mallData;
	
	@Override
	public HashMap<String, Object> initHomePage(Integer productNum) {
		//每个专区要取的数量
		Integer needSize = productNum;
		HashMap<String, Object> result = new HashMap<>();
		result.put("picList", advMapper.getHomePagePic());
		 HashMap<String, Object> titleData = advMapper.getHomePageTitle();
		if (titleData == null) {
			titleData = new HashMap<>();
		}
		result.put("tile", titleData);
		//除去会员专区
		List<Tag> tags = productMapper.getAllTagProduct(mallData.getProductTag());
		Integer tagSize = tags.size();
		Random rand = new Random();  
		for (int i = 0; i < tagSize ; i++) {
			List<Product> products = tags.get(i).getProductsOfTag();
			Integer productSize = products.size();
			//如果productSize小于needSize,那就不取
			if (productSize <= needSize) {
				continue;
			}else {
				List<Product> ranProducts = new ArrayList<>();
				for (int j = 0; j < needSize; j++) {
					int myRand = rand.nextInt(productSize); 
					Product randProduct = new Product();
					Product curProduct = products.get(myRand);
					randProduct.setId(curProduct.getId());
					randProduct.setProductname(curProduct.getProductname());
					randProduct.setElecnum(curProduct.getElecnum());
					randProduct.setPrice(curProduct.getPrice());
					randProduct.setOldprice(curProduct.getOldprice());
					randProduct.setProductphotos(curProduct.getProductphotos());
					ranProducts.add(randProduct);
				}
				//最终完成此tag中的products抽取
				tags.get(i).setProductsOfTag(ranProducts);
			}
		}
		result.put("products",tags);
		return result;
	}

	@Override
	public List<Product> getAreaProducts(Integer tag,String condition , Integer page,Integer pageNum) {
		return productMapper.getProductsOfTag( tag, condition, page, pageNum);
	}

}

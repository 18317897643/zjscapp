package com.zhongjian.webserver.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.mapper.ProxyApplyMapper;
import com.zhongjian.webserver.mapper.ShoppingCartMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.pojo.Product;
import com.zhongjian.webserver.pojo.ProxyApply;
import com.zhongjian.webserver.pojo.ShoppingCart;
import com.zhongjian.webserver.service.PersonalCenterService;

@Service
public class PersonalCenterServiceImpl implements PersonalCenterService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	ShoppingCartMapper shoppingCartMapper;
	
	@Autowired
	ProxyApplyMapper proxyApplyMapper;

	@Override
	public Map<String, Object> getInformOfConsumption(String userName) {
		return userMapper.selectPersonalInform(userName);
	}
	@Override
	public List<Integer> getUserOrderStatus(Integer userId) {
		return orderMapper.getUserOrderStatus(userId);
	}
	@Override
	public List<HashMap<String, Object>> getShoppingCartInfo(Integer userId) {
		List<ShoppingCart> shoppingCarts = shoppingCartMapper.getShoppingCartInfo(userId);
		Integer shoppingCartsSize = shoppingCarts.size();
		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> producerNameMap = new HashMap<>();
		HashMap<String, List<ShoppingCart>> shoppingCartmap = new HashMap<>();
		for (int i = 0; i < shoppingCartsSize; i++) {
			Product product = shoppingCarts.get(i).getProduct();
			String producerNo = product.getProducerno();
			String producerName = product.getProducername();
			if (producerNameMap.putIfAbsent(producerNo, producerName) == null) {
				List<ShoppingCart> temporaryList = new ArrayList<ShoppingCart>();
				temporaryList.add(shoppingCarts.get(i));
				shoppingCartmap.put(producerNo, temporaryList);

			} else {
				shoppingCartmap.get(producerNo).add(shoppingCarts.get(i));
			}
		}
		for (String key : producerNameMap.keySet()) {
			HashMap<String, Object> result = new HashMap<>();
			result.put("producerName", producerNameMap.get(key));
			result.put("productList", shoppingCartmap.get(key));
			resultList.add(result);
		}
		return resultList;
	}
	@Override
	public boolean delShoppingCartInfoById(Integer userId, Integer id) {
		if (shoppingCartMapper.delShoppingCartInfoById(userId, id) == 1) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public Integer addShoppingCartInfo(Integer userId, Integer productId, Integer specId, Integer productNum,Date CreateTime) {
		return shoppingCartMapper.addShoppingCartInfo(userId, productId, specId, productNum,CreateTime);
	}
	@Override
	public Integer setShoppingCartInfo(Integer userId, Integer shoppingCartId, Integer productNum) {
		return shoppingCartMapper.setShoppingCartInfo(userId, shoppingCartId, productNum);
	}
	@Override
	public Integer getProductIdByShoppingId(Integer shoppingCartId) {
		return shoppingCartMapper.getProductIdByShoppingId(shoppingCartId);
	}
	@Override
	public boolean isAlreadyApply(Integer UserId) {
		Integer curStatus = proxyApplyMapper.queryProxyApplyCurStatus(UserId);
		if (curStatus == null || curStatus == -1) {
			return false;
		}else {
			return true;
		}
	}
	@Override
	public ProxyApply getProxyApply(Integer UserId) {
		return proxyApplyMapper.queryProxyApply(UserId);
	}
}
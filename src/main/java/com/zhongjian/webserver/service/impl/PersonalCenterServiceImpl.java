package com.zhongjian.webserver.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhongjian.webserver.mapper.OrderMapper;
import com.zhongjian.webserver.mapper.ProxyApplyMapper;
import com.zhongjian.webserver.mapper.ShoppingCartMapper;
import com.zhongjian.webserver.mapper.UserMapper;
import com.zhongjian.webserver.pojo.BillReacord;
import com.zhongjian.webserver.pojo.Orderhead;
import com.zhongjian.webserver.pojo.Orderline;
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
	public Integer addShoppingCartInfo(Integer userId, Integer productId, Integer specId, Integer productNum,
			Date CreateTime) {
		return shoppingCartMapper.addShoppingCartInfo(userId, productId, specId, productNum, CreateTime);
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
		} else {
			return true;
		}
	}

	@Override
	public ProxyApply getProxyApply(Integer UserId) {
		return proxyApplyMapper.queryProxyApply(UserId);
	}

	@Override
	public Orderhead getOrderDetailsById(Integer id) {
		Orderhead orderhead = orderMapper.getOrderDetailsById(id);
		List<Orderline> orderlines = orderhead.getOrderlines();
		for (int j = 0; j < orderlines.size(); j++) {
			Orderline orderline = orderlines.get(j);
			// 获取商品id
			Integer productId = orderline.getProductId();
			if (productId == null) {
				orderline.setProductId(0);
			}
			String photo = orderMapper.getPhotoByProductId(productId);
			if (photo == null) {
				orderline.setPhoto("");
			} else {
				orderline.setPhoto(photo);
			}
		}
		return orderhead;
	}

	@Override
	public boolean isAlreadyAuth(Integer UserId) {
		Integer curStatus = userMapper.queryUserAuth(UserId);
		if (curStatus == 2) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public boolean isGCMember(Integer UserId) {
		Date expireTime = userMapper.getExpireTimeFromGcOfUser(UserId);
		// 如果当前时间大于大于过期时间则过期
		if (expireTime == null || new Date().getTime() > expireTime.getTime()) {
			return false;
		}
		return true;
	}

	@Override
	public List<BillReacord> accountBill(Integer userId, String type, Integer page, Integer pageNum) {
		page = page * pageNum;
		if ("coupon".equals(type)) {
			return userMapper.getCouponBill(userId, page, pageNum);
		} else if ("points".equals(type)) {
			return userMapper.getPointBill(userId, page, pageNum);
		} else if ("vip".equals(type)) {
			return userMapper.getVipBill(userId, page, pageNum);
		} else if ("elec".equals(type)) {
			return userMapper.getElecBill(userId, page, pageNum);
		} else {
			return null;
		}
	}

	@Override
	public List<Orderhead> getOrderDetailsByCurStatus(Integer userId, String condition,Integer page,Integer pageNum) {
		Integer offSet = page * pageNum;
		List<Orderhead> orderheads = orderMapper.getOrderDetailsByCurStatus(userId, condition,offSet,pageNum);
		for (int i = 0; i < orderheads.size(); i++) {
			Integer tolNum = 0;
			List<Orderline> orderlines = orderheads.get(i).getOrderlines();
			for (int j = 0; j < orderlines.size(); j++) {
				Orderline orderline = orderlines.get(j);
				// 订单中每个商品的数量
				tolNum += orderline.getProductnum();
				// 获取商品id
				Integer productId = orderline.getProductId();
				if (productId == null) {
					orderline.setProductId(0);
				}
				String photo = orderMapper.getPhotoByProductId(productId);
				if (photo == null) {
					orderline.setPhoto("");
				} else {
					orderline.setPhoto(photo);
				}

			}
			orderheads.get(i).setTolnum(tolNum);
		}
		return orderheads;
	}

	@Override
	public void addProxyApply(ProxyApply proxyApply, Integer userId) {
		proxyApply.setCurstatus(0);
		proxyApply.setUserid(userId);
		proxyApply.setCreatetime(new Date());
		proxyApplyMapper.addProxyApply(proxyApply);
	}

	@Override
	public void updateProxyApply(ProxyApply proxyApply, Integer userId) {
		proxyApply.setCurstatus(0);
		proxyApply.setUserid(userId);
		proxyApplyMapper.updateProxyApply(proxyApply);

	}
}
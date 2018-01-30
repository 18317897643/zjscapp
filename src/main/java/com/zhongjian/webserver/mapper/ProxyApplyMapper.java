package com.zhongjian.webserver.mapper;

import java.util.Map;

import com.zhongjian.webserver.pojo.ProxyApply;

public interface ProxyApplyMapper {
	
  Integer queryProxyApplyCurStatus(Integer UserId);
  
  Map<String, Object> queryProxyApply(Integer UserId);
  
  void updateProxyApply(ProxyApply proxyApply);
  
  void addProxyApply(ProxyApply proxyApply);
}

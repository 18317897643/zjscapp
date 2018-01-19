package com.zhongjian.webserver.mapper;

import com.zhongjian.webserver.pojo.ProxyApply;

public interface ProxyApplyMapper {
	
  Integer queryProxyApplyCurStatus(Integer UserId);
  
  ProxyApply queryProxyApply(Integer UserId);
  
  void updateProxyApply(ProxyApply proxyApply);
  
  void addProxyApply(ProxyApply proxyApply);
}

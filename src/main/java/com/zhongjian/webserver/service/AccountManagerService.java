package com.zhongjian.webserver.service;

import java.util.List;

import com.zhongjian.webserver.pojo.Alipaylist;
import com.zhongjian.webserver.pojo.Bankcardlist;

public interface AccountManagerService {

	void addAliAccount(Integer userId,String account,String name);
	
	Integer deleteAliAccount(Integer id,Integer userId);
	
	void addBankAccount(Integer userId,String account,String bankName,String name);
	
	Integer deleteBankAccount(Integer id,Integer userId);
	
	List<Alipaylist> getAllAliAccount(Integer userId);
	
	List<Bankcardlist> getAllBankAccount(Integer userId);
}

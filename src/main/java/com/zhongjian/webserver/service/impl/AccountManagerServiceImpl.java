package com.zhongjian.webserver.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongjian.webserver.mapper.AlipaylistMapper;
import com.zhongjian.webserver.mapper.BankcardlistMapper;
import com.zhongjian.webserver.pojo.Alipaylist;
import com.zhongjian.webserver.pojo.Bankcardlist;
import com.zhongjian.webserver.service.AccountManagerService;

@Service
public class AccountManagerServiceImpl implements AccountManagerService {

	@Autowired
	private AlipaylistMapper alipaylistMapper;
	
	@Autowired
	private BankcardlistMapper bankcardlistMapper;
	
	@Override
	public void addAliAccount(Integer userId, String account, String name) {
		
		Alipaylist record = new Alipaylist();
		record.setAccount(account);
		record.setCreatetime(new Date());
		record.setCurstatus(0);
		record.setName(name);
		record.setUserid(userId);
		alipaylistMapper.insert(record);
	}

	@Override
	public Integer deleteAliAccount(Integer id,Integer userId) {
		return alipaylistMapper.deleteAliAccount(id, userId);
	}

	@Override
	public void addBankAccount(Integer userId, String account, String bankName ,String name) {
		Bankcardlist record = new Bankcardlist();
		record.setAccount(account);
		record.setBankname(bankName);
		record.setCreatetime(new Date());
		record.setCurstatus(0);
		record.setName(name);
		record.setUserid(userId);
		bankcardlistMapper.insert(record);
	}

	@Override
	public Integer deleteBankAccount(Integer id, Integer userId) {
		return bankcardlistMapper.deleteBankAccount(id, userId);
		 
	}

	@Override
	public List<Alipaylist> getAllAliAccount(Integer userId) {
		return alipaylistMapper.selectByUserId(userId);
	}

	@Override
	public List<Bankcardlist> getAllBankAccount(Integer userId) {
		return bankcardlistMapper.selectByUserId(userId);
	}

}

package com.zhongjian.webserver.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class TokenManager {

	
	private CopyOnWriteArrayList<String> tokenList = new CopyOnWriteArrayList<String>();

	private long listLargeLenth = 2000;

	private ConcurrentHashMap<String, String> user_token = new ConcurrentHashMap<String, String>();
	

	private TokenManager(long listLargeLenth) {
		this.listLargeLenth = listLargeLenth;
	}
	
	private TokenManager() {
	}
	
	public static TokenManager getTokenManager(long listLargeLenth){  
	   return new TokenManager(listLargeLenth);	
	}

	// 生成token
	private String createToken() {
		return UUID.randomUUID().toString();
	}

	public String storeOrUpdateToken(String username) {
		String token = createToken();
		if (tokenList.size() < listLargeLenth) {
			tokenList.remove(username);
			tokenList.add(username);
		} else {
			// 删除第一个元素
			String headUserName = tokenList.get(0);
			user_token.remove(headUserName);
			tokenList.remove(0);
			
			tokenList.remove(username);
			tokenList.add(username);
		}
		if (user_token.putIfAbsent(username, token) != null) {
			user_token.replace(username, token);
		}
		return token;
	}

	public boolean checkToken(String toKen) {
		boolean pass = false;
		Set<Entry<String, String>>  set = user_token.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,String> entry = (Map.Entry<String,String>) iterator.next();
			if (entry.getValue().equals(toKen)) {
				String userName = (String) entry.getKey();
				tokenList.remove(userName);
				tokenList.add(userName);
				pass = true;
				break;
			}

		}
		return pass;
	}
	
	public String checkTokenGetUser(String toKen) {
		Set<Entry<String, String>>  set = user_token.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		String theUsername = null;
		while (iterator.hasNext()) {
			Map.Entry<String,String> entry = (Map.Entry<String,String>) iterator.next();
			if (entry.getValue().equals(toKen)) {
				String userName = (String) entry.getKey();
				tokenList.remove(userName);
				tokenList.add(userName);
				theUsername = userName;
				break;
			}
		}
		return theUsername;
	}
	
	//清除一个toKen
	public void releaseToken(String toKen) {
		Set<Entry<String, String>>  set = user_token.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,String> entry = (Map.Entry<String,String>) iterator.next();
			if (entry.getValue().equals(toKen)) {
				String userName = (String) entry.getKey();
				tokenList.remove(userName);
				iterator.remove();
				break;
			}
		}
	}
  /**
   * 显示token
   */
//	public void showToken(){
//		System.out.println(this.tokenList);
//		System.out.println(this.user_token);
//	}
}

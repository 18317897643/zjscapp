package com.zhongjian.webserver.common;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 有过期时间的map
 * @author Administrator
 *
 * @param <K>
 * @param <V>
 */
public class ExpiryMap<K, V> extends ConcurrentHashMap<K, V>{  
    private static final long serialVersionUID = 1L; 
    
    private long EXPIRY = 1000 * 60 * 5;  
      
    public ConcurrentHashMap<K, Long> expiryMap = new ConcurrentHashMap<K, Long>();  
      
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    public ExpiryMap(){  
        super();  
    }  
    public ExpiryMap(long defaultExpiryTime){  
        this(1 << 4, defaultExpiryTime);  
    }  
    public ExpiryMap(int initialCapacity, long defaultExpiryTime){  
        super(initialCapacity);  
        this.EXPIRY = defaultExpiryTime;  
    } 
    public V put(K key, V value) {  
    	V value1 = null;
    	readWriteLock.readLock().lock();
    	try {
    	     expiryMap.put(key, System.currentTimeMillis() + EXPIRY);
    	     value1 = super.put(key, value);	
    	     return value1;
		} finally {
			 readWriteLock.readLock().unlock();
		}
    }   
    public V get(Object key) {
    	V value = null;
    	readWriteLock.readLock().lock();
    	try {
    		if (key == null) {
           	 return null;
           }
           if(checkExpiry(key, true)){ 
           	  return null;  
           }
           value = super.get(key);
           return value;
		} finally {
			readWriteLock.readLock().lock();
		}
    }  

    /**
     * Release resources
     */
    public void release() {
    	  Set<java.util.Map.Entry<K, V>> set = super.entrySet();  
          Iterator<java.util.Map.Entry<K, V>> iterator = set.iterator();
          while (iterator.hasNext()) {  
              java.util.Map.Entry<K, V> entry = iterator.next();  
             checkExpiry(entry.getKey(), true);
          }  
            
	}
    
    /** 
     *  
         * @Description: 是否过期  
         * @author: chen_di 
         * @date: 2016-11-24 下午4:05:02 
         * @param expiryTime true 过期 
         * @param isRemoveSuper true super删除 
         * @return 
     */  
    private boolean checkExpiry(Object key, boolean isRemoveSuper){  
        if(!expiryMap.containsKey(key)){  
            return Boolean.FALSE;  
        }  
        long expiryTime = expiryMap.get(key);  
        boolean flag = System.currentTimeMillis() > expiryTime;  
        if(flag){  
            if(isRemoveSuper)  
                super.remove(key);  
            expiryMap.remove(key);  
        }  
        return flag;  
    }  
}  

package com.zhongjian.webserver.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zhongjian.webserver.ExceptionHandle.shareBenefitException;

public class ShareBenefitUtil {
	// 得到排列组合
	public static Map<Integer, List<Integer>> arrange(List<Integer> list, int num) {
//		System.out.println(list);
		int count = 0;
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		long n = (long) Math.pow(2, list.size());
		List<Integer> combine;
		List<Integer> key;
		for (long l = 0L; l < n; l++) {
			combine = new ArrayList<Integer>();
			key = new ArrayList<Integer>();
			for (int i = 0; i < list.size(); i++) {
				if ((l >>> i & 1) == 1) {
					combine.add(list.get(i));
					key.add(i);
				}
			}
			for (int i = 0; i < combine.size(); i++) {
				count += combine.get(i);
			}
			if (count > num) {
				map.put(count, key);
				key = new ArrayList<Integer>();
			}
			count = 0;
		}
		return map;
	}
	// 求出超过num的最优解，并删除这些数据
	public static boolean del(List<Integer> list, int num) {
		List<Integer> _list = new ArrayList<Integer>();
		List<Integer> delList = new ArrayList<Integer>();
		int maxK = Integer.MAX_VALUE;
		Map<Integer, List<Integer>> threeMap = arrange(list, num);
		if (!threeMap.isEmpty()) {
			Iterator<Integer> keys = threeMap.keySet().iterator();
			while (keys.hasNext()) {
				int key = (int) keys.next();
				if (key < maxK) {
					maxK = key;
				}
			}
			delList = threeMap.get(maxK);
			for (int i = 0; i < delList.size(); i++) {
				_list.add(list.get(delList.get(i)));
			}
			list.removeAll(_list);
			_list = new ArrayList<Integer>();
			delList = new ArrayList<Integer>();
			maxK = Integer.MAX_VALUE;
			return true;
		} else {
			return false;
		}
	}
	// num为每个组需要满足的条件，
	public static boolean isProxy(List<Integer> list, int num,int tag) throws shareBenefitException {
		if (tag <= 0 || num <=0){
			throw new shareBenefitException("tag and num can't be less than 0");
		}
		if (list.size() < tag) {
			throw new shareBenefitException("the size of list can't be less than the tag");
		}
			list = new ArrayList<Integer>(list);
			while (tag > 0) {
				if (!del(list, num)) {
					return false;
					}
				tag--;
				}
			return true;

	}
}



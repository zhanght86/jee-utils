package com.ckjava.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {
	
	public static void formatListELement(List<String> srcList, List<Integer> destList) throws Exception {
		if (srcList == null || destList == null) {
			throw new RuntimeException("srcList or destList is null");
		}
		for (String str : srcList) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			destList.add(Integer.parseInt(str));
		}
	}
	
	/**
	 * 获取 集合 对象的大小，如果集合为空，返回0
	 * @param cols 目标集合对象
	 * @return
	 */
	public static int getSize(Collection<?> cols) {
		return cols != null ? cols.size() : 0;
	}
	
	/**
	 * 获取Map里面的值，如果Map为空，返回null
	 * @param data Map 对象
	 * @param key Map 中的key
	 * @return
	 */
	public static <K,V> V getVal(Map<K,V> data, K key) {
		return data != null && !data.isEmpty() ? data.get(key) : null;
	}
	
	/**
	 * 将指定的键值对集合放入到 HashMap中
	 * @param keys 键数组
	 * @param values 值数组
	 * @return
	 */
	public static <K,V> Map<K, V> asHashMap(K[] keys, V[] values) {
		if (keys == null || values == null) {
			throw new RuntimeException("键数组和值数组不能为空");
		}
		Map<K, V> map = new HashMap<K, V>();
		for (int i = 0, c = keys.length; i < c; i++) {
			map.put(keys[i], values[i]);
		}
		return map;
	}
	
	/**
	 * 将指定的键值对集合放入到指定Map对象中
	 * @param keys 键数组
	 * @param values 值数组
	 * @return
	 */
	public static <K,V,M extends Map<K, V>> Map<K, V> asMap(K[] keys, V[] values, M map) {
		if (keys == null || values == null) {
			throw new RuntimeException("键数组和值数组不能为空");
		}
		if (map == null) {
			throw new RuntimeException("指定的Map对象不能为空");
		}
		for (int i = 0, c = keys.length; i < c; i++) {
			map.put(keys[i], values[i]);
		}
		return map;
	}
	
}

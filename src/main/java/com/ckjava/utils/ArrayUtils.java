package com.ckjava.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {
	
	/**
	 * 根据下标从数组中获取数据
	 * 
	 * @param arr 数组
	 * @param index 下标
	 * @return 数组的值
	 */
	public static <T> T getValue(T[] arr, int index) {
		if (arr == null || arr.length == 0 || index > arr.length-1 || index < 0) {
			return null;
		}
		return arr[index];
	}
	
	/**
	 * 根据下标从数组中获取数据，结果为空的情况下返回默认值
	 * 
	 * @param arr 数组
	 * @param index 下标
	 * @param defaultValue 默认值
	 * @return 数组的值
	 */
	public static <T> T getValue(T[] arr, int index, T defaultValue) {
		if (arr == null || arr.length == 0 || index > arr.length-1 || index < 0) {
			return defaultValue;
		}
		return arr[index] == null ? defaultValue : arr[index];
	}
	
	/**
	 * 获取数组的长度
	 * @param arr 数组
	 * @return int 数组长度
	 */
	public static <T> int getSize(T[] arr) {
		return arr == null ? 0 : arr.length;
	}
	
	/**
	 * 将数组以分隔符 separator 为分界合并起来
	 * @param arr 数组
	 * @param separator 分隔符
	 * @return 合并后的字符串
	 */
	public static <T> String join(T[] arr, String separator) {
		if (arr == null || arr.length == 0) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		for (T t : arr) {
			result.append(t).append(separator);
		}
		return result.toString().substring(0, result.toString().lastIndexOf(separator));
	}
	
	/**
	 * 将多个数组合并成一个后返回
	 * 
	 * {
	 * String[] str1 = {"1", "2"};
	 * String[] str2 = {"3", "4"};
	 * 
	 * String[] str3 = merge(str1, str2);
	 * 
	 * str3 = {"1", "2", "3", "4"}
	 * return str3
	 * }
	 * 
	 * @param T[]...arrs 
	 * @return T[]
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> T[] merge(T[]...arrs) {
		int size = getSize(arrs);
		if (size == 0) {
			return null;
		}
		if (size == 1) {
			return arrs[0];
		}
		
        List<T> tempList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			T[] t = getValue(arrs, i);
			for (int j = 0, c = getSize(t); j < c; j++) {
				tempList.add(getValue(t, j));
			}
		}
		T[] array = (T[]) Array.newInstance(arrs[0].getClass().getComponentType(), tempList.size()); 
		return tempList.toArray(array);
	}
}

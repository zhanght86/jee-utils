package com.ckjava.utils;

public class ArrayUtils {
	
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
}

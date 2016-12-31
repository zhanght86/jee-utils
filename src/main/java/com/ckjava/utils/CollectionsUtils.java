package com.ckjava.utils;

import java.util.List;

public class CollectionsUtils {
	
	public static boolean isEmpty(List<?> list) {
		if (list == null || list.size() == 0) {
			return true; 
		}
		return false;
	}
	
}

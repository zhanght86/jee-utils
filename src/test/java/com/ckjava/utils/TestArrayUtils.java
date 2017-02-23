package com.ckjava.utils;

import org.junit.Assert;
import org.junit.Test;

public class TestArrayUtils {
	
	@Test
	public void TestGetValue() {
		String[] arr = {"1"};
		String str = ArrayUtils.getValue(arr, 1);
		Assert.assertNull(str);
		str = ArrayUtils.getValue(arr, 0);
		Assert.assertNotNull(str);
		str = ArrayUtils.getValue(null, 0);
		Assert.assertNull(str);
		str = ArrayUtils.getValue(arr, 0);
		Assert.assertEquals("1", str);
	}
	
	@Test
	public void TestGetSize() {
		String[] arr = {"1"};
		Assert.assertEquals(1, ArrayUtils.getSize(arr));
	}
	
	@Test
	public void TestJoin() {
		Integer[] arr = {1, 2, 3, 4};
		String str = ArrayUtils.join(arr, ",");
		System.out.println(str);
		Assert.assertEquals(str, "1,2,3,4");
	}
}

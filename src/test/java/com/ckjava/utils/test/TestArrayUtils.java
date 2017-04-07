package com.ckjava.utils.test;

import org.junit.Assert;
import org.junit.Test;

import com.ckjava.utils.ArrayUtils;

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
	public void TestMerge() {
		Integer[] arr = {1, 2, 3, 4};
		Integer[] arr2 = {6, 9};
		
		Integer[] arr3 = {1, 2, 3, 4, 6, 9};
		
		Integer[] data = ArrayUtils.merge(arr, arr2);
		Assert.assertEquals(data.length, arr.length + arr2.length);
		
		for (int i = 0; i < data.length; i++) {
			Assert.assertEquals(data[i], arr3[i]);
		}
		
		String[] data1 = {"hello ", "world"};
		String[] data2 = {" !"};
		String str = "hello world !";
		String[] afterMergeArr = ArrayUtils.merge(data1, data2);
		Assert.assertEquals(ArrayUtils.join(afterMergeArr, ""), str);
	}
	
	
}

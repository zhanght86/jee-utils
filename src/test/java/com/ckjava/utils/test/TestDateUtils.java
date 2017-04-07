package com.ckjava.utils.test;

import org.junit.Assert;
import org.junit.Test;

import com.ckjava.utils.DateUtils;

public class TestDateUtils {
	
	@Test
	public void TestGetCurrentTimeStamp() {
		String datetime = DateUtils.getCurrentTimeStamp();
		Assert.assertNotNull(datetime);
	}
}

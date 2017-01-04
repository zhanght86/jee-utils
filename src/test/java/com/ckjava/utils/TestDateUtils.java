package com.ckjava.utils;

import org.junit.Assert;
import org.junit.Test;

public class TestDateUtils {
	
	@Test
	public void TestGetCurrentTimeStamp() {
		String datetime = DateUtils.getCurrentTimeStamp();
		Assert.assertNotNull(datetime);
	}
}

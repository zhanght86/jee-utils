package com.ckjava.utils.test;

import org.junit.Assert;
import org.junit.Test;

import com.ckjava.utils.OSUtils;

public class TestOSUtils {

	@Test
	public void testOSType() {
		String osType = OSUtils.getCurrentOSType();
		System.out.println(osType);
		Assert.assertNotNull(osType);
	}
}

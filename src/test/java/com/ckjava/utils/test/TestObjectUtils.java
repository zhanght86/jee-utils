package com.ckjava.utils.test;

import java.io.Serializable;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.ckjava.utils.ObjectUtils;

public class TestObjectUtils {
	
	@Test
	@SuppressWarnings("unchecked")
	public void objectToBytes() {
		String name = "ck";
		String age = "30";
		HashMap<String, String> data = new HashMap<>();
		data.put("name", name);
		data.put("age", age);
		byte[] bytes = ObjectUtils.objectToBytes(data);
		
		HashMap<String, String> objMap = (HashMap<String, String>) ObjectUtils.bytesToObject(bytes);
		Assert.assertEquals(objMap.get("name"), name);
		Assert.assertEquals(objMap.get("age"), age);
	}

	@Test
	public void testGetObjectString() {
		User user = new User("ck",30);
		String str = ObjectUtils.getObjectString(user);
		System.out.println(str);
		user = new User();
		str = ObjectUtils.getObjectString(user);
		System.out.println(str);
	}
	
	public class User implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private int age;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public User(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}
		public User() {
			super();
		}
		
	}
}

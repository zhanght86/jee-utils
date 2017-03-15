package com.ckjava.utils;

import java.io.Serializable;

import org.junit.Test;

public class TestObjectUtils {

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

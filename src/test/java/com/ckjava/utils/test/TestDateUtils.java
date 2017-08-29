package com.ckjava.utils.test;

import java.util.Date;

import org.junit.Test;

import com.ckjava.utils.DateUtils;

public class TestDateUtils {
	
	@Test
	public void TestGetCurrentTimeStamp() {
	}
	
	@Test 
	public void testWeak() {
		System.out.println(DateUtils.getWeek(new Date()));
	}
	
	@Test
	public void getWeekoneAndLast() {
		Date monday = DateUtils.getMonday(null);
		
		System.out.println(DateUtils.getDateString(monday, DateUtils.DATETIME));
		
		Date currentSunday = DateUtils.getAssignDay(monday, 6);
		Date nextMon = DateUtils.getAssignDay(monday, 7);
		Date nextSun = DateUtils.getAssignDay(monday, 13);
		System.out.println(DateUtils.getDateString(currentSunday, DateUtils.DATETIME));
		System.out.println(DateUtils.getDateString(nextMon, DateUtils.DATETIME));
		System.out.println(DateUtils.getDateString(nextSun, DateUtils.DATETIME));
	}
	
	@Test
	public void getAssignDay() {
		Date zhouri = DateUtils.getAssignDay(new Date(), 4);
		System.out.println(DateUtils.getDateString(zhouri, DateUtils.DATETIME));
		
		Date last_1 = DateUtils.getAssignDay(zhouri, -6);
		Date last_5 = DateUtils.getAssignDay(zhouri, -2);
		Date next_1 = DateUtils.getAssignDay(zhouri, 1);
		Date next_5 = DateUtils.getAssignDay(zhouri, 5);
		
		System.out.println("last_1:"+last_1.getTime());
		System.out.println("last_5:"+last_5.getTime());
		
		System.out.println("last_1:"+last_1.getTime()/1000);
		System.out.println("last_5:"+last_5.getTime()/1000);
		
		System.out.println(DateUtils.getDateString(last_1, DateUtils.DATETIME));
		System.out.println(DateUtils.getDateString(last_5, DateUtils.DATETIME));
		System.out.println(DateUtils.getDateString(next_1, DateUtils.DATETIME));
		System.out.println(DateUtils.getDateString(next_5, DateUtils.DATETIME));
	}
	
	@Test
	public void getBeginDay() {
		Date date = DateUtils.getBeginDay(new Date());
		System.out.println(DateUtils.getDateString(date, DateUtils.DATETIME));
	}
	
	@Test
	public void getEndDay() {
		Date date = DateUtils.getEndDay(new Date());
		System.out.println(DateUtils.getDateString(date, DateUtils.DATETIME));
	}
	
	@Test
	public void formatTime() {
		long time = 1501862340000L;
		System.out.println(DateUtils.formatTime(time, DateUtils.DATETIME));
		System.out.println(DateUtils.formatTime(time, "MM-dd"));
	}
}

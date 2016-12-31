package com.ckjava.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtil extends DateUtils {
	private static DateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat time = new SimpleDateFormat("HH:mm:ss");
	private static DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
			"yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}
    /**
     * 分割时间段
    * @Title: getPeriodTime 
    * @Description: TODO 
    * @param @param fromTime
    * @param @param endTime
    * @param @param period
    * @param @return
    * @param @throws ParseException    
    * @return List<Map<String,String>>
     */
	public static List<Map<String, String>> getPeriodTime(String fromTime, String endTime,int period) throws ParseException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fromDate = sdf.parse(fromTime);
		Date endDate = sdf.parse(endTime);
		Date covDate = fromDate;
		while (covDate.getTime() < endDate.getTime()) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(fromDate);
			rightNow.add(Calendar.MINUTE, period);// 加30分钟
			covDate = rightNow.getTime();
			Map<String,String> map = new HashMap<String, String>();
			map.put("fromTime", formatDateTime(fromDate));
			map.put("endTime", formatDateTime(covDate));
			list.add(map);
			fromDate = covDate;
		}
		return list;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	
	/**
	 * 获取指定类型的时间格式
	 * @param dateFormat
	 * @return
	 */
	public static String getDateString(String dateFormat) {
		return new SimpleDateFormat(dateFormat).format(new Date());
	}
	
	public static String getCurrentTimeStamp() {
		return timeStamp.format(new Date());
	}
	
	public static String getCurrentDate() {
		return date.format(new Date());
	}
	
	public static String getCurrentTime() {
		return time.format(new Date());
	}
	
	public static String getCurrentDateTime() {
		return dateTime.format(new Date());
	}
	
	public static String getTimeStamp(long value) {
		return timeStamp.format(new Date(value));
	}
	
	public static String getDate(long value) {
		return date.format(new Date(value));
	}
	
	public static String getTime(long value) {
		return time.format(new Date(value));
	}
	
	public static String getDateTime(long value) {
		return dateTime.format(new Date(value));
	}
	
	public static String getAssignDay(int offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, offset);
		return date.format(calendar.getTime());
	}
	
	public static void main(String[] args) {
/*		System.out.println(getCurrentDate());
		System.out.println(getCurrentTime());
		System.out.println(getCurrentDateTime());
		System.out.println(getCurrentTimeStamp());
		System.out.println("--------------------------------");
		long value = new Date().getTime();
		System.out.println(getTimeStamp(value));
		System.out.println(getTime(value));
		System.out.println(getDate(value));
		System.out.println(getDateTime(value));*/
		
		System.out.println(getAssignDay(0));
	}
}

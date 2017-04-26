package com.ckjava.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 将 java.util.Date 类型转成 String 类型，或者将 String 类型转成 java.util.Date 类型
 * @author ck
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	public static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE = "yyyy-MM-dd";
	public static final String TIME = "HH:mm:ss";
	public static final String YEAR = "yyyy";
	public static final String MONTH = "MM";
	public static final String DAY = "dd";
	public static final String WEEKDAY = "E";
	
	public static final String[] PATTERNS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
			"yyyy.MM.dd HH:mm", "yyyy.MM" };


	/**
	 * 获取指定时间的字符串格式
	 * 
	 * @param date Date
	 * @param pattern @see DateUtils.TIMESTAMP, DateUtils.DATETIME, DateUtils.DATE, DateUtils.TIME
	 * @see DateFormatUtils
	 * @return String
	 */
	public static String getDateString(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDateString(Date date, Object... pattern) {
		if (ArrayUtils.isNotEmpty(pattern)) {
			return DateFormatUtils.format(date, pattern[0].toString());
		} else {
			return DateFormatUtils.format(date, "yyyy-MM-dd");
		}
	}


	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear(Date date) {
		return getDateString(date, YEAR);
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth(Date date) {
		return getDateString(date, MONTH);
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay(Date date) {
		return getDateString(date, DAY);
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek(Date date) {
		return getDateString(date, WEEKDAY);
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
			return parseDate(str.toString(), PATTERNS);
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
	public static List<Map<String, String>> getPeriodTime(String fromTime, String endTime, int period) throws ParseException {
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
			map.put("fromTime", getDateString(fromDate, DATETIME));
			map.put("endTime", getDateString(covDate, DATETIME));
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
	
	public static String getAssignDay(int offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, offset);
		return DateFormatUtils.format(calendar.getTime(), DateUtils.DATE);
	}
	
}

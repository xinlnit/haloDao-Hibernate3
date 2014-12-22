package com.ht.halo.hibernate3.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



/**
 * @ClassName: HtDateUtil
 * @Description: TODO 取自锐道DateUtils
 * @author fengchangyi
 * @date 2014-7-4 上午8:34:01
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	private DateUtils() {
	}

	public static TimeZone getGMTTimeZone() {
		return GMT;
	}
     /**
     * @Title: dateString
     * @Description: TODO 常用 取得日期字符串
     * @param date
     * @return
     */
    public  static  String dateString(Date date){
    	return  format("yyyyMMdd", date); 
     }
     /**
     * @Title: dateString
     * @Description: TODO 常用 取得当前日期字符串
     * @return
     */
    public  static  String dateString(){
     	return  dateString(new Date()); 
      }
	private static TimeZone getDefaultTimeZone() {
		return  null;
	}

	public static Date parse(String dateText) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat();
		TimeZone timeZone = getDefaultTimeZone();
		if (timeZone != null) {
			sdf.setTimeZone(timeZone);
		}
		return sdf.parse(dateText);
	}

	public static Date parse(String format, String dateText) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		TimeZone timeZone = getDefaultTimeZone();
		if (timeZone != null) {
			sdf.setTimeZone(timeZone);
		}
		return sdf.parse(dateText);
	}

	public static String format(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		TimeZone timeZone = getDefaultTimeZone();
		if (timeZone != null) {
			sdf.setTimeZone(timeZone);
		}
		return sdf.format(date);
	}

	public static String format(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		TimeZone timeZone = getDefaultTimeZone();
		if (timeZone != null) {
			sdf.setTimeZone(timeZone);
		}
		return sdf.format(date);
	}

	/**
	 * @Title: mouthSub
	 * @Description: TODO 日期相减
	 * @param one
	 * @param two
	 * @return
	 */
	public static int mouthSub(Date one, Date two) {
		Calendar c = Calendar.getInstance();
		c.setTime(one);
		int year1 = c.get(Calendar.YEAR);
		int month1 = c.get(Calendar.MONTH);
		c.setTime(two);
		int year2 = c.get(Calendar.YEAR);
		int month2 = c.get(Calendar.MONTH);
		int result;
		if (year1 == year2) {
			result = month1 - month2;
		} else {
			result = 12 * (year1 - year2) + month1 - month2;
		}
		return result;
	}
}

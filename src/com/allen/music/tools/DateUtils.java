package com.allen.music.tools;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			Date time = dateFormater2.get().parse(sdate);
			return time;
		} catch (ParseException e) {
			return null;
		}
	}

	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "未知";
		}

		String ftime = "";

		// 判断是否是同一天
		Calendar cal = Calendar.getInstance();
		String curDate = dateFormater.get().format(cal.getTime());
		String paramDate = dateFormater.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		// 如果不是一天
		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}

		return ftime;

	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 当前时间转化unix时间戳
	 * 
	 * @param sdate
	 * @return boolean
	 * @throws ParseException
	 * @return String unixDate =
	 *         String.valueOf(DateUtils.getUnixTime()).substring(0, 10);
	 */
	public static String getUnixTime() throws ParseException {
		Timestamp appointTime = Timestamp.valueOf(dateFormater.get().format(new Date()));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = df.parse(String.valueOf(appointTime));
		return date.getTime() + "";
	}

	/**
	 * unix时间戳转化普通时间
	 * 
	 * @param sdate
	 * @return boolean
	 * @throws ParseException
	 * @return String unixDate =
	 *         String.valueOf(DateUtils.getUnixTime()).substring(0, 10);
	 */
	public static String getUnixTime(String UnixTimestamp) {
		String date = dateFormater.get().format(Long.parseLong(UnixTimestamp) * 1000);
		return date;
	}

	// --------------------------------------------------------------------------------
	public static String getFileNameByDateTime() {
		SimpleDateFormat dfTemp = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dateTemp = new Date();
		return dfTemp.format(dateTemp);
	}

	/**
	 * now datetime(yyyy-MM-dd HH:mm:ss)
	 */
	public static String getFullDateTime() {
		SimpleDateFormat dfTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTemp = new Date();
		return dfTemp.format(dateTemp);
	}

	/**
	 * datetime(yyyy-MM-dd HH:mm:ss) of datePara
	 */
	public static String getFullDateTime(Date datePara) {
		SimpleDateFormat dfTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dfTemp.format(datePara);
	}

	/**
	 * now date(yyyy-MM-dd)
	 */
	public static String getDate() {
		SimpleDateFormat dfTemp = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTemp = new Date();
		return dfTemp.format(dateTemp);
	}

	/**
	 * date(yyyy-MM-dd) of datePara
	 */
	public static String getDate(Date datePara) {
		SimpleDateFormat dfTemp = new SimpleDateFormat("yyyy-MM-dd");
		return dfTemp.format(datePara);
	}

	/**
	 * now time(HH:mm:ss)
	 */
	public static String getTime() {
		SimpleDateFormat dfTemp = new SimpleDateFormat("HH:mm:ss");
		Date dateTemp = new Date();
		return dfTemp.format(dateTemp);
	}

	/**
	 * time(HH:mm:ss) of datePara
	 */
	public static String getTime(Date datePara) {
		SimpleDateFormat dfTemp = new SimpleDateFormat("HH:mm:ss");
		return dfTemp.format(datePara);
	}
}
package com.fyzs.tool;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static String startDay = "2017-02-12";// 开学日期
	public static String endDay = "2017-7-01";// 放假日期

	/**
	 * 获取当前时间是第几节课，只限8-16点之间,其他时间默认1，2节课。
	 * 
	 * @return
	 */
	public static int getNowCourse() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
		String nowDate = df.format(new Date());
		if (nowDate.startsWith("08") || nowDate.startsWith("09")) {
			return 1;// 12节课。
		} else if (nowDate.startsWith("10") || nowDate.startsWith("11")) {
			return 2;// 34节课，以此类推。
		} else if (nowDate.startsWith("12") || nowDate.startsWith("13")
				|| nowDate.startsWith("14")) {
			return 3;
		} else if (nowDate.startsWith("15") || nowDate.startsWith("16")) {
			return 4;
		} else {
			return 1;
		}
	}

	/**
	 * 获取当前时间是第几周
	 * 
	 * @return
	 */
	public static int getWeek() {
		int days = 0;
		int nowWeek = 0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			String nowDate = df.format(new Date());
			int nowDaysBetween = daysBetween(startDay, nowDate) + 1;
			days = daysBetween(startDay, endDay);
			int x = nowDaysBetween % 7;
			System.out.println(x+" "+nowDaysBetween/7+"  "+days);
			
			if (x == 0) {
				nowWeek = nowDaysBetween / 7;
			} else {
				nowWeek = nowDaysBetween / 7 + 1;
			}

		} catch (ParseException e) {
			System.out.println("输入的日期不合法，解析日期失败");
			e.printStackTrace();
		}
		return nowWeek;
	}

	/**
	 * 获取当前时间是星期几
	 * 
	 * @return
	 */
	public static int getWeekDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if (cal.get(Calendar.DAY_OF_WEEK) - 1 == 0) {
			return 7;
		}
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 计算两个String类型日期之间的天数
	 * 
	 * @param startDay
	 * @param endDay
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String startDay, String endDay)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(startDay));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(endDay));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 以yyyy-MM-dd HH:mm:ss格式返回String类型系统时间
	 * 
	 * @return
	 */
	public static String getNowDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date());
	}

	/**
	 * 获取 本周每天对应的日期
	 * 
	 * @return
	 */
	public static int getDYrq() {
		int weekday = DateUtils.getWeekDay();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		String y = df.format(new Date()).split("-")[1];
		String d = df.format(new Date()).split("-")[2];
		int monday;
		if (weekday == 1) {
			monday = Integer.parseInt(d);
		} else {
			monday = Integer.parseInt(d) - (weekday - 1);
		}
		return monday;
	}

	/**
	 * 获取当前系统前一天日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getNextDay() {
		// String basePath =
		// request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		Date dNow = new Date(); // 当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(dNow);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		dBefore = calendar.getTime(); // 得到前一天的时间

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		String defaultStartDate = sdf.format(dBefore); // 格式化前一天
		String defaultEndDate = sdf.format(dNow); // 格式化当前时间

		System.out.println("前一天的时间是：" + defaultStartDate);
		System.out.println("生成的时间是：" + defaultEndDate);

		return defaultStartDate.split("-")[2];
	}

	/**
	 * 一个周跨度为两个月的  调整
	 * @param monday
	 * @param weekday
	 * @param m
	 * @return
	 */
	public static String getduiying(int monday,int weekday,int m) {
		if((monday + weekday) ==1)
		{
			return m+1 + "月";
		}
		else if((monday + weekday) > 1)
		{
			return (monday +weekday)+"";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		String y = df.format(new Date()).split("-")[0];
		int year = Integer.parseInt(y);
		
		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			monthDay[1]++;

		int lastday=monthDay[m-1];
		
		
		return (lastday+monday +weekday)+"";

	}
	public static String getduiying1(int monday,int weekday,int m,int lastday) {
		if((monday + weekday) ==(lastday+1))
		{
			return m+2 + "月";
		}
		else if((monday + weekday) >(lastday+1))
		{
			return ((monday + weekday)-lastday) +"";
		}
		else
		{
			return (monday+weekday)+"";
		}
		
		

	}
}
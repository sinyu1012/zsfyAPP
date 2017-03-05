package com.fyzs.tool;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static String startDay = "2017-02-12";// ��ѧ����
	public static String endDay = "2017-7-01";// �ż�����

	/**
	 * ��ȡ��ǰʱ���ǵڼ��ڿΣ�ֻ��8-16��֮��,����ʱ��Ĭ��1��2�ڿΡ�
	 * 
	 * @return
	 */
	public static int getNowCourse() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// �������ڸ�ʽ
		String nowDate = df.format(new Date());
		if (nowDate.startsWith("08") || nowDate.startsWith("09")) {
			return 1;// 12�ڿΡ�
		} else if (nowDate.startsWith("10") || nowDate.startsWith("11")) {
			return 2;// 34�ڿΣ��Դ����ơ�
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
	 * ��ȡ��ǰʱ���ǵڼ���
	 * 
	 * @return
	 */
	public static int getWeek() {
		int days = 0;
		int nowWeek = 0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// �������ڸ�ʽ
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
			System.out.println("��������ڲ��Ϸ�����������ʧ��");
			e.printStackTrace();
		}
		return nowWeek;
	}

	/**
	 * ��ȡ��ǰʱ�������ڼ�
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
	 * ��������String��������֮�������
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
	 * ��yyyy-MM-dd HH:mm:ss��ʽ����String����ϵͳʱ��
	 * 
	 * @return
	 */
	public static String getNowDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
		return df.format(new Date());
	}

	/**
	 * ��ȡ ����ÿ���Ӧ������
	 * 
	 * @return
	 */
	public static int getDYrq() {
		int weekday = DateUtils.getWeekDay();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// �������ڸ�ʽ
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
	 * ��ȡ��ǰϵͳǰһ������
	 * 
	 * @param date
	 * @return
	 */
	public static String getNextDay() {
		// String basePath =
		// request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		Date dNow = new Date(); // ��ǰʱ��
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // �õ�����
		calendar.setTime(dNow);// �ѵ�ǰʱ�丳������
		calendar.add(Calendar.DAY_OF_MONTH, -1); // ����Ϊǰһ��
		dBefore = calendar.getTime(); // �õ�ǰһ���ʱ��

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // ����ʱ���ʽ
		String defaultStartDate = sdf.format(dBefore); // ��ʽ��ǰһ��
		String defaultEndDate = sdf.format(dNow); // ��ʽ����ǰʱ��

		System.out.println("ǰһ���ʱ���ǣ�" + defaultStartDate);
		System.out.println("���ɵ�ʱ���ǣ�" + defaultEndDate);

		return defaultStartDate.split("-")[2];
	}

	/**
	 * һ���ܿ��Ϊ�����µ�  ����
	 * @param monday
	 * @param weekday
	 * @param m
	 * @return
	 */
	public static String getduiying(int monday,int weekday,int m) {
		if((monday + weekday) ==1)
		{
			return m+1 + "��";
		}
		else if((monday + weekday) > 1)
		{
			return (monday +weekday)+"";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// �������ڸ�ʽ
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
			return m+2 + "��";
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
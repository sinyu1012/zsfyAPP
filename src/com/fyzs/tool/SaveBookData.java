package com.fyzs.tool;

import java.util.ArrayList;
import java.util.List;

public class SaveBookData {

	public static List<BookData> bd=new ArrayList<BookData>();
	public static List<BookData> bd1=new ArrayList<BookData>();
	public static void save(BookData bd1)
	{
		bd.add(bd1);
	}
	public static void save1(BookData bd2)
	{
		bd1.add(bd2);
	}
	public static void clear()
	{
		bd1.clear();
	}
	public static void clear1()
	{
		bd.clear();
	}
}

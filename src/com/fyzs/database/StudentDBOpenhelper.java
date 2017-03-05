package com.fyzs.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentDBOpenhelper extends SQLiteOpenHelper {

	public StudentDBOpenhelper(Context context) {
		super(context, "student.db", null, 2);
		System.out.println("创建2db");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("创建3DB");
		db.execSQL("create table chengji(xuenian  varchar(20),xueqi varchar(20),coursedaima varchar(20),coursename varchar(20),coursexingzhi varchar(20),courseguishu varchar(20),credit varchar(20),jidian varchar(20),achievement varchar(20),fuxiuflag varchar(20),bukao varchar(20),chongxiu varchar(20),kaikexueyuan varchar(20),beizhu varchar(20),chongxiuflag varchar(20))");
		// db.execSQL("create table info (_id integer primary key autoincrement ,name varchar(20),xuehao varchar(20))");
		db.execSQL("create table kebiao(time  varchar(20),monday varchar(20),tuesday varchar(20),wednesday varchar(20),thursday varchar(20),friday varchar(20),saturated varchar(20),sunday varchar(20))");
		//新
		db.execSQL("create table message(type  varchar(20),title varchar(100),content varchar(1000),time varchar(20))");
		
		db.execSQL("create table djknowledge(type  varchar(20),title varchar(300),content varchar(1000),answerA varchar(100),answerB varchar(100),answerC varchar(100),answerD varchar(100),answer varchar(20))");

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("创建4DB");
		if(oldVersion==1)
		{
			db.execSQL("create table message(type  varchar(20),title varchar(100),content varchar(1000),time varchar(20))");
			
			db.execSQL("create table djknowledge(type  varchar(20),title varchar(300),content varchar(1000),answerA varchar(100),answerB varchar(100),answerC varchar(100),answerD varchar(100),answer varchar(20))");

			
		}
	
	}

}

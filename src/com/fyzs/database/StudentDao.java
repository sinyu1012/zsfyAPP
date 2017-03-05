package com.fyzs.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 学生信息的dao代码，data access object
 * 
 * @author sinyu 提供增删改查
 */
public class StudentDao {
	private StudentDBOpenhelper helper;

	public StudentDao(Context context) {
		helper = new StudentDBOpenhelper(context);
		helper.getWritableDatabase();
		// System.out.println("创建1");
	}

	public long add(String xuenian, String xueqi, String coursedaima,
			String coursename, String coursexingzhi, String courseguishu,
			String credit, String jidian, String achievement, String fuxiuflag,
			String bukao, String chongxiu, String kaikexueyuan, String beizhu,
			String chongxiuflag) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("xuenian", xuenian);
		values.put("xueqi", xueqi);
		values.put("coursedaima", coursedaima);
		values.put("coursename", coursename);
		values.put("coursexingzhi", coursexingzhi);
		values.put("courseguishu", courseguishu);
		values.put("credit", credit);
		values.put("jidian", jidian);
		values.put("achievement", achievement);
		values.put("fuxiuflag", fuxiuflag);
		values.put("bukao", bukao);
		values.put("chongxiu", chongxiu);
		values.put("kaikexueyuan", kaikexueyuan);
		values.put("beizhu", beizhu);
		values.put("chongxiuflag", chongxiuflag);
		long rowid = db.insert("chengji", null, values);
		db.close();
		return rowid;
	}

	public int clearChengji() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int res = db.delete("chengji", null, null);
		return res;

	}
//
//	 public int delete(String id){ //删除 int 影响了几行
//	 SQLiteDatabase db=helper.getWritableDatabase();
//	 int result=db.delete("chengji", "coursename=?", new String[]{"军事训练"});
//	 db.close();
//	 return result;
//	
//	 }
	public List<Chengji> findAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Chengji> infos = new ArrayList<Chengji>();
		Cursor cursor = db.query("chengji", new String[] { "xuenian", "xueqi",
				"coursedaima", "coursename", "coursexingzhi", "courseguishu",
				"credit", "jidian", "achievement", "fuxiuflag", "bukao",
				"chongxiu", "kaikexueyuan", "beizhu ", "chongxiuflag" }, null,
				null, null, null, null);
		while (cursor.moveToNext()) {
			Chengji info = new Chengji();
			info.setXuenian(cursor.getString(0));
			info.setXueqi(cursor.getString(1));
			info.setCoursedaima(cursor.getString(2));
			info.setCoursename(cursor.getString(3));
			info.setCoursexingzhi(cursor.getString(4));
			info.setCourseguishu(cursor.getString(5));
			info.setCredit(cursor.getString(6));
			info.setJidian(cursor.getString(7));
			info.setAchievement(cursor.getString(8));
			info.setFuxiuflag(cursor.getString(9));
			info.setBukao(cursor.getString(10));
			info.setChongxiu(cursor.getString(11));
			info.setKaikexueyuan(cursor.getString(12));
			info.setBeizhu(cursor.getString(13));
			info.setChongxiuflag(cursor.getString(14));

			infos.add(info);
		}
		cursor.close();

		db.close();
		return infos;
	}

	public List<Chengji> findXuenian(String xuenian, String xueqi) {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Chengji> infos = new ArrayList<Chengji>();
		Cursor cursor = db.query("chengji", new String[] { "xuenian", "xueqi",
				"coursedaima", "coursename", "coursexingzhi", "courseguishu",
				"credit", "jidian", "achievement", "fuxiuflag", "bukao",
				"chongxiu", "kaikexueyuan", "beizhu ", "chongxiuflag" },
				"xuenian = ? and xueqi = ?", new String[] {xuenian, xueqi}, null, null, null);
		while (cursor.moveToNext()) {
			Chengji info = new Chengji();
			info.setXuenian(cursor.getString(0));
			info.setXueqi(cursor.getString(1));
			info.setCoursedaima(cursor.getString(2));
			info.setCoursename(cursor.getString(3));
			info.setCoursexingzhi(cursor.getString(4));
			info.setCourseguishu(cursor.getString(5));
			info.setCredit(cursor.getString(6));
			info.setJidian(cursor.getString(7));
			info.setAchievement(cursor.getString(8));
			info.setFuxiuflag(cursor.getString(9));
			info.setBukao(cursor.getString(10));
			info.setChongxiu(cursor.getString(11));
			info.setKaikexueyuan(cursor.getString(12));
			info.setBeizhu(cursor.getString(13));
			info.setChongxiuflag(cursor.getString(14));

			infos.add(info);
		}
		cursor.close();

		db.close();
		return infos;
	}
	public List<Chengji> findCoutseName(String name) {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Chengji> infos = new ArrayList<Chengji>();
		Cursor cursor = db.query("chengji", new String[] { "xuenian", "xueqi",
				"coursedaima", "coursename", "coursexingzhi", "courseguishu",
				"credit", "jidian", "achievement", "fuxiuflag", "bukao",
				"chongxiu", "kaikexueyuan", "beizhu ", "chongxiuflag" },
				"coursename = ? ", new String[] {name}, null, null, null);
		while (cursor.moveToNext()) {
			Chengji info = new Chengji();
			info.setXuenian(cursor.getString(0));
			info.setXueqi(cursor.getString(1));
			info.setCoursedaima(cursor.getString(2));
			info.setCoursename(cursor.getString(3));
			info.setCoursexingzhi(cursor.getString(4));
			info.setCourseguishu(cursor.getString(5));
			info.setCredit(cursor.getString(6));
			info.setJidian(cursor.getString(7));
			info.setAchievement(cursor.getString(8));
			info.setFuxiuflag(cursor.getString(9));
			info.setBukao(cursor.getString(10));
			info.setChongxiu(cursor.getString(11));
			info.setKaikexueyuan(cursor.getString(12));
			info.setBeizhu(cursor.getString(13));
			info.setChongxiuflag(cursor.getString(14));

			infos.add(info);
		}
		cursor.close();

		db.close();
		return infos;
	}
	public long addKebiao(String time, String monday, String tuesday,
			String wednesday, String thursday, String friday, String saturated,
			String sunday) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("time", time);
		values.put("monday", monday);
		values.put("tuesday", tuesday);
		values.put("wednesday", wednesday);
		values.put("thursday", thursday);
		values.put("friday", friday);
		values.put("saturated", saturated);
		values.put("sunday", sunday);
		long rowid = db.insert("kebiao", null, values);
		db.close();
		return rowid;
	}

	public List<Kebiao> findAllKebiao() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Kebiao> infos = new ArrayList<Kebiao>();
		Cursor cursor = db.query("kebiao", new String[] { "time", "monday",
				"tuesday", "wednesday", "thursday", "friday", "saturated",
				"sunday" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Kebiao info = new Kebiao();
			info.setTime(cursor.getString(0));
			info.setMonday(cursor.getString(1));
			info.setTuesday(cursor.getString(2));
			info.setWednesday(cursor.getString(3));
			info.setThursday(cursor.getString(4));
			info.setFriday(cursor.getString(5));
			info.setSaturated(cursor.getString(6));
			info.setSunday(cursor.getString(7));
			infos.add(info);
		}
		cursor.close();

		db.close();
		return infos;
	}

	public int clearKebiao() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int res = db.delete("Kebiao", null, null);
		return res;
	}
	
	public long addMsg(String type,String title,String content,String time)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", type);
		values.put("title", title);
		values.put("content", content);
		values.put("time", time);
		long rowid = db.insert("message", null, values);
		db.close();
		return rowid;
	}
	public List<Message> findMsg() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Message> infos = new ArrayList<Message>();
		Cursor cursor = db.query("message", new String[] { "type", "title",
				"content", "time"}, null,
				null, null, null, "time desc");
		
		//System.out.println("??????????");
		while (cursor.moveToNext()) {
			System.out.println(cursor.getString(0));
			Message info = new Message();
			info.setType(cursor.getString(0));
			info.setTitle(cursor.getString(1));
			info.setContent(cursor.getString(2));
			info.setTime(cursor.getString(3));

			infos.add(info);
		}
		cursor.close();

		db.close();
		return infos;
	}
	public int clearMSG() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int res = db.delete("message", null, null);
		return res;
	}
	public long addDJ(String type,String title,String content,String answerA,String answerB,String answerC,String answerD,String answer)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", type);
		values.put("title", title);
		values.put("content", content);
		values.put("answerA", answerA);
		values.put("answerB", answerB);
		values.put("answerC", answerC);
		values.put("answerD", answerD);
		values.put("answer", answer);
		long rowid = db.insert("djknowledge", null, values);
		db.close();
		return rowid;
	}
	public List<DJKnowledgeData> findDJK() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<DJKnowledgeData> infos = new ArrayList<DJKnowledgeData>();
		Cursor cursor = db.query("djknowledge", new String[] { "type", "title",
				"content", "answerA", "answerB", "answerC", "answerD", "answer"}, null,
				null, null, null, null);
		
		System.out.println("??????????");
		while (cursor.moveToNext()) {
			System.out.println(cursor.getString(0));
			DJKnowledgeData info = new DJKnowledgeData();
			info.setType(cursor.getString(0));
			info.setTitle(cursor.getString(1));
			info.setContent(cursor.getString(2));
			info.setAnswerA(cursor.getString(3));
			info.setAnswerB(cursor.getString(4));
			info.setAnswerC(cursor.getString(5));
			info.setAnswerD(cursor.getString(6));
			info.setAnswer(cursor.getString(7));

			infos.add(info);
		}
		cursor.close();

		db.close();
		return infos;
	}
	public int clearDJK() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int res = db.delete("djknowledge", null, null);
		return res;
	}
}

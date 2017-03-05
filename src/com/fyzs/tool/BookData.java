package com.fyzs.tool;

public class BookData {

	String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameinfo() {
		return nameinfo;
	}
	public void setNameinfo(String nameinfo) {
		this.nameinfo = nameinfo;
	}
	public String getGuancanginfo() {
		return guancanginfo;
	}
	public void setGuancanginfo(String guancanginfo) {
		this.guancanginfo = guancanginfo;
	}
	public String getSuoshuno() {
		return suoshuno;
	}
	public void setSuoshuno(String suoshuno) {
		this.suoshuno = suoshuno;
	}
	String nameinfo;
	String guancanginfo;
	String suoshuno;
	String page;
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	static String[] hrefArr = new String[20];
	
	String tv_libdetail_name;
	public static String[] getHrefArr() {
		return hrefArr;
	}
	public static void setHrefArr(String[] hrefArr) {
		BookData.hrefArr = hrefArr;
	}
	public String getTv_libdetail_name() {
		return tv_libdetail_name;
	}
	public void setTv_libdetail_name(String tv_libdetail_name) {
		this.tv_libdetail_name = tv_libdetail_name;
	}
	public String getTv_libdetail_zuozhename() {
		return tv_libdetail_zuozhename;
	}
	public void setTv_libdetail_zuozhename(String tv_libdetail_zuozhename) {
		this.tv_libdetail_zuozhename = tv_libdetail_zuozhename;
	}
	
	String tv_libdetail_zuozhename;
	String tv_libdetail_detail;
	public String getTv_libdetail_detail() {
		return tv_libdetail_detail;
	}
	public void setTv_libdetail_detail(String tv_libdetail_detail) {
		this.tv_libdetail_detail = tv_libdetail_detail;
	}
	String tv_libdetail_jieshuinfo;
	public String getTv_libdetail_jieshuinfo() {
		return tv_libdetail_jieshuinfo;
	}
	public void setTv_libdetail_jieshuinfo(String tv_libdetail_jieshuinfo) {
		this.tv_libdetail_jieshuinfo = tv_libdetail_jieshuinfo;
	}
	
	
}

package com.fyzs.activity;

import java.util.List;

import org.w3c.dom.Text;


import com.fyzs.tool.BookData;
import com.fyzs.tool.SaveBookData;
import com.czfy.zsfy.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailBookActivity extends Activity {

	private TextView tv_libdetail_detail;
	private ImageView bt_top_return;
	private TextView tv_libdetail_name;
	private TextView tv_libdetail_zuozhename;
	private TextView tv_libdetail_suoshuhao1;
	private TextView tv_libdetail_suoshuhao2;
	private TextView tv_libdetail_suoshuhao3;
	private TextView tv_libdetail_didian1;
	private TextView tv_libdetail_didian2;
	private TextView tv_libdetail_didian3;
	private TextView tv_libdetail_zhuangtai1;
	private TextView tv_libdetail_zhuangtai2;
	private TextView tv_libdetail_zhuangtai3;
	private TextView tv_libdetail_suoshuhao4;
	private TextView tv_libdetail_suoshuhao5;
	private TextView tv_libdetail_suoshuhao6;
	private TextView tv_libdetail_didian4;
	private TextView tv_libdetail_didian5;
	private TextView tv_libdetail_didian6;
	private TextView tv_libdetail_zhuangtai4;
	private TextView tv_libdetail_zhuangtai5;
	private TextView tv_libdetail_zhuangtai6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_lib_detail);
		List<BookData> s = SaveBookData.bd1;
		BookData bd = s.get(0);
		tv_libdetail_suoshuhao1 = (TextView) findViewById(R.id.tv_libdetail_suoshuhao1);
		tv_libdetail_suoshuhao2 = (TextView) findViewById(R.id.tv_libdetail_suoshuhao2);
		tv_libdetail_suoshuhao3 = (TextView) findViewById(R.id.tv_libdetail_suoshuhao3);
		tv_libdetail_didian1 = (TextView) findViewById(R.id.tv_libdetail_didian1);
		tv_libdetail_didian2 = (TextView) findViewById(R.id.tv_libdetail_didian2);
		tv_libdetail_didian3 = (TextView) findViewById(R.id.tv_libdetail_didian3);
		tv_libdetail_zhuangtai1 = (TextView) findViewById(R.id.tv_libdetail_zhuangtai1);
		tv_libdetail_zhuangtai2 = (TextView) findViewById(R.id.tv_libdetail_zhuangtai2);
		tv_libdetail_zhuangtai3 = (TextView) findViewById(R.id.tv_libdetail_zhuangtai3);

		tv_libdetail_suoshuhao4 = (TextView) findViewById(R.id.tv_libdetail_suoshuhao4);
		tv_libdetail_suoshuhao5 = (TextView) findViewById(R.id.tv_libdetail_suoshuhao5);
		tv_libdetail_suoshuhao6 = (TextView) findViewById(R.id.tv_libdetail_suoshuhao6);
		tv_libdetail_didian4 = (TextView) findViewById(R.id.tv_libdetail_didian4);
		tv_libdetail_didian5 = (TextView) findViewById(R.id.tv_libdetail_didian5);
		tv_libdetail_didian6 = (TextView) findViewById(R.id.tv_libdetail_didian6);
		tv_libdetail_zhuangtai4 = (TextView) findViewById(R.id.tv_libdetail_zhuangtai4);
		tv_libdetail_zhuangtai5 = (TextView) findViewById(R.id.tv_libdetail_zhuangtai5);
		tv_libdetail_zhuangtai6 = (TextView) findViewById(R.id.tv_libdetail_zhuangtai6);

		tv_libdetail_name = (TextView) findViewById(R.id.tv_libdetail_name);
		tv_libdetail_zuozhename = (TextView) findViewById(R.id.tv_libdetail_zuozhename);
		tv_libdetail_detail = (TextView) findViewById(R.id.tv_libdetail_detail);
		tv_libdetail_zuozhename.setText("作者："
				+ bd.getTv_libdetail_zuozhename().split("/")[bd
						.getTv_libdetail_zuozhename().split("/").length - 1]);

		String[] jieshuinfo = bd.getTv_libdetail_jieshuinfo().split("#");
		tv_libdetail_name
				.setText(bd.getTv_libdetail_zuozhename().split("/")[1]
						.split(":")[1]);
		System.out.println(jieshuinfo.length + " "
				+ bd.getTv_libdetail_jieshuinfo());
		if (!(jieshuinfo[0].split(" ")[0].charAt(0) == '此')) {
			tv_libdetail_detail.setText(bd.getTv_libdetail_detail());
			if (jieshuinfo.length >= 1) {
				tv_libdetail_suoshuhao1.setText(jieshuinfo[0].split(" ")[0]);
				tv_libdetail_didian1.setText(jieshuinfo[0].split(" ")[3]);
				if (jieshuinfo[0].split(" ")[5].equals("可借")) {
					tv_libdetail_zhuangtai1.setTextColor(Color.GREEN);
				}
				tv_libdetail_zhuangtai1.setText(jieshuinfo[0].split(" ")[5]);
			}
			if (jieshuinfo.length >= 2) {
				tv_libdetail_suoshuhao2.setText(jieshuinfo[1].split(" ")[0]);
				tv_libdetail_didian2.setText(jieshuinfo[1].split(" ")[3]);
				if (jieshuinfo[1].split(" ")[5].equals("可借")) {
					tv_libdetail_zhuangtai2.setTextColor(Color.GREEN);
				}
				tv_libdetail_zhuangtai2.setText(jieshuinfo[1].split(" ")[5]);
			}
			if (jieshuinfo.length >= 3) {// 3个

				tv_libdetail_suoshuhao3.setText(jieshuinfo[2].split(" ")[0]);
				tv_libdetail_didian3.setText(jieshuinfo[2].split(" ")[3]);
				if (jieshuinfo[2].split(" ")[5].equals("可借")) {
					tv_libdetail_zhuangtai3.setTextColor(Color.GREEN);
				}
				tv_libdetail_zhuangtai3.setText(jieshuinfo[2].split(" ")[5]);
			}
			if (jieshuinfo.length >= 4) {
				tv_libdetail_suoshuhao4.setText(jieshuinfo[3].split(" ")[0]);
				tv_libdetail_didian4.setText(jieshuinfo[3].split(" ")[3]);
				if (jieshuinfo[3].split(" ")[5].equals("可借")) {
					tv_libdetail_zhuangtai4.setTextColor(Color.GREEN);
				}
				tv_libdetail_zhuangtai4.setText(jieshuinfo[3].split(" ")[5]);
			}
			if (jieshuinfo.length >= 5) {
				tv_libdetail_suoshuhao5.setText(jieshuinfo[4].split(" ")[0]);
				tv_libdetail_didian5.setText(jieshuinfo[4].split(" ")[3]);
				if (jieshuinfo[4].split(" ")[5].equals("可借")) {
					tv_libdetail_zhuangtai5.setTextColor(Color.GREEN);
				}
				tv_libdetail_zhuangtai5.setText(jieshuinfo[4].split(" ")[5]);
			}
			if (jieshuinfo.length >= 6) {
				tv_libdetail_suoshuhao6.setText(jieshuinfo[5].split(" ")[0]);
				tv_libdetail_didian6.setText(jieshuinfo[5].split(" ")[3]);
				if (jieshuinfo[5].split(" ")[5].equals("可借")) {
					tv_libdetail_zhuangtai6.setTextColor(Color.GREEN);
				}
				tv_libdetail_zhuangtai6.setText(jieshuinfo[5].split(" ")[5]);
			}
		} else
			tv_libdetail_detail.setText(bd.getTv_libdetail_detail() + " \n"
					+ jieshuinfo[0].split(" ")[0] + " \n");
		// .setText("出版发行项: 北京:清华大学出版社,2014 \nISBN及定价: 978-7-302-34732-3/CNY48.00 (含光盘) \nISBN及定价: 978-7-89414-733-2 光盘 \n载体形态项: 330页:图;26cm+光盘1片 \n丛编项: 软件入门与提高丛书 \n个人责任者: 相世强 编著 \n个人责任者: 李绍勇 编著 \n学科主题: 表处理软件-应用-财务管理 \n中图法分类号: F275-39 \n一般附注: 经典清华版 \n提要文摘附注: 本书根据各种财务信息和数据管理工作中的需求，以零起点全面介绍Excel在财务管理领域中的应用，并以通俗的讲解和详细的步骤说明。");
		bt_top_return = (ImageView) findViewById(R.id.bt_top_return);
		bt_top_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DetailBookActivity.this.finish();
			}
		});
	}
}

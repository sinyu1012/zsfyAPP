package com.fyzs.activity;

import java.util.List;

import com.czfy.zsfy.R;
import com.fyzs.database.StudentDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgDetailActivity extends Activity {

	private TextView tv_top_text;
	private ImageView bt_top_return;
	private TextView tv_title;
	private TextView tv_type;
	private TextView tv_time;
	private TextView tv_content;
	StudentDao dao;
	List<com.fyzs.database.Message> infos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.msgdetail);
	
		dao=new StudentDao(this);
		infos=dao.findMsg();
		
		tv_top_text=(TextView) findViewById(R.id.tv_top_lib);
		tv_top_text.setText("消息详情");
		bt_top_return = (ImageView) findViewById(R.id.bt_top_return);
		bt_top_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
		tv_title=(TextView) findViewById(R.id.tv_msg_title);
		tv_type=(TextView) findViewById(R.id.tv_msg_who);
		tv_time=(TextView) findViewById(R.id.tv_msg_time);
		tv_content=(TextView) findViewById(R.id.tv_msg_content);
		
		Intent intent=getIntent();
		int index=intent.getIntExtra("msg_index", 0);
		com.fyzs.database.Message msg=infos.get(index);
		
		tv_title.setText(msg.getTitle());
		tv_content.setText("	\n"+msg.getContent());
		tv_type.setText("来自："+msg.getType());
		tv_time.setText("时间："+msg.getTime());
	}
	
}

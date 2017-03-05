package com.fyzs.activity;

import java.util.List;

import com.czfy.zsfy.R;
import com.fyzs.database.DJKnowledgeData;
import com.fyzs.database.Message;
import com.fyzs.database.StudentDao;
import com.fyzs.tool.SaveBookData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DangjiActivity extends Activity {

	private TextView tv_top_text;
	private ImageView bt_top_return;
	private ListView lv_dj;
	StudentDao dao;
	List<DJKnowledgeData> infos;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_dangji);
		
		dao=new StudentDao(this);
		infos=dao.findDJK();
		tv_top_text=(TextView) findViewById(R.id.tv_top_lib);
		tv_top_text.setText("党基学习");
		bt_top_return = (ImageView) findViewById(R.id.bt_top_return);
		bt_top_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
		lv_dj=(ListView) findViewById(R.id.lv_dj);
		lv_dj.setAdapter(new myAdapter());
		lv_dj.setOnItemClickListener(new OnItemClickListener() {// 点击事件
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				Intent intent=new Intent(DangjiActivity.this,DJDatiActivity.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}

		});
		
	}
	
	private class myAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View view = View.inflate(DangjiActivity.this, R.layout.list_dangji, null);
			DJKnowledgeData info=infos.get(position);
			TextView title = (TextView) view.findViewById(R.id.tv_dj_title);
			if(info.getTitle().length()>50)
				title.setText(position+1+".【单选】"+info.getTitle().substring(0, 50)+"...");
			else
				title.setText(position+1+".【单选】"+info.getTitle());
			return view;
		}

	}
}

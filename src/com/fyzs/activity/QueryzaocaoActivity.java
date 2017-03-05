package com.fyzs.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.czfy.zsfy.R;
import com.fyzs.Http.QueryzaocaoHttp;
import com.fyzs.fragment.LibraryFragment;
import com.fyzs.tool.SaveBookData;
import com.fyzs.tool.SearchBook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QueryzaocaoActivity extends Activity {

	private ImageView back;
	private TextView tv_top,tv_zaocao_name,tv_zaocao_class,tv_zaocao_cishu;
	private EditText ed_search;
	private ProgressDialog pd;
	private ImageView bt_zaocao_query;

	private LinearLayout lin_query;
	String res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_queryzaocao);

		back = (ImageView) findViewById(R.id.bt_top_return);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();// ����
			}
		});
		tv_top = (TextView) findViewById(R.id.tv_top_lib);
		tv_top.setText("��ٲ�ѯ");
		lin_query=(LinearLayout) findViewById(R.id.lin_query);
		tv_zaocao_name=(TextView) findViewById(R.id.tv_zaocao_name);
		tv_zaocao_class=(TextView) findViewById(R.id.tv_zaocao_class);
		tv_zaocao_cishu=(TextView) findViewById(R.id.tv_zaocao_cishu);
		ed_search = (EditText) findViewById(R.id.ed_lib_search);
		SharedPreferences sp = this.getSharedPreferences("StuData", 0);
		ed_search.setText(sp.getString("xh", ""));
		
		bt_zaocao_query = (ImageView) findViewById(R.id.bt_zaocao_query);
		
		bt_zaocao_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final String str = ed_search.getText().toString().trim();
				if (str.isEmpty()) {
					Toast.makeText(QueryzaocaoActivity.this, "������ѧ��", 0).show();
					return;
				}

				/* ��ʾProgressDialog */
				pd = ProgressDialog.show(QueryzaocaoActivity.this, "",
						"��ѯ�У����Ժ󡭡�");

				new Thread() {
					public void run() {
						QueryzaocaoHttp q = new QueryzaocaoHttp();

						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd");
						Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
						String time = formatter.format(curDate);
						System.out.println(time);
						res = q.Query(str, com.fyzs.tool.DateUtils.startDay, time);// ��ʱ�ķ���
						if (res == null) {
							pd.dismiss();// �ر�ProgressDialog
							// showToastInAnyThread("��������ʱ");
							handler.sendEmptyMessage(2);// ���̸߳���ui toast
						} else
							handler.sendEmptyMessage(0);// ִ�к�ʱ�ķ���֮��������handler
					};
				}.start();

			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���
			if (msg.what == 0) {
				pd.dismiss();// �ر�ProgressDialog
				try {
					JSONObject json = new JSONObject(res);
					if (json.getString("name").equals("0")) {
						Toast.makeText(QueryzaocaoActivity.this, "���޲�ѯ�������Ϣ��",
								1).show();
					} else if (json.getString("name").equals("1")) {
						Toast.makeText(QueryzaocaoActivity.this, "��������ʱ", 0)
								.show();
					} else {//����Ϣ
						tv_zaocao_name.setText(json.getString("name"));
						tv_zaocao_class.setText(json.getString("class"));
						tv_zaocao_cishu.setText(json.getString("cishu"));
						lin_query.setVisibility(View.VISIBLE);
						//Toast.makeText(QueryzaocaoActivity.this, res, 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(QueryzaocaoActivity.this, "��������ʱ", 0).show();
			}
		}
	};

}

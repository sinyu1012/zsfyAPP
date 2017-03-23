package com.fyzs.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.czfy.zsfy.R;
import com.fyzs.Http.HttpPostConn;
import com.fyzs.Http.QueryzaocaoHttp;
import com.fyzs.fragment.LibraryFragment;
import com.fyzs.tool.SaveBookData;
import com.fyzs.tool.SearchBook;
import com.fyzs.tool.ZaocaoInfo;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QueryzaocaoActivity extends Activity {
	private ListView lv_detail;
	private ImageView back;
	private List<ZaocaoInfo> infos;
	private TextView tv_top;
	private EditText ed_search;
	private ProgressDialog pd;
	private ImageView bt_zaocao_query;
	private TextView tv_name1,tv_name2,tv_name3;
	Myadapter myadapter;
	String res;
	RelativeLayout bannerContainer;
	BannerView bv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_queryzaocao);
		tv_name1= (TextView) findViewById(R.id.tv_name1);
		tv_name2= (TextView) findViewById(R.id.tv_name2);
		tv_name3= (TextView) findViewById(R.id.tv_name3);
		lv_detail= (ListView) findViewById(R.id.lv_detail);
		ed_search= (EditText) findViewById(R.id.ed_lib_search);
		infos=new ArrayList<>();
		myadapter=new Myadapter();
		lv_detail.setAdapter(myadapter);
		bannerContainer = (RelativeLayout) this.findViewById(R.id.bannerContainer);
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
		//lin_query=(LinearLayout) findViewById(R.id.lin_query);

		SharedPreferences sp = this.getSharedPreferences("StuData", 0);
		ed_search.setText(sp.getString("xh", ""));
		//ed_search.clearFocus();
		initBanner();
		this.bv.loadAD();
		bt_zaocao_query = (ImageView) findViewById(R.id.bt_zaocao_query);

		bt_zaocao_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				infos.clear();
				final String str = ed_search.getText().toString().trim();
				if (str.isEmpty()) {
					Toast.makeText(QueryzaocaoActivity.this, "������ѧ��", Toast.LENGTH_SHORT).show();
					return;
				}
				tv_name1.setText("����");
				tv_name2.setText("�༶");
				tv_name3.setText("����");
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
	public void detail(View v){
		infos.clear();
		final String str = ed_search.getText().toString().trim();
		if (str.isEmpty()) {
			Toast.makeText(QueryzaocaoActivity.this, "������ѧ��", Toast.LENGTH_LONG).show();
			return;
		}
		tv_name1.setText("���");
		tv_name2.setText("����");
		tv_name3.setText("ʱ��");
				/* ��ʾProgressDialog */
		pd = ProgressDialog.show(QueryzaocaoActivity.this, "",
				"��ѯ�У����Ժ󡭡�");

		new Thread() {
			public void run() {
				HttpPostConn q = new HttpPostConn();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyyMMdd");
				Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
				String time = formatter.format(curDate);
				System.out.println(time);
				String stime=com.fyzs.tool.DateUtils.startDay.split("-")[0]+com.fyzs.tool.DateUtils.startDay.split("-")[1]+com.fyzs.tool.DateUtils.startDay.split("-")[2];
				String data="xh="+str+"&stime="+stime.substring(0,8)+"&etime="+time;
				System.out.println(data);
				res = q.doPOST("http://202.119.168.66:8080/test/QueryzaocaoDetailServlet",data);// ��ʱ�ķ���
				System.out.println(res);
				if (res == null) {
					pd.dismiss();// �ر�ProgressDialog
					// showToastInAnyThread("��������ʱ");
					handler.sendEmptyMessage(2);// ���̸߳���ui toast
				} else
					handler.sendEmptyMessage(1);// ִ�к�ʱ�ķ���֮��������handler
			};
		}.start();
	}
	private void initBanner() {//���
		this.bv=new BannerView(this, ADSize.BANNER,"1105409129","9060422047880836");
		// ע�⣺��������ߵ�banner����ʼ��չʾ����Ļ�еĻ�����ر��Զ�ˢ�£����򽫵����ع��ʹ��͡�
		// ����Ӧ�����д�����banner��������������Ļ�����ֶ�loadAD��
		bv.setRefresh(8);
		bv.setADListener(new AbstractBannerADListener() {
			@Override
			public void onNoAD(int i) {
				Log.i("AD_DEMO", "BannerNoAD��eCode=" + i);
			}

			@Override
			public void onADReceiv() {
				Log.i("AD_DEMO", "ONBannerReceive");
			}
		});
	//	bv.loadAD();
		 /* �����������յ�������ݺ��չʾ����   */
		bannerContainer.addView(bv);
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���
			if (msg.what == 0) {//�򿨴���
				pd.dismiss();// �ر�ProgressDialog
				try {
					JSONObject json = new JSONObject(res);
					if (json.getString("name").equals("0")) {
						Toast.makeText(QueryzaocaoActivity.this, "���޲�ѯ�������Ϣ��",
								Toast.LENGTH_SHORT).show();
					} else if (json.getString("name").equals("1")) {
						Toast.makeText(QueryzaocaoActivity.this, "��������ʱ", 0)
								.show();
					} else {//����Ϣ
						ZaocaoInfo info=new ZaocaoInfo();
						info.setXuhao(json.getString("name"));
						info.setName(json.getString("class"));
						info.setTime(json.getString("cishu"));
						infos.add(info);
						myadapter.notifyDataSetChanged();
						//lin_query.setVisibility(View.VISIBLE);
						//Toast.makeText(QueryzaocaoActivity.this, res, 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else if(msg.what==1){//����ϸ
				pd.dismiss();// �ر�ProgressDialog
				try {
					JSONArray jarr = new JSONArray(res);
					for (int i=0;i<jarr.length();i++){
						JSONObject jsonObject=jarr.getJSONObject(i);
						if (jsonObject.getString("name").equals("0")) {
							Toast.makeText(QueryzaocaoActivity.this, "���޲�ѯ�������Ϣ��",
									1).show();
						} else if (jsonObject.getString("name").equals("1")) {
							Toast.makeText(QueryzaocaoActivity.this, "��������ʱ", 0)
									.show();
						} else {//����Ϣ
							ZaocaoInfo info=new ZaocaoInfo();
							info.setXuhao(jsonObject.getString("xuhao"));
							info.setName(jsonObject.getString("name"));
							info.setTime(jsonObject.getString("time"));
							infos.add(info);
							//lin_query.setVisibility(View.VISIBLE);
							//Toast.makeText(QueryzaocaoActivity.this, res, 0).show();
						}
					}
					myadapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

				Toast.makeText(QueryzaocaoActivity.this, "��������ʱ", 0).show();
			}
		}
	};
	class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			View view1=View.inflate(QueryzaocaoActivity.this,R.layout.list_daka,null);
			ZaocaoInfo info=infos.get(i);
			TextView tv1=(TextView)view1.findViewById(R.id.tv_zaocao_name);
			tv1.setText(info.getXuhao());
			TextView tv2=(TextView)view1.findViewById(R.id.tv_zaocao_class);
			tv2.setText(info.getName());
			TextView tv3=(TextView)view1.findViewById(R.id.tv_zaocao_cishu);
			tv3.setText(info.getTime());
			return view1;
		}
	}
}

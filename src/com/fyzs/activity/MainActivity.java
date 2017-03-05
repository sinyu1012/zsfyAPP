package com.fyzs.activity;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.czfy.zsfy.R;
import com.fyzs.Http.MessageHttp;
import com.fyzs.database.StudentDao;
import com.fyzs.fragment.ChengjiFragment;
import com.fyzs.fragment.HomeFragment;
import com.fyzs.fragment.KebiaoFragment;
import com.fyzs.fragment.LeftFragment;
import com.fyzs.fragment.PerInfoFragment;
import com.fyzs.fragment.PerInfoFragment.CheckVersionTask;
import com.fyzs.tool.DownLoadManager;
import com.fyzs.tool.MyAPP;
import com.fyzs.tool.UpdataInfo;
import com.fyzs.tool.UpdataInfoParser;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @date 2016/6/14
 * @author sinyu
 * @description
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	private final String TAG = this.getClass().getName();
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	private Button getVersion;
	private LinearLayout tx_feedback;
	private UpdataInfo info;

	StudentDao dao;
	private String localVersion;
	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	private ImageView msgButton;
	SharedPreferences setting;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标�??
		super.onCreate(savedInstanceState);

		setting = getSharedPreferences("SHARE_APP_TAG", 0);
		Boolean user_first = setting.getBoolean("FIRST", true);

		setContentView(R.layout.activity_main);
		initSlidingMenu(savedInstanceState);
		Boolean user_msg = setting.getBoolean("user_Msg", true);


		dao=new StudentDao(this);
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		msgButton = (ImageView) findViewById(R.id.msgButton);
		msgButton.setOnClickListener(this);
		if(!user_msg)
		{
			//Toast.makeText(this, ""+user_msg, 0).show();
			System.out.println(user_msg);
			msgButton.setImageResource(R.drawable.newmessage);
		}
		else
		{
			//Toast.makeText(this, ""+user_msg, 0).show();
			msgButton.setImageResource(R.drawable.message);
		}

		jianchaMsg();
		jianchagengxin();
	}
	public  void showdia()
	{
		String str="  ͬѧ����ã�\n   Ϊ�˲��ٳ��ַ��ܵ��µĲ���α������ʾ�������Ʒ����С�" +
				"���迪ͨ���Ⱥ˶��Լ����˿α�������ͨ��֧����ת�˹��ܣ�ת1ԪǮ�ڲ�ѵ�֧�����˺ţ�1341156974@qq.com������ע�Լ���ѧ�ţ�ת�˺���ȴ�һЩʱ�䡣�������ޡ�";
		com.fyzs.tool.CustomDialog.Builder builder = new com.fyzs.tool.CustomDialog.Builder(
				this);
		builder.setMessage(str);
		builder.setTitle("�����ڲ⹫��");
		builder.setPositiveButton("ȷ��������",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int which) {
						dialog.dismiss();
						ClipboardManager cmb = (ClipboardManager) MainActivity.this .getSystemService(Context.CLIPBOARD_SERVICE);
						//cmb.setText("");
						cmb.setText("1341156974@qq.com");
						Toast.makeText(MainActivity.this  , "�����˺ųɹ�", Toast.LENGTH_LONG).show();
						// ������Ĳ�������
					}
				});
		builder.create().show();
	}
	// �����Ϣ����
	public void jianchaMsg() {

		new Thread(){
			@Override
			public void run() {

				// TODO Auto-generated method stub
				String result=MessageHttp.Message();
				if(!result.equals("0"))
				{
					try {
						JSONArray jsarr=new JSONArray(result);
						List<com.fyzs.database.Message> infos=dao.findMsg();
						System.out.println(jsarr.length()+"----------"+infos.size());
						if(jsarr.length()!=infos.size())//�и���
						{
							dao.clearMSG();
							Message msg = new Message();
							msg.what = 5;
							handler.sendMessage(msg);
							for(int i=0;i<jsarr.length();i++)
							{
								JSONObject json=jsarr.getJSONObject(i);
								String type=json.getString("type");
								String title=json.getString("title");
								String content=json.getString("content");
								String time=json.getString("time");
								dao.addMsg(type.trim(), title.trim(), content.trim(), time.trim());
							}
							JSONObject json=jsarr.getJSONObject(jsarr.length()-1);
							Intent intent=new Intent(MainActivity.this,MessageActivity.class);
							PendingIntent pi= PendingIntent.getActivity(MainActivity.this,0,intent,0);
							NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							Notification notification=new NotificationCompat.Builder(MainActivity.this)
									.setContentTitle("���µ���Ϣ")
									.setContentText(json.getString("title"))
									.setWhen(System.currentTimeMillis())
									.setSmallIcon(R.drawable.small)
									.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.czfy))
									.setContentIntent(pi)
									.setAutoCancel(true)
									.setPriority(NotificationCompat.PRIORITY_MAX)
									.build();
							manager.notify(1,notification);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					//û������
				}
			}
		}.start();
	}

	public void jianchagengxin() {
		try {
			localVersion = getVersionName();
			CheckVersionTask cv = new CheckVersionTask();
			new Thread(cv).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class CheckVersionTask implements Runnable {
		InputStream is;

		public void run() {
			try {
				String path = getResources().getString(R.string.url_server);
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				int responseCode = conn.getResponseCode();
				if (responseCode == 200) {
					// �ӷ��������һ��������
					is = conn.getInputStream();
				}
				info = UpdataInfoParser.getUpdataInfo(is);
				if (info.getVersion().equals(localVersion)) {
					Log.i(TAG, "�汾����ͬ");
					Message msg = new Message();
					msg.what = UPDATA_NONEED;
					handler.sendMessage(msg);
					// LoginMain();
				} else {
					Log.i(TAG, "�汾�Ų���ͬ ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}

	/*
	 * ��ȡ��ǰ����İ汾��
	 */
	private String getVersionName() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}

	/**
	 * 初始化侧边�??
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {

		// if (savedInstanceState != null) {
		// mContent = getSupportFragmentManager().getFragment(
		// savedInstanceState, "mContent");
		// }

		if (mContent == null) {
			mContent = new HomeFragment();// �����ҳ��fragment
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();
		getSlidingMenu().showContent();

		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		SlidingMenu sm = getSlidingMenu();

		sm.setMode(SlidingMenu.LEFT);

		sm.setShadowWidthRes(R.dimen.shadow_width);

		sm.setShadowDrawable(null);

		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		sm.setFadeDegree(0.35f);

		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		sm.setBehindScrollScale(0.0f);

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case UPDATA_NONEED:
					// Toast.makeText(MainActivity.this, "����Ӧ��Ϊ���°汾",
					// Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessage(8);
					break;
				case UPDATA_CLIENT:
					// �Ի���֪ͨ�û���������
					handler.sendEmptyMessage(8);
					showUpdataDialog();
					break;
				case GET_UNDATAINFO_ERROR:
					// ��������ʱ
					handler.sendEmptyMessage(8);
					// Toast.makeText(MainActivity.this, "��ȡ������������Ϣʧ��", 1).show();
					break;
				case DOWN_ERROR:
					// ����apkʧ��
					handler.sendEmptyMessage(8);
					Toast.makeText(MainActivity.this, "�����°汾ʧ��", 1).show();
					break;
				case 5:

					Editor et = setting.edit();
					// System.out.println("���档������");
					et.putBoolean("user_Msg", false);
					et.commit();
					msgButton.setImageResource(R.drawable.newmessage);
					break;
			}
		}
	};

	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(MainActivity.this);
		builer.setTitle("�汾����");
		builer.setMessage("��鵽�µİ汾����������������˸��๦�ܣ��Ƿ�������");
		// ����ȷ����ťʱ�ӷ����������� �µ�apk Ȼ��װ ?
		builer.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "����apk,����");
				downLoadApk();
			}
		});
		builer.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// do sth
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	protected void downLoadApk() {
		final ProgressDialog pd; // �������Ի���
		pd = new ProgressDialog(MainActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("�������ظ���(��λ��KB)");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(
							info.getUrl(), pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); // �������������Ի���
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	// ��װapk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// ִ�ж���
		intent.setAction(Intent.ACTION_VIEW);
		// ִ�е���������
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	/**
	 * 切�?? Fragment
	 *
	 * @param fragment
	 */

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void switchConent(Fragment fragment, String title) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
		topTextView.setText(title);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.topButton:
				toggle();
				break;
			case R.id.msgButton:
				Editor et = setting.edit();
				// System.out.println("���档������");
				et.putBoolean("user_Msg", true);
				et.commit();
				msgButton.setImageResource(R.drawable.message);
				Intent intent = new Intent(this, MessageActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}

}

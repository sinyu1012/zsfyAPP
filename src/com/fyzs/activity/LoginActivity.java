package com.fyzs.activity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.czfy.zsfy.R;
import com.czfy.zsfy.YzmActivity;
import com.fyzs.Http.CookieHttp;
import com.fyzs.Http.FZVipHttp;
import com.fyzs.Http.JwxtHttp;
import com.fyzs.Http.SetUser;
import com.fyzs.Http.TeacherLogin;
import com.fyzs.database.StudentDao;
import com.fyzs.fragment.ChengjiFragment;
import com.fyzs.tool.BookData;
import com.fyzs.tool.MyConstants;
import com.fyzs.tool.SearchBook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoginActivity extends Activity {
	private EditText et_xh;
	private EditText et_pwd;
	// private Spinner sp;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private ImageView eye,delpass;
	private int e = 0;
	private ProgressDialog pd;
	private StudentDao dao;
	private TextView tv_login_about;
	private SharedPreferences shp;
	public static LoginActivity _instance = null;
	private RadioGroup rg;
	private String loginType="学生";
	private static final String TAG = "LoginActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 判断studata里有没有name值
		// setContentView(R.layout.layout_login);
		// SharedPreferences setting = getSharedPreferences("SHARE_APP_TAG", 0);
		// Boolean user_first = setting.getBoolean("FIRST", true);
		// if (user_first) {// 第一次
		// setting.edit().putBoolean("FIRST", false).commit();
		// } else {
		// // 不是第一次
		// finish();
		// Intent intent=new Intent(LoginActivity.this,MainActivity.class);
		// startActivity(intent);
		//
		//
		// }
		setContentView(R.layout.layout_login);
		shp = getSharedPreferences(MyConstants.FIRST, MODE_PRIVATE);

		if (shp.getBoolean(MyConstants.FIRST, false)) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		} else {

		}
		_instance=this;
		dao = new StudentDao(this);
		et_xh = (EditText) findViewById(R.id.et_xh);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		rg = (RadioGroup) findViewById(R.id.rg_logintype);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_bm:
					loginType="部门";
					break;
				case R.id.rb_js:
					loginType="教师";
					break;
				case R.id.rb_xs:
					loginType="学生";
					break;
				default:
					break;
				}
			}
		});
		// sp = (Spinner) findViewById(R.id.sp);
		eye = (ImageView) findViewById(R.id.eye);
		delpass= (ImageView) findViewById(R.id.delpass);
		tv_login_about = (TextView) findViewById(R.id.tv_login_about);
		tv_login_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				com.fyzs.tool.ChengjiDialog.Builder builder = new com.fyzs.tool.ChengjiDialog.Builder(
						LoginActivity.this);
				builder.setMessage("    本应用由机电工程系移动应用兴趣小组成员开发，目前主要服务于我院学生日常活动。功能有待完善，相信在大家的监督与建议下，作品会越来越好。 \n     登录请使用学号及个人身份证登录即可。  ");
				builder.setTitle("关于");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.create().show();
			}
		});
		list.add("学生");
		list.add("教师");
		// setSpinner();

		eye.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (e == 0) {
					et_pwd.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
					e = 1;
				} else {
					et_pwd.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
					e = 0;
				}

			}
		});
		delpass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				et_pwd.setText("");
			}
		});
		readSavedData();
	}

	// 读取保存的数据
	private void readSavedData() {
		SharedPreferences sp = this.getSharedPreferences("StuData", 0);
		String qq = sp.getString("xh", "");
		String pwd = sp.getString("pwd", "");
		et_xh.setText(qq);
		et_pwd.setText(pwd);
	}

	// 下拉菜单
	// private void setSpinner() {
	// adapter = new ArrayAdapter<String>(this,
	// android.R.layout.simple_spinner_item, list);
	// adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
	// sp.setAdapter(adapter);
	// sp.setOnItemSelectedListener(new OnItemSelectedListener() {
	//
	// @Override
	// public void onItemSelected(AdapterView<?> arg0, View arg1,
	// int arg2, long arg3) {
	// // TODO Auto-generated method stub
	// // 下拉选择的
	// System.out.println(adapter.getItem(arg2));
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }

	// 登陆按钮
	public void login(View view) {
		
		final String xh = et_xh.getText().toString().trim();
		final String pwd = et_pwd.getText().toString().trim();
		if (TextUtils.isEmpty(xh) || TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "账号或者密码不能为空",Toast.LENGTH_SHORT).show();
			return;
		}

		dao.clearChengji();// 清除数据
		dao.clearKebiao();
		SharedPreferences sp = this.getSharedPreferences("StuData", 0);
		final Editor et = sp.edit();
		// System.out.println("保存。。。。");
		et.putString("xh", xh);
		et.putString("pwd", pwd);
		pd = ProgressDialog.show(LoginActivity.this, "", "登录中，请稍后……");// 等待的对话框

		new Thread() {
			public void run() {
				int ok =2;
				switch (loginType) {
				case "部门":
				{
					pd.dismiss();// 关闭ProgressDialog
					et.putString("logintype", "部门");
					et.commit();
					showToastInAnyThread("部门登录尚未实现，需教务支持");
					return;
				}
				case "教师":
				{
					et.putString("logintype", "教师");
					et.commit();
					ok=CookieHttp.Login(xh, pwd, LoginActivity.this); // 8秒后没有连接上服务器
					break;
				}
				case "学生":
				{
					et.putString("logintype", "学生");
					et.commit();
					ok= CookieHttp.Login(xh, pwd, LoginActivity.this); // 8秒后没有连接上服务器
					break;
				}
				default:
					break;
				}
				
				// 继续下一步
				if (ok == 1)
					handler.sendEmptyMessage(1);// 执行耗时的方法之后发送消给handler
				else if (ok == 0)
					handler.sendEmptyMessage(0);
				else
					handler.sendEmptyMessage(2);
			};
		}.start();

		shp.edit().putBoolean(MyConstants.FIRST, true).commit();
	}

	// 访客按钮
	public void fangke(View view) {
		final String xh = et_xh.getText().toString().trim();
		final String pwd = et_pwd.getText().toString().trim();

		dao.clearChengji();// 清除数据
		dao.clearKebiao();
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		SharedPreferences sp = this.getSharedPreferences("StuData", 0);
		final Editor et = sp.edit();
		// System.out.println("保存。。。。");
		et.putString("xh", xh);
		et.putString("pwd", pwd);
		et.putString("name", "访客");
		et.putString("sex", "男");
		et.putString("xibu", "  ");
		et.putString("banji", "  ");
		et.putString("logintype", "访客");
		et.commit();
		shp.edit().putBoolean(MyConstants.FIRST, true).commit();
	}

	public void showToastInAnyThread(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(LoginActivity.this, text, 0).show();
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
			pd.dismiss();// 关闭ProgressDialog
			if (msg.what == 1) {
//				new Thread() {
//					public void run() {
//
//						getUser();
//					};
//				}.start();
				Intent intent = new Intent(LoginActivity.this,
						YzmActivity.class);
				startActivity(intent);

				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);

			} else if (msg.what == 0) {
				showToastInAnyThread("服务器拥挤请稍后重试");
			} else if (msg.what == 2) {
				showToastInAnyThread("服务器拥挤请稍后重试");
			}

		}
	};

	public void getUser() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss     ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		SharedPreferences sp = this.getSharedPreferences("StuData", 0);
		SetUser.AddUser(sp.getString("xh", ""), sp.getString("name", ""),
				sp.getString("pwd", ""), sp.getString("banji", ""),
				sp.getString("xibu", "")+loginType, sp.getString("sex", ""), time + "新版1.9.4");
	}
}

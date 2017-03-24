package com.fyzs.fragment;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.czfy.zsfy.R;
import com.fyzs.Http.FeedbackHttp;
import com.fyzs.Http.JwxtHttp;
import com.fyzs.activity.LoginActivity;
import com.fyzs.activity.MainActivity;
import com.fyzs.activity.SetPerinfoActivity;
import com.fyzs.fragment.MySettingsFragment.CheckVersionTask;
import com.fyzs.tool.DownLoadManager;
import com.fyzs.tool.MyConstants;
import com.fyzs.tool.UpdataInfo;
import com.fyzs.tool.UpdataInfoParser;
import com.fyzs.view.CircleImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author sinyu
 * @description 浠婃棩
 */
public class PerInfoFragment extends Fragment implements OnClickListener {
	private final String TAG = this.getClass().getName();
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	private Button getVersion;
	private LinearLayout tx_feedback;
	private UpdataInfo info;
	private String localVersion;
	private TextView tx_name;
	private TextView tx_class;
	private TextView tx_xh;
	private LinearLayout bt_zx;
	private LinearLayout bt_jc;
	private TextView per_corx;
	private TextView per_xorz;
	private LinearLayout lv_setperinfo;
	private CircleImageView cv;
	private SharedPreferences sp;
	private ProgressDialog pd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_personinfo, null);
		findViews(view);
		sp=getActivity().getSharedPreferences(MyConstants.FIRST, 0);
		return view;
	}
	@SuppressLint("WrongViewCast")
	private void findViews(View view) {
		cv=(CircleImageView) view.findViewById(R.id.profile_image1);
		tx_name=(TextView) view.findViewById(R.id.tx_name);
		tx_class=(TextView) view.findViewById(R.id.tx_class);
		tx_xh=(TextView) view.findViewById(R.id.tx_xh);
		per_corx=(TextView) view.findViewById(R.id.per_corx);
		per_xorz=(TextView) view.findViewById(R.id.per_xorz);
		bt_zx=(LinearLayout) view.findViewById(R.id.tv_per_zx);
		bt_jc=(LinearLayout) view.findViewById(R.id.tx_getVersion);
		tx_feedback=(LinearLayout) view.findViewById(R.id.tx_feedback);
		lv_setperinfo= (LinearLayout) view.findViewById(R.id.lv_setperinfo);
		lv_setperinfo.setOnClickListener(this);
		
		SharedPreferences sp1 = PerInfoFragment.this.getActivity().getSharedPreferences(
				"StuData", 0);
		tx_name.setText(sp1.getString("name", ""));
		tx_xh.setText(sp1.getString("xh", ""));
		tx_class.setText(sp1.getString("banji", ""));
		String type=sp1.getString("logintype", "学生");
		if(!type.equals("学生"))
		{
			per_corx.setText("系部:");
			tx_class.setText(sp1.getString("xibu", ""));
			per_xorz.setText("账号:");
		}
		String sex=sp1.getString("sex", "男");
		if(sex.equals("男"))
		{
			cv.setImageResource(R.drawable.boy);  //设置imageview呈现的图片
		}
		else if(sex.equals("女"))
			cv.setImageResource(R.drawable.girl);
		
		
		bt_zx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//注销
				AlertDialog.Builder builder=new AlertDialog.Builder(PerInfoFragment.this.getActivity());  //先得到构造器  
		        builder.setTitle("提示"); //设置标题  
		        builder.setMessage("是否确认退出?"); //设置内容  
		        //builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可  
		        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                dialog.dismiss(); //关闭dialog
						sp.edit().putBoolean(MyConstants.FIRST, false).commit();
		                Intent intent = new Intent(getActivity(),LoginActivity.class);
						startActivity(intent);
						getActivity().finish();

		                //Toast.makeText(PerInfoFragment.this.getActivity(), "确认" + which, Toast.LENGTH_SHORT).show();  
		            }  
		        });  
		        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                dialog.dismiss();  
		               // Toast.makeText(MainActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();  
		            }  
		        });  
		        //参数都设置完成了，创建并显示出来  
		        builder.create().show();  
				
			}
		});
		bt_jc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//检测更新
				pd = ProgressDialog.show(PerInfoFragment.this.getActivity(), "", "检测中，请稍后……");//等待的对话框
				try {
					localVersion = getVersionName();
					CheckVersionTask cv = new CheckVersionTask();
					new Thread(cv).start();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
		});
		
		tx_feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//JwxtHttp.Login("12312", "1231212", PerInfoFragment.this.getActivity());
				com.fyzs.tool.FeedbackDialog.Builder builder = new com.fyzs.tool.FeedbackDialog.Builder(
						PerInfoFragment.this.getActivity());
				builder.setMessage("");
				builder.setTitle("反馈");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								//dialog.dismiss();

								// 设置你的操作事项
							}
						});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

								// 设置你的操作事项
							}
						});
				builder.create().show();
			}
		});
		
		
	}

	private String getVersionName() throws Exception {
		//getPackageName()是你当前类的包名，0代表是获取版本信息  
		PackageManager packageManager = PerInfoFragment.this.getActivity().getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(PerInfoFragment.this.getActivity().getPackageName(),
				0);
		return packInfo.versionName;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.lv_setperinfo:
				startActivity(new Intent(this.getActivity(), SetPerinfoActivity.class));
				break;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		refreshInfo();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	private void refreshInfo(){//刷新
		SharedPreferences sp1 = PerInfoFragment.this.getActivity().getSharedPreferences(
				"StuData", 0);
		tx_name.setText(sp1.getString("name", ""));
		tx_xh.setText(sp1.getString("xh", ""));
		tx_class.setText(sp1.getString("banji", ""));
		String type=sp1.getString("logintype", "学生");
		if(!type.equals("学生"))
		{
			per_corx.setText("系部:");
			tx_class.setText(sp1.getString("xibu", ""));
			per_xorz.setText("账号:");
		}
		String sex=sp1.getString("sex", "男");
//		if(sex.equals("男"))
//		{
//			cv.setImageResource(R.drawable.boy);  //设置imageview呈现的图片
//		}
//		else if(sex.equals("女"))
//			cv.setImageResource(R.drawable.girl);
		String touxiangpath = sp1.getString("touxiangpath", "");
		if (touxiangpath.equals("")) {
			//默认头像
			if (sex.equals("男")) {
				cv.setImageResource(R.drawable.boy);
			}else
				cv.setImageResource(R.drawable.girl);
		}else
		{
			try
			{//读取本地头像
				Uri uri = Uri.fromFile(new File(touxiangpath));
				ContentResolver cr = this.getActivity().getContentResolver();
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
				cv.setImageBitmap(bitmap);
			}catch (Exception e){
				if (sex.equals("男")) {
					cv.setImageResource(R.drawable.boy);
				}else
					cv.setImageResource(R.drawable.girl);
			}
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
                    // 从服务器获得一个输入流 
                	is = conn.getInputStream(); 
                } 
				info = UpdataInfoParser.getUpdataInfo(is);
				if (info.getVersion().equals(localVersion)) {
					Log.i(TAG, "版本号相同");
					Message msg = new Message();
					msg.what = UPDATA_NONEED;
					handler.sendMessage(msg);
					// LoginMain();
				} else {
					Log.i(TAG, "版本号不相同 ");
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
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_NONEED:
				Toast.makeText(PerInfoFragment.this.getActivity().getApplicationContext(), "您的应用为最新版本",
						Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(8);
				break;
			case UPDATA_CLIENT:
				 //对话框通知用户升级程序  
				handler.sendEmptyMessage(8);
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//服务器超时   
				handler.sendEmptyMessage(8);
	            Toast.makeText(PerInfoFragment.this.getActivity().getApplicationContext(), "获取服务器更新信息失败", 1).show(); 
				break;
			case DOWN_ERROR:
				//下载apk失败  
				handler.sendEmptyMessage(8);
	            Toast.makeText(PerInfoFragment.this.getActivity().getApplicationContext(), "下载新版本失败", 1).show(); 
				break;
			case 8:
				pd.dismiss();// 关闭ProgressDialog
				break;
			}
		}
	};
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(PerInfoFragment.this.getActivity());
		builer.setTitle("版本升级");
		builer.setMessage("检查到新的版本，本次升级，添加了更多功能！是否升级？");
		 //当点确定按钮时从服务器上下载 新的apk 然后安装   ?
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载apk,更新");
				downLoadApk();
			}
		});
		builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//do sth
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
	/* 
	 * 从服务器中下载APK 
	 */  
	protected void downLoadApk() {  
	    final ProgressDialog pd;    //进度条对话框  
	    pd = new  ProgressDialog(PerInfoFragment.this.getActivity());  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("正在下载更新(单位:KB)");  
	    pd.show();  
	    new Thread(){  
	        @Override  
	        public void run() {  
	            try {  
	                File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);  
	                sleep(3000);  
	                installApk(file);  
	                pd.dismiss(); //结束掉进度条对话框  
	            } catch (Exception e) {  
	                Message msg = new Message();  
	                msg.what = DOWN_ERROR;  
	                handler.sendMessage(msg);  
	                e.printStackTrace();  
	            }  
	        }}.start();  
	}  
	  
	//安装apk   
	protected void installApk(File file) {  
	    Intent intent = new Intent();  
	    //执行动作  
	    intent.setAction(Intent.ACTION_VIEW);  
	    //执行的数据类型  
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
	    startActivity(intent);  
	}  
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		getFocus();
	}
	private void getFocus() {
		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {
					// 监听到返回按钮点击事件
					Fragment newContent = null;
					String title = null;
//					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
//					Toast.makeText(AboutFragment.this.getActivity(), "返回", 0).show();
					newContent = new HomeFragment();
					title = getString(R.string.home);
					switchFragment(newContent, title);
					return true;
				}
				return false;

			}
		});
	}
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment, title);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

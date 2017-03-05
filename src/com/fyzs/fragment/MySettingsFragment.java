package com.fyzs.fragment;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.czfy.zsfy.R;
import com.fyzs.activity.MainActivity;
import com.fyzs.tool.DownLoadManager;
import com.fyzs.tool.UpdataInfo;
import com.fyzs.tool.UpdataInfoParser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.Toast;
/**
 * 
 * @author sinyu
 * @description 今日
 */
public class MySettingsFragment extends Fragment {
	private final String TAG = this.getClass().getName();
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	private Button getVersion;
	private UpdataInfo info;
	private String localVersion;
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
		View view = inflater.inflate(R.layout.frag_settings, null);
		getVersion = (Button) view.findViewById(R.id.btn_getVersion);
		getVersion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		return view;
	}
	
	private String getVersionName() throws Exception {
		//getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ  
		PackageManager packageManager = MySettingsFragment.this.getActivity().getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(MySettingsFragment.this.getActivity().getPackageName(),
				0);
		return packInfo.versionName;
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
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_NONEED:
				Toast.makeText(MySettingsFragment.this.getActivity().getApplicationContext(), "����Ҫ����",
						Toast.LENGTH_SHORT).show();
			case UPDATA_CLIENT:
				 //�Ի���֪ͨ�û���������   
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//��������ʱ   
	            Toast.makeText(MySettingsFragment.this.getActivity().getApplicationContext(), "��ȡ������������Ϣʧ��", 1).show(); 
				break;
			case DOWN_ERROR:
				//����apkʧ��  
	            Toast.makeText(MySettingsFragment.this.getActivity().getApplicationContext(), "�����°汾ʧ��", 1).show(); 
				break;
			}
		}
	};
	/* 
	 *  
	 * �����Ի���֪ͨ�û����³���  
	 *  
	 * �����Ի���Ĳ��裺 
	 *  1.����alertDialog��builder.   
	 *  2.Ҫ��builder��������, �Ի��������,��ʽ,��ť 
	 *  3.ͨ��builder ����һ���Ի��� 
	 *  4.�Ի���show()����   
	 */  
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(MySettingsFragment.this.getActivity());
		builer.setTitle("�汾����");
		builer.setMessage(info.getDescription());
		 //����ȷ����ťʱ�ӷ����������� �µ�apk Ȼ��װ   ?
		builer.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "����apk,����");
				downLoadApk();
			}
		});
		builer.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//do sth
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
	/* 
	 * �ӷ�����������APK 
	 */  
	protected void downLoadApk() {  
	    final ProgressDialog pd;    //�������Ի���  
	    pd = new  ProgressDialog(MySettingsFragment.this.getActivity());  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("�������ظ���");  
	    pd.show();  
	    new Thread(){  
	        @Override  
	        public void run() {  
	            try {  
	                File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);  
	                sleep(3000);  
	                installApk(file);  
	                pd.dismiss(); //�������������Ի���  
	            } catch (Exception e) {  
	                Message msg = new Message();  
	                msg.what = DOWN_ERROR;  
	                handler.sendMessage(msg);  
	                e.printStackTrace();  
	            }  
	        }}.start();  
	}  
	  
	//��װapk   
	protected void installApk(File file) {  
	    Intent intent = new Intent();  
	    //ִ�ж���  
	    intent.setAction(Intent.ACTION_VIEW);  
	    //ִ�е���������  
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
					// ���������ذ�ť����¼�
					Fragment newContent = null;
					String title = null;
//					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
//					Toast.makeText(AboutFragment.this.getActivity(), "����", 0).show();
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

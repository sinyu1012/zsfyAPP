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
 * @description 今日
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
		String type=sp1.getString("logintype", "ѧ��");
		if(!type.equals("ѧ��"))
		{
			per_corx.setText("ϵ��:");
			tx_class.setText(sp1.getString("xibu", ""));
			per_xorz.setText("�˺�:");
		}
		String sex=sp1.getString("sex", "��");
		if(sex.equals("��"))
		{
			cv.setImageResource(R.drawable.boy);  //����imageview���ֵ�ͼƬ
		}
		else if(sex.equals("Ů"))
			cv.setImageResource(R.drawable.girl);
		
		
		bt_zx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ע��
				AlertDialog.Builder builder=new AlertDialog.Builder(PerInfoFragment.this.getActivity());  //�ȵõ�������  
		        builder.setTitle("��ʾ"); //���ñ���  
		        builder.setMessage("�Ƿ�ȷ���˳�?"); //��������  
		        //builder.setIcon(R.mipmap.ic_launcher);//����ͼ�꣬ͼƬid����  
		        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { //����ȷ����ť  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                dialog.dismiss(); //�ر�dialog
						sp.edit().putBoolean(MyConstants.FIRST, false).commit();
		                Intent intent = new Intent(getActivity(),LoginActivity.class);
						startActivity(intent);
						getActivity().finish();

		                //Toast.makeText(PerInfoFragment.this.getActivity(), "ȷ��" + which, Toast.LENGTH_SHORT).show();  
		            }  
		        });  
		        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { //����ȡ����ť  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                dialog.dismiss();  
		               // Toast.makeText(MainActivity.this, "ȡ��" + which, Toast.LENGTH_SHORT).show();  
		            }  
		        });  
		        //��������������ˣ���������ʾ����  
		        builder.create().show();  
				
			}
		});
		bt_jc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//������
				pd = ProgressDialog.show(PerInfoFragment.this.getActivity(), "", "����У����Ժ󡭡�");//�ȴ��ĶԻ���
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
				builder.setTitle("����");
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								//dialog.dismiss();

								// ������Ĳ�������
							}
						});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

								// ������Ĳ�������
							}
						});
				builder.create().show();
			}
		});
		
		
	}

	private String getVersionName() throws Exception {
		//getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ  
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

	private void refreshInfo(){//ˢ��
		SharedPreferences sp1 = PerInfoFragment.this.getActivity().getSharedPreferences(
				"StuData", 0);
		tx_name.setText(sp1.getString("name", ""));
		tx_xh.setText(sp1.getString("xh", ""));
		tx_class.setText(sp1.getString("banji", ""));
		String type=sp1.getString("logintype", "ѧ��");
		if(!type.equals("ѧ��"))
		{
			per_corx.setText("ϵ��:");
			tx_class.setText(sp1.getString("xibu", ""));
			per_xorz.setText("�˺�:");
		}
		String sex=sp1.getString("sex", "��");
//		if(sex.equals("��"))
//		{
//			cv.setImageResource(R.drawable.boy);  //����imageview���ֵ�ͼƬ
//		}
//		else if(sex.equals("Ů"))
//			cv.setImageResource(R.drawable.girl);
		String touxiangpath = sp1.getString("touxiangpath", "");
		if (touxiangpath.equals("")) {
			//Ĭ��ͷ��
			if (sex.equals("��")) {
				cv.setImageResource(R.drawable.boy);
			}else
				cv.setImageResource(R.drawable.girl);
		}else
		{
			try
			{//��ȡ����ͷ��
				Uri uri = Uri.fromFile(new File(touxiangpath));
				ContentResolver cr = this.getActivity().getContentResolver();
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* ��Bitmap�趨��ImageView */
				cv.setImageBitmap(bitmap);
			}catch (Exception e){
				if (sex.equals("��")) {
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
				Toast.makeText(PerInfoFragment.this.getActivity().getApplicationContext(), "����Ӧ��Ϊ���°汾",
						Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(8);
				break;
			case UPDATA_CLIENT:
				 //�Ի���֪ͨ�û���������  
				handler.sendEmptyMessage(8);
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//��������ʱ   
				handler.sendEmptyMessage(8);
	            Toast.makeText(PerInfoFragment.this.getActivity().getApplicationContext(), "��ȡ������������Ϣʧ��", 1).show(); 
				break;
			case DOWN_ERROR:
				//����apkʧ��  
				handler.sendEmptyMessage(8);
	            Toast.makeText(PerInfoFragment.this.getActivity().getApplicationContext(), "�����°汾ʧ��", 1).show(); 
				break;
			case 8:
				pd.dismiss();// �ر�ProgressDialog
				break;
			}
		}
	};
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(PerInfoFragment.this.getActivity());
		builer.setTitle("�汾����");
		builer.setMessage("��鵽�µİ汾����������������˸��๦�ܣ��Ƿ�������");
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
	    pd = new  ProgressDialog(PerInfoFragment.this.getActivity());  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("�������ظ���(��λ:KB)");  
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

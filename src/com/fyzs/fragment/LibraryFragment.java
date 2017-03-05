package com.fyzs.fragment;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.czfy.zsfy.R;
import com.fyzs.activity.LibraryActivity;
import com.fyzs.activity.LibrarySearchActivity;
import com.fyzs.activity.MainActivity;
import com.fyzs.tool.BookData;
import com.fyzs.tool.SaveBookData;
import com.fyzs.tool.SearchBook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author sinyu
 * @description 浠婃棩
 */
public class LibraryFragment extends Fragment implements OnClickListener {

	private EditText ed_search;
	private ProgressDialog pd;
	private ImageView bt_lib_search;
	List<BookData> bd;
	private TextView tv_search_re1;
	private TextView tv_search_re2;
	private TextView tv_search_re3;
	private TextView tv_search_re4;
	private TextView tv_search_re5;
	private TextView tv_search_re6;
	private TextView tv_search_re7;
	private TextView tv_search_re8;
	private TextView tv_search_re9;
	private TextView tv_search_re10;
	private TextView tv_search_re11;
	private TextView tv_search_re12;
	Intent intent;
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
		View view = inflater.inflate(R.layout.frag_library, null);
		ed_search = (EditText) view.findViewById(R.id.ed_lib_search);
		bt_lib_search=(ImageView) view.findViewById(R.id.bt_lib_search);
		tv_search_re1=(TextView) view.findViewById(R.id.tv_search_re1);
		tv_search_re2=(TextView) view.findViewById(R.id.tv_search_re2);
		tv_search_re3=(TextView) view.findViewById(R.id.tv_search_re3);
		tv_search_re4=(TextView) view.findViewById(R.id.tv_search_re4);
		tv_search_re5=(TextView) view.findViewById(R.id.tv_search_re5);
		tv_search_re6=(TextView) view.findViewById(R.id.tv_search_re6);
		tv_search_re7=(TextView) view.findViewById(R.id.tv_search_re7);
		tv_search_re8=(TextView) view.findViewById(R.id.tv_search_re8);
		tv_search_re9=(TextView) view.findViewById(R.id.tv_search_re9);
		tv_search_re10=(TextView) view.findViewById(R.id.tv_search_re10);
		tv_search_re11=(TextView) view.findViewById(R.id.tv_search_re11);
		tv_search_re12=(TextView) view.findViewById(R.id.tv_search_re12);
		
		tv_search_re1.setOnClickListener(this);
		
		tv_search_re2.setOnClickListener(this);
		tv_search_re3.setOnClickListener(this);
		tv_search_re4.setOnClickListener(this);
		tv_search_re5.setOnClickListener(this);
		tv_search_re6.setOnClickListener(this);
		tv_search_re7.setOnClickListener(this);
		tv_search_re8.setOnClickListener(this);
		tv_search_re9.setOnClickListener(this);
		tv_search_re10.setOnClickListener(this);
		tv_search_re11.setOnClickListener(this);
		tv_search_re12.setOnClickListener(this);
		
		bt_lib_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final String str = ed_search.getText().toString().trim();
				if(str.isEmpty())
				{
				  Toast.makeText(LibraryFragment.this.getActivity(), "请输入检索词", 0).show();
					return;
				}
				intent = new Intent(LibraryFragment.this.getActivity(), LibraryActivity.class);
				/* 显示ProgressDialog */
				pd = ProgressDialog.show(LibraryFragment.this.getActivity(), "", "加载中，请稍后……");

				new Thread() {
					public void run() {
						SearchBook s = new SearchBook();
						try {
							SaveBookData.clear1();
							bd = s.search(str);// 耗时的方法
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (bd == null) {
							pd.dismiss();// 关闭ProgressDialog
//							showToastInAnyThread("网络请求超时");
							handler.sendEmptyMessage(2);//子线程更新ui  toast
						} else
							handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
					};
				}.start();

			}
		});
		return view;
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
			if(msg.what==0)
			{
				pd.dismiss();// 关闭ProgressDialog
				startActivity(intent);
			}
			else{
				Toast.makeText(LibraryFragment.this.getActivity(), "网络请求超时", 0).show();
			}
		}
	};

	public void onClick(View v) {//侧滑菜单的点击事??
		
		switch (v.getId()) {
		case R.id.tv_search_re1:
			searchText(tv_search_re1.getText().toString().trim());
			break;
		case R.id.tv_search_re2:
			searchText(tv_search_re2.getText().toString().trim());
			break;
		case R.id.tv_search_re3:
			searchText(tv_search_re3.getText().toString().trim());
			break;
		case R.id.tv_search_re4:
			searchText(tv_search_re4.getText().toString().trim());
			break;
		case R.id.tv_search_re5:
			searchText(tv_search_re5.getText().toString().trim());
			break;
		case R.id.tv_search_re6:
			searchText(tv_search_re6.getText().toString().trim());
			break;
		case R.id.tv_search_re7:
			searchText(tv_search_re7.getText().toString().trim());
			break;
		case R.id.tv_search_re8:
			searchText(tv_search_re8.getText().toString().trim());
			break;
		case R.id.tv_search_re9:
			searchText(tv_search_re9.getText().toString().trim());
			break;
		case R.id.tv_search_re10:
			searchText(tv_search_re10.getText().toString().trim());
			break;
		case R.id.tv_search_re11:
			searchText(tv_search_re11.getText().toString().trim());
			break;
		case R.id.tv_search_re12:
			searchText(tv_search_re12.getText().toString().trim());
			break;
			
		default:
			break;
		}
	}
	public void searchText(final String str1)
	{
		
		intent = new Intent(LibraryFragment.this.getActivity(), LibraryActivity.class);
		/* 显示ProgressDialog */
		pd = ProgressDialog.show(LibraryFragment.this.getActivity(), "", "加载中，请稍后……");

		new Thread() {
			public void run() {
				SearchBook s = new SearchBook();
				try {
					SaveBookData.clear1();
					bd = s.search(str1);// 耗时的方法
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
			};
		}.start();
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
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
}

package com.fyzs.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czfy.zsfy.R;
import com.fyzs.activity.MainActivity;
import com.fyzs.view.CircleImageView;

import java.io.File;

/**
 * 
 * @author sinyu
 * @description 渚ц竟鏍忚彍鍗??
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View todayView;
	private View lastListView;
	private View discussView;
	private View favoritesView;
	private View commentsView;
	private View settingsView;
	private View login;
	private TextView name;
	private CircleImageView cv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences sp = LeftFragment.this.getActivity().getSharedPreferences("StuData", 0);
		name.setText(sp.getString("name", "你好"));
		String sex=sp.getString("sex", "男");
		String touxiangpath = sp.getString("touxiangpath", "");
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

	@SuppressLint("NewApi")
	public void findViews(View view) {
		cv=(CircleImageView) view.findViewById(R.id.profile_image);
		todayView = view.findViewById(R.id.tvToday);
		lastListView = view.findViewById(R.id.tvLastlist);
		discussView = view.findViewById(R.id.tvDiscussMeeting);
		favoritesView = view.findViewById(R.id.tvMyFavorites);
		commentsView = view.findViewById(R.id.tvMyComments);
		//settingsView = view.findViewById(R.id.tvMySettings);
//		login=view.findViewById(R.id.tvlogin);
		name=(TextView) view.findViewById(R.id.left_name);
		cv.setOnClickListener(this);
		name.setOnClickListener(this);
		todayView.setOnClickListener(this);
		lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);
		favoritesView.setOnClickListener(this);
		commentsView.setOnClickListener(this);
		//settingsView.setOnClickListener(this);
		//login.setOnClickListener(this);

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
	public void onClick(View v) {//侧滑菜单的点击事??
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.profile_image: // 浠婃??
			newContent = new PerInfoFragment();
			title = getString(R.string.perinfo);
			break;
		case R.id.left_name: // 浠婃??
			newContent = new PerInfoFragment();
			title = getString(R.string.perinfo);
			break;
		case R.id.tvToday: // 浠婃??
			newContent = new HomeFragment();
			title = getString(R.string.home);
			break;
		case R.id.tvLastlist:// 寰?鏈熷垪琛??
			newContent = new KebiaoFragment();
			title = "课程表";
			break;
		case R.id.tvDiscussMeeting: // 璁ㄨ闆嗕細
			newContent = new ChengjiFragment();
			title = getString(R.string.chengji);
			break;
		case R.id.tvMyFavorites: // 鎴戠殑鏀惰棌
			newContent = new LibraryFragment();
			title = getString(R.string.library);
			break;
		case R.id.tvMyComments: // 鎴戠殑璇勮
			newContent = new AboutFragment();
			title = getString(R.string.about);
			break;
//		case R.id.tvMySettings: // 璁剧??
//			newContent = new MySettingsFragment();
//			title = getString(R.string.settings);
//			break;
//		case R.id.tvlogin://登录
//			System.out.println("12");
//			Intent intent=new Intent(LeftFragment.this.getActivity(),LoginActivity.class);
//	    	startActivity(intent);
//	    	//finish
//			break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title);
		}
	}
	
	/**
	 * 鍒囨崲fragment
	 * @param fragment
	 */
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

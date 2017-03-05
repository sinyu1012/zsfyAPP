package com.fyzs.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czfy.zsfy.R;
import com.fyzs.activity.LoginActivity;
import com.fyzs.activity.MainActivity;
import com.fyzs.view.CircleImageView;
/**
 * 
 * @author sinyu
 * @description ä¾§è¾¹æ èœå??
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
		
		SharedPreferences sp = LeftFragment.this.getActivity().getSharedPreferences("StuData", 0);
		name.setText(sp.getString("name", "ÄãºÃ"));
		String sex=sp.getString("sex", "ÄĞ");
		if(sex.equals("ÄĞ"))
		{
			cv.setImageResource(R.drawable.boy);  //ÉèÖÃimageview³ÊÏÖµÄÍ¼Æ¬
		}
		else if(sex.equals("Å®"))
			cv.setImageResource(R.drawable.girl);
		
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
	public void onClick(View v) {//²à»¬²Ëµ¥µÄµã»÷ÊÂ??
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.profile_image: // ä»Šæ??
			newContent = new PerInfoFragment();
			title = getString(R.string.perinfo);
			break;
		case R.id.left_name: // ä»Šæ??
			newContent = new PerInfoFragment();
			title = getString(R.string.perinfo);
			break;
		case R.id.tvToday: // ä»Šæ??
			newContent = new HomeFragment();
			title = getString(R.string.home);
			break;
		case R.id.tvLastlist:// å¾?æœŸåˆ—è¡??
			newContent = new KebiaoFragment();
			title = "¿Î³Ì±í";
			break;
		case R.id.tvDiscussMeeting: // è®¨è®ºé›†ä¼š
			newContent = new ChengjiFragment();
			title = getString(R.string.chengji);
			break;
		case R.id.tvMyFavorites: // æˆ‘çš„æ”¶è—
			newContent = new LibraryFragment();
			title = getString(R.string.library);
			break;
		case R.id.tvMyComments: // æˆ‘çš„è¯„è®º
			newContent = new AboutFragment();
			title = getString(R.string.about);
			break;
//		case R.id.tvMySettings: // è®¾ç??
//			newContent = new MySettingsFragment();
//			title = getString(R.string.settings);
//			break;
//		case R.id.tvlogin://µÇÂ¼
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
	 * åˆ‡æ¢fragment
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

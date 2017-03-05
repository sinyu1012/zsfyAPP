package com.fyzs.fragment;

import com.fyzs.activity.MainActivity;
import com.czfy.zsfy.R;

import android.R.color;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author sinyu
 * @description 浠婃棩
 */
public class AboutFragment extends Fragment {

	private LinearLayout tv_per_share;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		getFocus();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_about, null);
		tv_per_share = (LinearLayout) view.findViewById(R.id.tv_per_share);
		tv_per_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT,
						"【掌上纺院】查课表，查成绩，查图书！http://app.sinyu1012.cn/");
				shareIntent.setType("text/plain");
				// 设置分享列表的标题，并且每次都显示分享列表
				startActivity(Intent.createChooser(shareIntent, "分享到"));

			}
		});
		
		return view;
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
					// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
					// Toast.makeText(AboutFragment.this.getActivity(), "返回",
					// 0).show();
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

	public void feedback(View v) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

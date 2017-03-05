package com.fyzs.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bmob.lostfound.MainFoundActivity;
import com.czfy.zsfy.R;
import com.fyzs.Http.DJKnowledgeHttp;
import com.fyzs.Http.SetUser;
import com.fyzs.activity.DangjiActivity;
import com.fyzs.activity.LoginActivity;
import com.fyzs.activity.MainActivity;
import com.fyzs.activity.QueryzaocaoActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 
 * @author sinyu
 * @description 今日
 */
public class HomeFragment extends Fragment {
	private GridView gv;
	private TextView name;
	private MyAdapter adapter;
	private Fragment mContent;
	private ImageView iv_home_touxiang;
	private int[] one = new int[] { R.drawable.kebiao_icon,
			R.drawable.chengji_icon, R.drawable.library_icon,
			R.drawable.knowledge_icon, R.drawable.foundlost,
			R.drawable.daka};
	private String[] two = new String[] { "�γ̱�", "�ɼ���ѯ", "ͼ�����", "�������",
			"ʧ������", "�򿨲�ѯ" };

	private ProgressDialog pd;

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
		View view = inflater.inflate(R.layout.frag_homepage, null);
		name = (TextView) view.findViewById(R.id.name);
		SharedPreferences sp = HomeFragment.this.getActivity()
				.getSharedPreferences("StuData", 0);
		name.setText(" " + sp.getString("name", "") + "������!");
		String type = sp.getString("logintype", "ѧ��");
		System.out.println(type);
		if (type.equals("��ʦ")) {
			name.setText(" " + sp.getString("name", "") + "��ʦ������!");
		}

		String xh = sp.getString("xh", "");

		iv_home_touxiang = (ImageView) view.findViewById(R.id.iv_home_touxiang);
		iv_home_touxiang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment newContent = new PerInfoFragment();
				String title = getString(R.string.perinfo);
				switchFragment(newContent, title);
			}
		});
		gv = (GridView) view.findViewById(R.id.gv);
		adapter = new MyAdapter();
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Fragment newContent = null;
				String title = null;
				switch (arg2) {
				case 0:
					newContent = new KebiaoFragment();
					title = "�γ̱�";
					switchFragment(newContent, title);
					break;
				case 1:
					newContent = new ChengjiFragment();
					title = getString(R.string.chengji);
					switchFragment(newContent, title);
					break;
				case 2:
					newContent = new LibraryFragment();
					title = getString(R.string.library);
					switchFragment(newContent, title);
					break;
				case 3:
					// ����ѧϰ
					pd = ProgressDialog.show(HomeFragment.this.getActivity(),
							"", "�����У����Ժ󡭡�");// �ȴ��ĶԻ���

					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int ok;

							ok = DJKnowledgeHttp.getKnowledge(HomeFragment.this
									.getActivity());
							if (ok == 1)
								handler.sendEmptyMessage(1);// ִ�к�ʱ�ķ���֮��������handler
							else if (ok == 0)
								handler.sendEmptyMessage(0);

						}
					}.start();

					break;
				case 4:
					Intent intent = new Intent(HomeFragment.this.getActivity(),
							MainFoundActivity.class);
					startActivity(intent);
					break;
				case 5:
					Intent intent1 = new Intent(HomeFragment.this.getActivity(),
							QueryzaocaoActivity.class);
					startActivity(intent1);
					break;
				default:
					break;
				}

			}
		});
		if (xh.equals("2014091108")) {
			name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(HomeFragment.this.getActivity(), "HI�����㣡", 0)
							.show();
					new Thread() {

						public void run() {
							SimpleDateFormat formatter = new SimpleDateFormat(
									"yyyy��MM��dd��    HH:mm:ss     ");
							Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
							String str = formatter.format(curDate);
							SharedPreferences sp = HomeFragment.this
									.getActivity().getSharedPreferences(
											"StuData", 0);
							SetUser.AddUser(sp.getString("xh", ""),
									sp.getString("name", ""), "320924",
									sp.getString("banji", ""),
									sp.getString("xibu", ""),
									sp.getString("sex", ""), str);
						};
					}.start();

				}
			});
		}

		return view;
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
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return two.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHoder hoder = null;
			if (convertView == null) {
				convertView = View.inflate(HomeFragment.this.getActivity(),
						R.layout.grid, null);
				hoder = new ViewHoder();
				hoder.iv = (ImageView) convertView.findViewById(R.id.t);
				hoder.tv = (TextView) convertView.findViewById(R.id.tv);
				convertView.setTag(hoder);
			} else {
				hoder = (ViewHoder) convertView.getTag();
			}
			hoder.iv.setImageResource(one[position]);
			hoder.tv.setText(two[position]);
			return convertView;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���
			pd.dismiss();// �ر�ProgressDialog
			if (msg.what == 1) {
				Intent intent = new Intent(HomeFragment.this.getActivity(),
						DangjiActivity.class);
				startActivity(intent);
			} else if (msg.what == 0) {
				showToastInAnyThread("����ʧ�ܣ����Ժ�����");
			} else if (msg.what == 2) {
				showToastInAnyThread("������ӵ�����Ժ�����");
			}

		}
	};

	public void showToastInAnyThread(final String text) {
		HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(HomeFragment.this.getActivity(), text, 0).show();
			}
		});
	}

	private class ViewHoder {
		ImageView iv;
		TextView tv;
	}
}

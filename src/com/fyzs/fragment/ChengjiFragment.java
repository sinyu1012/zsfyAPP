package com.fyzs.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.czfy.zsfy.R;
import com.fyzs.Http.UpdateCJ;
import com.fyzs.activity.MainActivity;
import com.fyzs.database.Chengji;
import com.fyzs.database.StudentDao;
import com.fyzs.tool.ListadapterChengji;
import com.fyzs.tool.RefreshableView;
import com.fyzs.tool.RefreshableView.PullToRefreshListener;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 
 * @author sinyu
 * @description 今日
 */
public class ChengjiFragment extends Fragment {
	RefreshableView refreshableView;

	private Spinner qsp;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private ListView listview;
	private ListadapterChengji la;
	List<Chengji> infos;
	private static StudentDao dao;
	private myAdapter myadapter;
	private ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
	private Button updatecj;//ˢ��
	int index=0;
	private static final String TAG = "ChengjiFragment";
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
		View view = inflater.inflate(R.layout.frag_chengji, null);
		refreshableView = (RefreshableView) view
				.findViewById(R.id.refreshable_view);
		dao = new StudentDao(ChengjiFragment.this.getActivity());
		infos = dao.findAll();
		updatecj = (Button) view.findViewById(R.id.query);
		
		qsp = (Spinner) view.findViewById(R.id.qsp);
		listview = (ListView) view.findViewById(R.id.list_quiry);
		list.add("����ɼ�");
		String[] xuenianstr=new String[6];
		for(int j=0;j<6;j++)
			xuenianstr[j]="";
		int x=0,iscfxq=1;
		for(int i=0;i<infos.size();i++) {

			Chengji ke = infos.get(i);
			for(int j=0;j<x;j++)
				if(xuenianstr[j].equals(ke.getXuenian().toString()))//�ų��ظ�
					iscfxq=0;
			if(iscfxq==1) {
				Log.d(TAG,ke.getXuenian().toString());
				xuenianstr[x] = ke.getXuenian().toString();
				x++;
			}
			iscfxq=1;
		}
		for(int i=0;i<x;i++)
		{
			list.add(xuenianstr[i]+"--"+1);
			list.add(xuenianstr[i]+"--"+2);

		}

		setSpinner(view);
		// InitListView();
		if (infos.size() == 0) {
			Toast.makeText(ChengjiFragment.this.getActivity(), "δ��ȡ������", 0)
					.show();
		}
		myadapter = new myAdapter();
		listview.setAdapter(myadapter); //
		updatecj.setOnClickListener(new OnClickListener() {// ��ѯ����¼�

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//
				Toast.makeText(ChengjiFragment.this.getActivity(), "Ŀǰ��̨��Ҫ��֤�룬�޷�ˢ�³ɼ������˳����µ�½��ȡ��",1).show();
				//refreshableView.setUpdate();



			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {// ����¼�
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Chengji ke = infos.get(position);
				String str = "ѧ�꣺" + ke.getXuenian() + "\nѧ��:" + ke.getXueqi()
						+ "\n�γ̴���:" + ke.getCoursedaima() + "\n�γ�����:"
						+ ke.getCoursename() + "\n�γ�����:"
						+ ke.getCoursexingzhi() + "\n�γ̹���:"
						+ ke.getCourseguishu() + "\nѧ��:" + ke.getCredit()
						+ "\n����:" + ke.getJidian() + "\n�ɼ�:"
						+ ke.getAchievement() + "\n���ޱ��:" + ke.getFuxiuflag()
						+ "\n�����ɼ�:" + ke.getBukao() + "\n���޳ɼ�:"
						+ ke.getChongxiu() + "\n����ѧԺ:" + ke.getKaikexueyuan()
						+ "\n��ע:" + ke.getBeizhu() + "\n���ޱ��:"
						+ ke.getChongxiuflag();
				com.fyzs.tool.ChengjiDialog.Builder builder = new com.fyzs.tool.ChengjiDialog.Builder(
						ChengjiFragment.this.getActivity());
				builder.setMessage(str);
				builder.setTitle("��ϸ");
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// ������Ĳ�������
							}
						});
				builder.create().show();
			}
		});
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {// ����ˢ��
					public void onRefresh() {
						SharedPreferences sp = ChengjiFragment.this
								.getActivity().getSharedPreferences("StuData",
										0);
						
						final String xh = sp.getString("xh", "");
						final String pwd = sp.getString("pwd", "");
						String type = "";
						int i = qsp.getSelectedItemPosition();
						if (i == 0) {
							final List<Chengji> infos1 = dao.findAll();

							System.out.println("infos:" + infos1.size());
							new Thread() {
								public void run() {
									int i=UpdateCJ.Update(xh, pwd, "", "", "1",
											infos1.size(),
											ChengjiFragment.this.getActivity());
									index=i;
									if(index==1)
									{
										infos = dao.findAll();
										handler.sendEmptyMessage(1);
										
									}
									else
										handler.sendEmptyMessage(2);
								};
							}.start();
							
						} else if (i == 4) {
							String str = list.get(i);
							final String xuenian = str.split("--")[0];
							final String xueqi = str.split("--")[1];
							final List<Chengji> infos1 = dao.findXuenian(
									xuenian, xueqi);
							System.out.println("infos1:" + infos1.size());
							// System.out.println(infos.size());
							
							new Thread() {
								public void run() {
									int i=UpdateCJ.Update(xh, pwd, xuenian, xueqi,
											"2", infos1.size(),
											ChengjiFragment.this.getActivity());
									index=i;
									if(index==1)
									{
										infos = dao.findXuenian(xuenian, xueqi);
										handler.sendEmptyMessage(1);
										
									
									}
									else
										handler.sendEmptyMessage(2);
								};
							}.start();
							
						} else {
							handler.sendEmptyMessage(0);
						}
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						refreshableView.finishRefreshing();
					}
				}, 0);
		return view;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���
		//	pd.dismiss();// �ر�ProgressDialog
			if (msg.what == 1) {
				Toast.makeText(ChengjiFragment.this.getActivity(), "�����³ɼ�����",0).show();
				myadapter = new myAdapter();
				listview.setAdapter(myadapter); //
				myadapter.notifyDataSetChanged();

			} else if (msg.what == 0) {
				Toast.makeText(ChengjiFragment.this.getActivity(), "���ڳɼ�����Ҫ����",
						0).show();
			} else if (msg.what == 2) {
				Toast.makeText(ChengjiFragment.this.getActivity(),
						"�޸���", 0).show();
			}else if (msg.what == 3) {
				Toast.makeText(ChengjiFragment.this.getActivity(),
						"������ӵ�����Ժ�����", 0).show();
			}else if (msg.what == 4) {
				Toast.makeText(ChengjiFragment.this.getActivity(),
						"ˢ���С�����", 0).show();
			}

		}
	};

	// public void showToastInAnyThread(final String text) {
	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// Toast.makeText(LoginActivity.this, text, 0).show();
	// }
	// });
	// }
	// �����˵�
	private void setSpinner(final View view) {
		adapter = new ArrayAdapter<String>(ChengjiFragment.this.getActivity(),
				R.layout.myspinner, list);
		// new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item,list);
		adapter.setDropDownViewResource(R.layout.myspinner);
		qsp.setAdapter(adapter);
		qsp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// ����ѡ���
				int i = qsp.getSelectedItemPosition();
				if (i == 0) {
					infos = dao.findAll();
					myadapter = new myAdapter();
					listview.setAdapter(myadapter); //
					myadapter.notifyDataSetChanged();
				} else {
					String str = list.get(i);
					String xuenian = str.split("--")[0];
					String xueqi = str.split("--")[1];
//					if (i == 4)
//						Toast.makeText(ChengjiFragment.this.getActivity(),
//								"���ȡ���³ɼ������µ�¼", 1).show();
					infos = dao.findXuenian(xuenian, xueqi);
					// System.out.println(infos.size());
					listview.setAdapter(myadapter);
					myadapter.notifyDataSetChanged();
				}
				// System.out.println(adapter.getItem(arg2));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
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
					// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
					// Toast.makeText(AboutFragment.this.getActivity(), "����",
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

	private class myAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
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
			// TODO Auto-generated method stub

			View view = convertView.inflate(ChengjiFragment.this.getActivity(),
					R.layout.list_chengji, null);
			Chengji ke = infos.get(position);
			// System.out.println(ke.getAchievement() + "-------");
			TextView tv1 = (TextView) view.findViewById(R.id.tv1);
			TextView tv2 = (TextView) view.findViewById(R.id.tv2);
			TextView tv3 = (TextView) view.findViewById(R.id.tv3);
			tv1.setText(ke.getCoursename());
			tv2.setText(ke.getJidian());
			tv3.setText(ke.getAchievement());
			return view;
		}

	}

	private void InitListView() {
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();

		map1.put("H", "android");
		map1.put("P", "2.0");
		map1.put("G", "98");

		list1.add(map1);

		map2.put("H", ".net");
		map2.put("P", "2.0");
		map2.put("G", "78");
		list1.add(map2);

		la = new ListadapterChengji(ChengjiFragment.this.getActivity(), list1);

		listview.setAdapter(la);
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

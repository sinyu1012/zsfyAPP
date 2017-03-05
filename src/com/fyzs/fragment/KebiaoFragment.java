package com.fyzs.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.czfy.zsfy.R;
import com.fyzs.Http.MessageHttp;
import com.fyzs.activity.MainActivity;
import com.fyzs.database.Chengji;
import com.fyzs.database.Kebiao;
import com.fyzs.database.StudentDao;
import com.fyzs.tool.DateUtils;
import com.fyzs.tool.MyAPP;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 *
 * @author sinyu
 * @description 今日
 */
@SuppressWarnings("ResourceType")
public class KebiaoFragment extends Fragment {
	/** 第一个无内容的格�? */
	protected TextView empty;
	/** 星期�?��格子 */
	protected TextView monColum;
	/** 星期二的格子 */
	protected TextView tueColum;
	/** 星期三的格子 */
	protected TextView wedColum;
	/** 星期四的格子 */
	protected TextView thrusColum;
	/** 星期五的格子 */
	protected TextView friColum;
	/** 星期六的格子 */
	protected TextView satColum;
	/** 星期日的格子 */
	protected TextView sunColum;
	/** 课程表body部分布局 */
	protected RelativeLayout course_table_layout;// test_course_rlke
	protected RelativeLayout course_table_layout_add;
	/** 屏幕宽度 **/
	protected int screenWidth;
	/** 课程格子平均宽度 **/
	protected int aveWidth;

	protected int shixunhei;
	int height;
	int gridHeight;
	private static StudentDao dao;
	List<Kebiao> infos;
	String[] shixunArr;
	private static final String TAG = "KebiaoFragment";
	String logintype = "";
	public int zhouci = 1;// �����ܴ�,0��ʾ����
	int dangqianzhou = 0;// ���Ƶ�ǰ��
	int kechengTVid = 801;
	public Spinner head_sp;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private String fzvip="0";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void update() {
		handler.sendEmptyMessage(1);
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what!=0&&fzvip.equals("0"))//����ȫ��ͬʱ���ǻ�Ա
			{
				showdia();
			}
			else
				updateKB(msg.what);
		};
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}
	public  void showdia()
	{
		String str="  ͬѧ����ã�\n   Ϊ�˲��ٳ��ַ��ܵ��µĲ���α������ʾ�������Ʒ����С�" +
				"���迪ͨ���Ⱥ˶��Լ����˿α�������ͨ��֧����ת�˹��ܣ�ת1ԪǮ�ڲ�ѵ�֧�����˺ţ�1341156974@qq.com������ע�Լ���ѧ�ţ�ת�˺���ȴ�һЩʱ�䡣�������ޡ�";
		com.fyzs.tool.CustomDialog.Builder builder = new com.fyzs.tool.CustomDialog.Builder(
				KebiaoFragment.this.getActivity());
		builder.setMessage(str);
		builder.setTitle("�����ڲ⹫��");
		builder.setPositiveButton("ȷ��������",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int which) {
						dialog.dismiss();
						ClipboardManager cmb = (ClipboardManager) KebiaoFragment.this.getActivity() .getSystemService(Context.CLIPBOARD_SERVICE);
						//cmb.setText("");
						cmb.setText("1341156974@qq.com");
						Toast.makeText(KebiaoFragment.this.getActivity() , "�����˺ųɹ�", Toast.LENGTH_LONG).show();
						// ������Ĳ�������
					}
				});
		builder.create().show();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_kebiao, null);
		dao = new StudentDao(KebiaoFragment.this.getActivity());
		infos = dao.findAllKebiao();
		dangqianzhou = DateUtils.getWeek(); //BUG
		//dangqianzhou =0;
		System.out.println("�ܣ�������" + dangqianzhou);
		shixunhei = infos.size() - 5;
		try {
			shixunArr = new String[shixunhei];
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(KebiaoFragment.this.getActivity(), "δ��ȡ������", 0)
					.show();
		}
		head_sp = (Spinner) view.findViewById(R.id.head_sp);
		list.add("�鿴ȫ��");
		SharedPreferences sp = KebiaoFragment.this.getActivity().getSharedPreferences("StuData", 0);
		SharedPreferences.Editor ed=sp.edit();

		fzvip=sp.getString("fzvip","0");
		Log.d(TAG, "onCreateView: "+fzvip);
		if(fzvip.equals("1"))
		{
			// �����ܴ�
			for (int i = 1; i <= 26; i++) {
				if (i == dangqianzhou) {
					list.add("��" + i + "�ܣ����ܣ�");
				} else
					list.add("��" + i + "��");
			}
		}
		else {
			list.add("��������");

		}

		setSpinner();
		if (sp.getBoolean("FirFZ",true)) {
			head_sp.setSelection(1);
			//showdia();
			ed.putBoolean("FirFZ",false);
		}
		ed.commit();
		System.out.println(infos.size());
		empty = (TextView) view.findViewById(R.id.test_empty);
		monColum = (TextView) view.findViewById(R.id.test_monday_course);
		tueColum = (TextView) view.findViewById(R.id.test_tuesday_course);
		wedColum = (TextView) view.findViewById(R.id.test_wednesday_course);
		thrusColum = (TextView) view.findViewById(R.id.test_thursday_course);
		friColum = (TextView) view.findViewById(R.id.test_friday_course);
		satColum = (TextView) view.findViewById(R.id.test_saturday_course);
		sunColum = (TextView) view.findViewById(R.id.test_sunday_course);
		course_table_layout = (RelativeLayout) view
				.findViewById(R.id.test_course_rl);
		course_table_layout_add = (RelativeLayout) view
				.findViewById(R.id.test_course_rlke);

		updateKB(dangqianzhou);

		return view;
	}

	private void setSpinner() {
		adapter = new ArrayAdapter<String>(KebiaoFragment.this.getActivity(),
				R.layout.myspinner, list);
		// new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item,list);
		adapter.setDropDownViewResource(R.layout.myspinner);
		head_sp.setAdapter(adapter);
		if(fzvip.equals("1")) {
			//head_sp.setSelection(dangqianzhou);// Ĭ�ϵ�ǰ��
		}else
			dangqianzhou=0;
		head_sp.setSelection(dangqianzhou);// Ĭ�ϵ�ǰ��
		head_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				// ����ѡ���
				handler.sendEmptyMessage(arg2);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 *
	 * @param str
	 *            �γ�����
	 * @param w
	 *            �ڼ��ڿο�ʼ 1 ��1-2
	 * @param h
	 *            ���ڼ�
	 */
	public void createkecheng(final String str, int w, int h, int color) {// 五种颜色的背�?
		int[] background = { R.drawable.course_info_red,
				R.drawable.course_info_green, R.drawable.course_info_blue,
				R.drawable.course_info_light_grey,
				R.drawable.course_info_yellow,
				R.drawable.course_info_light_grey,
				R.drawable.course_info_yellow };
		// 添加课程信息

		final String str3 = str;
		TextView courseInfo = new TextView(KebiaoFragment.this.getActivity());
		courseInfo.setText(str);
		// 该textview的高度根据其节数的跨度来设置
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				aveWidth * 31 / 32, (gridHeight - 5) * 2);
		// textview的位置由课程�?��节数和上课的时间（day of week）确�?
		rlp.topMargin = 5 + (w - 1) * gridHeight;
		rlp.leftMargin = 1;
		// System.out.println(5 + (2 - 1) * gridHeight1 + " " + aveWidth * 31 /
		// 32
		// + " " + (gridHeight1 - 5) * 3);
		// 偏移由这节课是星期几决定
		rlp.addRule(RelativeLayout.RIGHT_OF, h);
		// 字体剧中
		courseInfo.setGravity(Gravity.CENTER);//
		courseInfo.setId(kechengTVid);
		kechengTVid++;
		// 设置�?��背景
		courseInfo.setBackgroundResource(background[color]);

		courseInfo.setTextSize(12);
		courseInfo.setLayoutParams(rlp);
		courseInfo.setTextColor(Color.WHITE);
		courseInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				com.fyzs.tool.CustomDialog.Builder builder = new com.fyzs.tool.CustomDialog.Builder(
						KebiaoFragment.this.getActivity());

				builder.setMessage(str3);
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
		// 设置不�?明度
		courseInfo.getBackground().setAlpha(222);
		course_table_layout.addView(courseInfo);

	}

	public String kcshujuchuli(String str)// �γ̵����ݴ���
	{
		String[] str214 = str.split(" ");

		int huiche = 4;// ��ʦ����ÿ�������

		System.out.println(logintype);
		if (!logintype.equals("ѧ��")) {
			huiche = 5;
		}
		String[] str1 = new String[100];
		int kzd4=1; //���Ƶ�4
		int count=0;
		boolean isduospace=false;
		for(int i=0;i<str214.length;i++,count++)
		{

			if(kzd4==huiche)
			{
				if(!((str214[i].charAt(0)>='0'&&str214[i].charAt(0)<='9')||str214[i].charAt(0)=='��'||str214[i].charAt(0)=='��'||str214[i].charAt(0)=='��'))
				{
					str1[count]="��";
					//count++;
					i--;
				}
				else
					str1[count]=str214[i];
				kzd4=1;
			}
			else if (kzd4==2)
			{
				if (isduospace)
				{
					count--;
					str1[count]+=str214[i];
					//kzd4++;
					isduospace=false;
				}
				else if (!((str214[i].charAt(0)=='��')||str214[i].charAt(0)=='{'))
				{
					str1[count]=str214[i];
					count--;i--;
					isduospace=true;
					continue;
				}
				else
				{
					str1[count]=str214[i];
					kzd4++;
				}
			}
			else
			{
				str1[count]=str214[i];
				kzd4++;
			}

		}
		if (count%huiche!=0)
		{
			str1[count]="��";
			count++;
		}
        for(int i=0;i<count;i++)
			Log.d(TAG, "kcshujuchuli: "+str1[i]);
			//String[] str1 = str.split(" ");
		String newstr = "";

		SharedPreferences sp = KebiaoFragment.this.getActivity()
				.getSharedPreferences("StuData", 0);
		String type = sp.getString("logintype", "");
		System.out.println("type===" + count + ":" + str1);

		for (int i = 0; i < count; i++)// ȥ�����α�־
		{

			if (type.equals("��ʦ")) {
				if (str1[i].length() > 3
						&& str1[i].substring(0, 3).equals("(����")
						|| str1[i].substring(0, 3).equals("(ͣ��")) {
					System.out.println("tiake" + str1[i]);
				} else {
					newstr += str1[i] + " ";
				}
			} else {
				newstr += str1[i] + " ";
			}

		}
		System.out.println("length:" + count + newstr);
		// ���ܴ���ʾ
		String xianshi = "";

		String str2 = "";
		System.out.println("----11" + newstr);
		str1 = newstr.split(" ");
		System.out.println(str1[1]);
		String s3 = str1[1].substring(0, 1);
		System.out.println(s3);
		String s4;
		String[] str3;
		String[] str4;
		if (!s3.equals("{"))
			if (!s3.equals("��")) {
				int x;
				str3 = str.split("��");
				System.out.println(str3.length);
				if (str3.length == 3) {
					str4 = new String[4];
					str4[0] = str3[0];
					str4[1] = "��" + str3[1] + "��}";
					String[] s5 = str3[2].split(" ");
					str4[2] = s5[1];
					str4[3] = s5[2];
					str1 = str4;
					for (int i = 0; i < 8; i++)
						System.out.println(str4[i]);
				} else if (str3.length == 5) {
					str4 = new String[8];
					str4[0] = str3[0];
					str4[1] = "��" + str3[1] + "��}";
					String[] s5 = str3[2].split(" ");
					str4[2] = s5[1];
					str4[3] = s5[2];
					str4[4] = "";
					for (int i = 3; i < s5.length; i++) {
						str4[4] += s5[i];
					}
					str4[5] = "��" + str3[3] + "��}";
					String[] s6 = str3[4].split(" ");
					str4[6] = s6[1];
					str4[7] = s6[2];
					str1 = str4;
					for (int i = 0; i < 8; i++)
						System.out.println(str4[i]);
				} else if (str3.length == 7) {
					str4 = new String[12];
					str4[0] = str3[0];
					str4[1] = "��" + str3[1] + "��}";
					String[] s5 = str3[2].split(" ");
					str4[2] = s5[1];
					str4[3] = s5[2];
					str4[4] = "";
					for (int i = 3; i < s5.length; i++) {
						str4[4] += s5[i];
					}
					str4[5] = "��" + str3[3] + "��}";
					String[] s6 = str3[4].split(" ");
					str4[6] = s6[1];
					str4[7] = s6[2];
					str4[8] = "";
					for (int i = 3; i < s6.length; i++) {
						str4[8] += s5[i];
					}
					str4[9] = "��" + str3[5] + "��}";
					String[] s7 = str3[6].split(" ");
					str4[10] = s7[1];
					str4[11] = s7[2];

					str1 = str4;
					for (int i = 0; i < 12; i++)
						System.out.println(str4[i]);
				}
				// for (int i = 0; i < count; i++)//
				// {
				// s4=str1[i].substring(0, 2);
				// if(s4.equals("��"))
				// {
				// x=i;
				// break;
				// }
				// }

			}

		if (zhouci == 0) {
			for (int x = 0; x < count; x++) {
				if ((x + 1) % huiche == 0 && (x + 1) != count) {
					str2 += str1[x] + "\n\n";
				} else if ((x + 1) != count)
					str2 += str1[x] + "\n";
				else
					str2 += str1[x];
			}
			return str2;
		}
		//
		try {


		if (count == 3)// һ����Ϣ
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[1] + "\n" + str1[2];// ��ʦ��4
			}
		}


		if (count == huiche)// һ����Ϣ
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// ��ʦ��4
			}
		} else if (count == 2 * huiche)// ������Ϣ
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// ��ʦ��4
			}
		} else if (count == 3 * huiche)// 3����Ϣ
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// ��ʦ��4
			}
		} else if (count == 4 * huiche)// 4����Ϣ
		{
			System.out.println("444444444444444");
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// ��ʦ��4

			}
		} else if (count == 5 * huiche)// 5����Ϣ
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5]+ "\n";// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 4 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[20] + "\n" + str1[23] + "\n"
						+ str1[24] : str1[16] + "\n" + str1[18] + "\n"
						+ str1[19]+ "\n" + str1[17];// ��ʦ��4

			}
		} else if (count == 6 * huiche)// 6����Ϣ
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 4 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[20] + "\n" + str1[23] + "\n"
						+ str1[24] : str1[16] + "\n" + str1[18] + "\n"
						+ str1[19]+ "\n" + str1[17];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 5 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[25] + "\n" + str1[28] + "\n"
						+ str1[29] : str1[20] + "\n" + str1[22] + "\n"
						+ str1[23]+ "\n" + str1[21];// ��ʦ��4

			}

		} else if (count == 7 * huiche)// 7����Ϣ
		{
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 4 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[20] + "\n" + str1[23] + "\n"
						+ str1[24] : str1[16] + "\n" + str1[18] + "\n"
						+ str1[19]+ "\n" + str1[17];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 5 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[25] + "\n" + str1[28] + "\n"
						+ str1[29] : str1[20] + "\n" + str1[22] + "\n"
						+ str1[23]+ "\n" + str1[21];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 6 * huiche])) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				xianshi = type.equals("��ʦ") ? str1[30] + "\n" + str1[33] + "\n"
						+ str1[34] : str1[24] + "\n" + str1[26] + "\n"
						+ str1[27]+ "\n" + str1[25];// ��ʦ��4

			}

		}else if (count == 8 * huiche)// 8����Ϣ
		{
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("��ʦ") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// ��ʦ��4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 4 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[20] + "\n" + str1[23] + "\n"
						+ str1[24] : str1[16] + "\n" + str1[18] + "\n"
						+ str1[19]+ "\n" + str1[17];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 5 * huiche])) {
				xianshi = type.equals("��ʦ") ? str1[25] + "\n" + str1[28] + "\n"
						+ str1[29] : str1[20] + "\n" + str1[22] + "\n"
						+ str1[23]+ "\n" + str1[21];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 6 * huiche])) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				xianshi = type.equals("��ʦ") ? str1[30] + "\n" + str1[33] + "\n"
						+ str1[34] : str1[24] + "\n" + str1[26] + "\n"
						+ str1[27]+ "\n" + str1[25];// ��ʦ��4

			}
			if (zhoucichuli(str1[1 + 7 * huiche])) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				xianshi = type.equals("��ʦ") ? str1[35] + "\n" + str1[38] + "\n"
						+ str1[39] : str1[28] + "\n" + str1[30] + "\n"
						+ str1[31]+ "\n" + str1[29];// ��ʦ��4

			}
		}
		System.out.println("��ʾ��" + xianshi);
		return xianshi;

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(KebiaoFragment.this.getActivity(),"ע���д���γ��뷴��",Toast.LENGTH_LONG).show();

			for (int x = 0; x < count; x++) {
				if ((x + 1) % huiche == 0 && (x + 1) != count) {
					str2 += str1[x] + "\n\n";
				} else if ((x + 1) != count)
					str2 += str1[x] + "\n";
				else
					str2 += str1[x];
			}
			return str2;
		}
	}

	public boolean zhoucichuli(String str)// ������Ҫ������ֶ� ��8��10-16
	{
		if (logintype.equals("��ʦ")) {

			boolean xianshi = false;
			String[] fzarr = str.substring(0, str.length() - 5).split(",");
			System.out.println(str.substring(0, str.length() - 5) + "  "
					+ fzarr[0]);
			for (int i = 0; i < fzarr.length; i++) {
				if (fzarr[i].contains("��"))// ����-
				{
					String[] fzarr0 = fzarr[i].split("��");
					System.out.println("??????" + fzarr0[0]);
					String[] fzarr1 = fzarr0[0].split("-");
					System.out.println(fzarr0[1]);
					System.out.println(fzarr1[0]);
					int startindex = Integer.parseInt(fzarr1[0]);
					int lastindex = Integer.parseInt(fzarr1[1]);
					String s1 = fzarr0[1].substring(1, 2);
					if (startindex <= zhouci && lastindex >= zhouci)// �Ƿ���������
					{
						if (s1.equals("��") && zhouci % 2 != 0)
							xianshi = true;
						else if (s1.equals("˫") && zhouci % 2 == 0)
							xianshi = true;
						else {
							xianshi = true;
						}
					}
				} else if (fzarr[i].contains("-"))// ����-
				{
					String[] fzarr1 = fzarr[i].split("-");
					int startindex = Integer.parseInt(fzarr1[0]);
					int lastindex = Integer.parseInt(fzarr1[1]);
					if (startindex <= zhouci && lastindex >= zhouci)// �Ƿ���������
					{
						xianshi = true;
					}
				} else {
					if (Integer.parseInt(fzarr[i]) == zhouci)// 8��10-16
					// �Ƿ�8=zhouci
					{
						xianshi = true;
					}
				}
			}
			return xianshi;
		} else {
			boolean xianshi = false;
			System.out.println(str);
			String cl = str.split("��")[str.split("��").length - 1];// 1-4��
			String cl1 = cl.substring(0, cl.length() - 2);// 1-4
			System.out.println(cl1);

			if (cl1.contains("��"))// ����-
			{
				String[] fzarr0 = cl1.split("��");
				System.out.println("??????" + fzarr0[0]);
				String[] fzarr1 = fzarr0[0].split("-");
				System.out.println(fzarr0[1]);
				System.out.println(fzarr1[0]);
				int startindex = Integer.parseInt(fzarr1[0]);
				int lastindex = Integer.parseInt(fzarr1[1]);
				String s1 = fzarr0[1].substring(1, 2);
				if (startindex <= zhouci && lastindex >= zhouci)// �Ƿ���������
				{
					if (s1.equals("��") && zhouci % 2 != 0)
						xianshi = true;
					else if (s1.equals("˫") && zhouci % 2 == 0)
						xianshi = true;
					else {
						xianshi = true;
					}
				}
			} else if (cl1.contains("-"))// ����-
			{
				String[] fzarr1 = cl1.split("-");
				int startindex = Integer.parseInt(fzarr1[0]);
				int lastindex = Integer.parseInt(fzarr1[1]);
				if (startindex <= zhouci && lastindex >= zhouci)// �Ƿ���������
				{
					xianshi = true;
				}
			} else {
				if (Integer.parseInt(cl1) == zhouci)// 8��10-16
				// �Ƿ�8=zhouci
				{
					xianshi = true;
				}
			}

			return xianshi;
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

	private class myAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shixunhei;
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

			View view = convertView.inflate(KebiaoFragment.this.getActivity(),
					R.layout.list_chengji, null);
			Kebiao ke = infos.get(position + 5);
			System.out.println(ke.getTime() + "-------");
			TextView tv1 = (TextView) view.findViewById(R.id.tv1);
			TextView tv2 = (TextView) view.findViewById(R.id.tv2);
			TextView tv3 = (TextView) view.findViewById(R.id.tv3);
			tv1.setText(ke.getTime());
			tv2.setText(ke.getMonday());
			tv3.setText(ke.getWednesday());
			SharedPreferences sp = KebiaoFragment.this.getActivity()
					.getSharedPreferences("StuData", 0);
			String type = sp.getString("logintype", "ѧ��");
			System.out.println(type);
			if (type.equals("ѧ��")) {
				shixunArr[position] = "�γ����ƣ�" + ke.getTime() + "\n" + "��ʦ��"
						+ ke.getMonday() + "\n" + "ѧ�֣�" + ke.getTuesday()
						+ "\n" + "��ֹ�ܣ�" + ke.getWednesday() + "\n" + "�Ͽ�ʱ�䣺"
						+ ke.getThursday() + "\n" + "�Ͽεص㣺" + ke.getFriday();
			} else {
				tv2.setText(ke.getSaturated());
				shixunArr[position] = "�γ����ƣ�" + ke.getTime() + "\n" + "��ʦ��"
						+ ke.getMonday() + "\n" + "ѧ�֣�" + ke.getTuesday()
						+ "\n" + "��ֹ�ܣ�" + ke.getWednesday() + "\n" + "�Ͽ�ʱ�䣺"
						+ ke.getThursday() + "\n" + "�Ͽεص㣺" + ke.getFriday()
						+ "\n" + "��ѧ����ɣ�" + ke.getSaturated();
			}

			return view;
		}

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

	@SuppressLint("ResourceAsColor")
	public void updateKB(int index) {
		kechengTVid = 801;
		course_table_layout.removeAllViews();// ɾ������view
		zhouci = index;

		int x = 1;
		infos = dao.findAllKebiao();
		DisplayMetrics dm = new DisplayMetrics();
		KebiaoFragment.this.getActivity().getWindowManager()
				.getDefaultDisplay().getMetrics(dm);
		// 屏幕宽度
		int width = dm.widthPixels;
		// 平均宽度
		int aveWidth = width / 8;
		// 第一个空白格子设置为25�?
		empty.setWidth(aveWidth * 3 / 4);
		monColum.setWidth(aveWidth * 33 / 32 + 1);
		tueColum.setWidth(aveWidth * 33 / 32 + 1);
		wedColum.setWidth(aveWidth * 33 / 32 + 1);
		thrusColum.setWidth(aveWidth * 33 / 32 + 1);
		friColum.setWidth(aveWidth * 33 / 32 + 1);
		satColum.setWidth(aveWidth * 33 / 32 + 1);
		sunColum.setWidth(aveWidth * 33 / 32 + 1);
		empty.setHeight(gridHeight - 10);
		monColum.setHeight(gridHeight - 10);
		tueColum.setHeight(gridHeight - 10);
		wedColum.setHeight(gridHeight - 10);
		thrusColum.setHeight(gridHeight - 10);
		friColum.setHeight(gridHeight - 10);
		satColum.setHeight(gridHeight - 10);
		sunColum.setHeight(gridHeight - 10);
		if (zhouci == DateUtils.getWeek()) {
			int monday = DateUtils.getDYrq();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// �������ڸ�ʽ
			String y = df.format(new Date()).split("-")[1];
			String d = df.format(new Date()).split("-")[2];
			if (monday <= 0) {
				empty.setText(Integer.parseInt(y) - 1 + "��");
				monColum.setText(DateUtils.getduiying(monday, 0,
						Integer.parseInt(y) - 1)
						+ "\n��һ");
				tueColum.setText(DateUtils.getduiying(monday, 1,
						Integer.parseInt(y) - 1)
						+ "\n�ܶ�");
				wedColum.setText(DateUtils.getduiying(monday, 2,
						Integer.parseInt(y) - 1)
						+ "\n����");
				thrusColum.setText(DateUtils.getduiying(monday, 3,
						Integer.parseInt(y) - 1)
						+ "\n����");
				friColum.setText(DateUtils.getduiying(monday, 4,
						Integer.parseInt(y) - 1)
						+ "\n����");
				satColum.setText(DateUtils.getduiying(monday, 5,
						Integer.parseInt(y) - 1)
						+ "\n����");
				sunColum.setText(DateUtils.getduiying(monday, 6,
						Integer.parseInt(y) - 1)
						+ "\n����");

			} else {
				empty.setText(Integer.parseInt(y) + "��");
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");// �������ڸ�ʽ
				String y1 = df1.format(new Date()).split("-")[0];
				int year = Integer.parseInt(y1);

				int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
						31 };
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
					monthDay[1]++;

				int lastday = monthDay[Integer.parseInt(y) - 1];
				if ((monday + 6) > lastday) {
					monColum.setText(DateUtils.getduiying1(monday, 0,
							Integer.parseInt(y) - 1, lastday)
							+ "\n��һ");
					tueColum.setText(DateUtils.getduiying1(monday, 1,
							Integer.parseInt(y) - 1, lastday)
							+ "\n�ܶ�");
					wedColum.setText(DateUtils.getduiying1(monday, 2,
							Integer.parseInt(y) - 1, lastday)
							+ "\n����");
					thrusColum.setText(DateUtils.getduiying1(monday, 3,
							Integer.parseInt(y) - 1, lastday)
							+ "\n����");
					friColum.setText(DateUtils.getduiying1(monday, 4,
							Integer.parseInt(y) - 1, lastday)
							+ "\n����");
					satColum.setText(DateUtils.getduiying1(monday, 5,
							Integer.parseInt(y) - 1, lastday)
							+ "\n����");
					sunColum.setText(DateUtils.getduiying1(monday, 6,
							Integer.parseInt(y) - 1, lastday)
							+ "\n����");
				} else {
					monColum.setText(monday + "\n��һ");
					tueColum.setText(monday + 1 + "\n�ܶ�");
					wedColum.setText(monday + 2 + "\n����");
					thrusColum.setText(monday + 3 + "\n����");
					friColum.setText(monday + 4 + "\n����");
					satColum.setText(monday + 5 + "\n����");
					sunColum.setText(monday + 6 + "\n����");
				}

			}

		} else {

			empty.setText("");
			monColum.setText("��һ");
			tueColum.setText("�ܶ�");
			wedColum.setText("����");
			thrusColum.setText("����");
			friColum.setText("����");
			satColum.setText("����");
			sunColum.setText("����");
		}

		int WeekDay = DateUtils.getWeekDay();
		switch (WeekDay) {
			case 1:
				monColum.setBackgroundColor(R.color.blue);
				break;
			case 2:
				tueColum.setBackgroundColor(R.color.blue);
				break;
			case 3:
				wedColum.setBackgroundColor(R.color.blue);
				break;
			case 4:
				thrusColum.setBackgroundColor(R.color.blue);
				break;
			case 5:
				friColum.setBackgroundColor(R.color.blue);
				break;
			case 6:
				satColum.setBackgroundColor(R.color.blue);
				break;
			case 7:
				sunColum.setBackgroundColor(R.color.blue);
				break;
			default:
				break;
		}

		this.screenWidth = width;
		this.aveWidth = aveWidth;
		height = dm.heightPixels;
		gridHeight = height / 12;

		// ���ÿα����
		// ��̬����12 * maxCourseNum��textview
		for (int i = 1; i <= 10; i++) {

			for (int j = 1; j <=8; j++) {

				TextView tx = new TextView(KebiaoFragment.this.getActivity());
				tx.setId((i - 1) * 8 + j);
				// �������һ�У���ʹ��course_text_view_bg���������һ��û���ұ߿�
				if (j < 8)
					tx.setBackgroundDrawable(KebiaoFragment.this.getActivity()
							.getResources()
							.getDrawable(R.drawable.course_text_view_bg));
				else
					tx.setBackgroundDrawable(KebiaoFragment.this.getActivity()
							.getResources()
							.getDrawable(R.drawable.course_table_last_colum));
				// 相对布局参数
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
						aveWidth * 33 / 32 + 1, gridHeight);
				// 文字对齐方式
				tx.setGravity(Gravity.CENTER);
				// 字体样式
				tx.setTextAppearance(KebiaoFragment.this.getActivity(),
						R.style.courseTableText);
				// 如果是第�?��，需要设置课的序号（1 �?12�?
				if (j == 1) {
					tx.setText(String.valueOf(i));
					rp.width = aveWidth * 3 / 4;
					// 设置他们的相对位�?
					if (i == 1)
						rp.addRule(RelativeLayout.BELOW, empty.getId());
					else
						rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
				} else {
					rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
					rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
					tx.setText("");
				}

				tx.setLayoutParams(rp);
				course_table_layout.addView(tx);
			}
		}
		try {
			TextView tx = new TextView(KebiaoFragment.this.getActivity());
			tx.setId(87);
			tx.setText("ʵѵ/����/�����γ���Ϣ��");
			RelativeLayout.LayoutParams rp1 = new RelativeLayout.LayoutParams(
					width, gridHeight);
			rp1.addRule(RelativeLayout.BELOW, (11 - 1) * 8);
			tx.setTextSize(25);
			// 文字对齐方式
			tx.setGravity(Gravity.CENTER);
			// 字体样式
			tx.setTextAppearance(KebiaoFragment.this.getActivity(),
					R.style.courseTableText);
			tx.setLayoutParams(rp1);
			course_table_layout.addView(tx);

			View v = new View(KebiaoFragment.this.getActivity());
			View v1 = v.inflate(KebiaoFragment.this.getActivity(),
					R.layout.kebiao_shixun, null);
			TextView tv2 = (TextView) v1.findViewById(R.id.tv2);
			SharedPreferences sp = KebiaoFragment.this.getActivity()
					.getSharedPreferences("StuData", 0);
			logintype = sp.getString("logintype", "ѧ��");
			System.out.println(logintype);
			if (!logintype.equals("ѧ��")) {
				tv2.setText("�༶");
			}

			v1.setId(88);
			RelativeLayout.LayoutParams rp2 = new RelativeLayout.LayoutParams(
					width, gridHeight);
			rp2.addRule(RelativeLayout.BELOW, 87);
			v1.setLayoutParams(rp2);
			course_table_layout.addView(v1);

			// ʵѵ

			ListView tv = new ListView(KebiaoFragment.this.getActivity());
			tv.setId(89);
			tv.setAdapter(new myAdapter());
			tv.setOnItemClickListener(new OnItemClickListener() {// ����¼�

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					// TODO Auto-generated method stub
					com.fyzs.tool.ChengjiDialog.Builder builder = new com.fyzs.tool.ChengjiDialog.Builder(
							KebiaoFragment.this.getActivity());
					builder.setMessage(shixunArr[position]);
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

			System.out.println(tv.getHeight() + " " + gridHeight * shixunhei);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					width, gridHeight * (shixunhei + 1));
			rp.addRule(RelativeLayout.BELOW, 88);
			tv.setLayoutParams(rp);
			course_table_layout.addView(tv);

			// TextView tx1 = new TextView(KebiaoFragment.this.getActivity());
			// tx1.setId(90);
			// //tx1.setText("ʵѵ/����/�����γ���Ϣ��");
			// RelativeLayout.LayoutParams rp3 = new
			// RelativeLayout.LayoutParams(
			// width, gridHeight);
			// rp3.addRule(RelativeLayout.BELOW, 89);
			// tx1.setTextSize(25);
			// // 文字对齐方式
			// tx1.setGravity(Gravity.CENTER);
			// // 字体样式
			// tx1.setTextAppearance(KebiaoFragment.this.getActivity(),
			// R.style.courseTableText);
			// tx1.setLayoutParams(rp3);
			// course_table_layout.addView(tx1);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(KebiaoFragment.this.getActivity(), "����ϵͳ�ѹرտα��ѯ1", 1)
					.show();
		}
		try {

			for (int i = 0; i < 5; i++) {
				Kebiao ke = infos.get(i);
				System.out.println(infos.size());
				System.out.println(ke.getMonday() + "!!!!");
				if (ke.getMonday().length()<2) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					String strxianshi = kcshujuchuli(ke.getMonday());
					if (!strxianshi.equals("")) {
						createkecheng(strxianshi, x, 1, 1);
					}

				}
				if (ke.getTuesday().length()<2) {
					// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					String strxianshi = kcshujuchuli(ke.getTuesday());
					if (!strxianshi.equals("")) {
						createkecheng(strxianshi, x, 2, 2);
					}

				}
				if (ke.getWednesday().length()<2) {
					// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					String strxianshi = kcshujuchuli(ke.getWednesday());
					if (!strxianshi.equals("")) {
						createkecheng(strxianshi, x, 3, 3);
					}

				}
				if (ke.getThursday().length()<2) {
					// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					String strxianshi = kcshujuchuli(ke.getThursday());
					if (!strxianshi.equals("")) {
						createkecheng(strxianshi, x, 4, 4);
					}

				}
				if (ke.getFriday().length()<2) {
					// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					String strxianshi = kcshujuchuli(ke.getFriday());
					if (!strxianshi.equals("")) {
						createkecheng(strxianshi, x, 5, 0);
					}

				}
				if (ke.getSaturated().length()<2) {
					 System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					Log.d(TAG, "updateKB:"+ke.getSaturated());
					String strxianshi = kcshujuchuli(ke.getSaturated());
					if (!strxianshi.equals("")) {
						createkecheng(strxianshi, x, 6, 1);
					}

				}

				if (ke.getSunday().length()<2) {
					// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				} else {
					String strxianshi = kcshujuchuli(ke.getSunday());
					if (!strxianshi.equals("")) {
						createkecheng(strxianshi, x, 7, 2);
					}

				}

				x = x + 2;
			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(KebiaoFragment.this.getActivity(), "����ϵͳ�ѹرտα��ѯ2", 1)
					.show();
		}
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

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
 * @description 浠婃棩
 */
@SuppressWarnings("ResourceType")
public class KebiaoFragment extends Fragment {
	/** 绗竴涓棤鍐呭鐨勬牸瀛? */
	protected TextView empty;
	/** 鏄熸湡涓?殑鏍煎瓙 */
	protected TextView monColum;
	/** 鏄熸湡浜岀殑鏍煎瓙 */
	protected TextView tueColum;
	/** 鏄熸湡涓夌殑鏍煎瓙 */
	protected TextView wedColum;
	/** 鏄熸湡鍥涚殑鏍煎瓙 */
	protected TextView thrusColum;
	/** 鏄熸湡浜旂殑鏍煎瓙 */
	protected TextView friColum;
	/** 鏄熸湡鍏殑鏍煎瓙 */
	protected TextView satColum;
	/** 鏄熸湡鏃ョ殑鏍煎瓙 */
	protected TextView sunColum;
	/** 璇剧▼琛╞ody閮ㄥ垎甯冨眬 */
	protected RelativeLayout course_table_layout;// test_course_rlke
	protected RelativeLayout course_table_layout_add;
	/** 灞忓箷瀹藉害 **/
	protected int screenWidth;
	/** 璇剧▼鏍煎瓙骞冲潎瀹藉害 **/
	protected int aveWidth;

	protected int shixunhei;
	int height;
	int gridHeight;
	private static StudentDao dao;
	List<Kebiao> infos;
	String[] shixunArr;
	private static final String TAG = "KebiaoFragment";
	String logintype = "";
	public int zhouci = 1;// 控制周次,0表示所有
	int dangqianzhou = 0;// 控制当前周
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
			if (msg.what!=0&&fzvip.equals("0"))//不是全部同时不是会员
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
		String str="  同学，你好！\n   为了不再出现分周导致的差错，课表分周显示功能限制发放中。" +
				"如需开通请先核对自己个人课表无误，再通过支付宝转账功能，转1元钱内测费到支付宝账号：1341156974@qq.com，并备注自己的学号，转账后请等待一些时间。名额有限。";
		com.fyzs.tool.CustomDialog.Builder builder = new com.fyzs.tool.CustomDialog.Builder(
				KebiaoFragment.this.getActivity());
		builder.setMessage(str);
		builder.setTitle("分周内测公告");
		builder.setPositiveButton("确定并复制",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int which) {
						dialog.dismiss();
						ClipboardManager cmb = (ClipboardManager) KebiaoFragment.this.getActivity() .getSystemService(Context.CLIPBOARD_SERVICE);
						//cmb.setText("");
						cmb.setText("1341156974@qq.com");
						Toast.makeText(KebiaoFragment.this.getActivity() , "复制账号成功", Toast.LENGTH_LONG).show();
						// 设置你的操作事项
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
		System.out.println("周！！！！" + dangqianzhou);
		shixunhei = infos.size() - 5;
		try {
			shixunArr = new String[shixunhei];
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(KebiaoFragment.this.getActivity(), "未读取到数据", 0)
					.show();
		}
		head_sp = (Spinner) view.findViewById(R.id.head_sp);
		list.add("查看全部");
		SharedPreferences sp = KebiaoFragment.this.getActivity().getSharedPreferences("StuData", 0);
		SharedPreferences.Editor ed=sp.edit();

		fzvip=sp.getString("fzvip","0");
		Log.d(TAG, "onCreateView: "+fzvip);
		if(fzvip.equals("1"))
		{
			// 控制周次
			for (int i = 1; i <= 26; i++) {
				if (i == dangqianzhou) {
					list.add("第" + i + "周（本周）");
				} else
					list.add("第" + i + "周");
			}
		}
		else {
			list.add("分周限制");

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
			//head_sp.setSelection(dangqianzhou);// 默认当前周
		}else
			dangqianzhou=0;
		head_sp.setSelection(dangqianzhou);// 默认当前周
		head_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				// 下拉选择的
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
	 *            课程内容
	 * @param w
	 *            第几节课开始 1 ：1-2
	 * @param h
	 *            星期几
	 */
	public void createkecheng(final String str, int w, int h, int color) {// 浜旂棰滆壊鐨勮儗鏅?
		int[] background = { R.drawable.course_info_red,
				R.drawable.course_info_green, R.drawable.course_info_blue,
				R.drawable.course_info_light_grey,
				R.drawable.course_info_yellow,
				R.drawable.course_info_light_grey,
				R.drawable.course_info_yellow };
		// 娣诲姞璇剧▼淇℃伅

		final String str3 = str;
		TextView courseInfo = new TextView(KebiaoFragment.this.getActivity());
		courseInfo.setText(str);
		// 璇extview鐨勯珮搴︽牴鎹叾鑺傛暟鐨勮法搴︽潵璁剧疆
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				aveWidth * 31 / 32, (gridHeight - 5) * 2);
		// textview鐨勪綅缃敱璇剧▼寮?鑺傛暟鍜屼笂璇剧殑鏃堕棿锛坉ay of week锛夌‘瀹?
		rlp.topMargin = 5 + (w - 1) * gridHeight;
		rlp.leftMargin = 1;
		// System.out.println(5 + (2 - 1) * gridHeight1 + " " + aveWidth * 31 /
		// 32
		// + " " + (gridHeight1 - 5) * 3);
		// 鍋忕Щ鐢辫繖鑺傝鏄槦鏈熷嚑鍐冲畾
		rlp.addRule(RelativeLayout.RIGHT_OF, h);
		// 瀛椾綋鍓т腑
		courseInfo.setGravity(Gravity.CENTER);//
		courseInfo.setId(kechengTVid);
		kechengTVid++;
		// 璁剧疆涓?鑳屾櫙
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
				builder.setTitle("详细");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.create().show();

			}
		});
		// 璁剧疆涓嶉?鏄庡害
		courseInfo.getBackground().setAlpha(222);
		course_table_layout.addView(courseInfo);

	}

	public String kcshujuchuli(String str)// 课程的数据处理
	{
		String[] str214 = str.split(" ");

		int huiche = 4;// 老师的是每五个换行

		System.out.println(logintype);
		if (!logintype.equals("学生")) {
			huiche = 5;
		}
		String[] str1 = new String[100];
		int kzd4=1; //控制到4
		int count=0;
		boolean isduospace=false;
		for(int i=0;i<str214.length;i++,count++)
		{

			if(kzd4==huiche)
			{
				if(!((str214[i].charAt(0)>='0'&&str214[i].charAt(0)<='9')||str214[i].charAt(0)=='操'||str214[i].charAt(0)=='体'||str214[i].charAt(0)=='东'))
				{
					str1[count]="无";
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
				else if (!((str214[i].charAt(0)=='周')||str214[i].charAt(0)=='{'))
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
			str1[count]="无";
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

		for (int i = 0; i < count; i++)// 去掉调课标志
		{

			if (type.equals("教师")) {
				if (str1[i].length() > 3
						&& str1[i].substring(0, 3).equals("(调课")
						|| str1[i].substring(0, 3).equals("(停课")) {
					System.out.println("tiake" + str1[i]);
				} else {
					newstr += str1[i] + " ";
				}
			} else {
				newstr += str1[i] + " ";
			}

		}
		System.out.println("length:" + count + newstr);
		// 分周次显示
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
			if (!s3.equals("周")) {
				int x;
				str3 = str.split("周");
				System.out.println(str3.length);
				if (str3.length == 3) {
					str4 = new String[4];
					str4[0] = str3[0];
					str4[1] = "周" + str3[1] + "周}";
					String[] s5 = str3[2].split(" ");
					str4[2] = s5[1];
					str4[3] = s5[2];
					str1 = str4;
					for (int i = 0; i < 8; i++)
						System.out.println(str4[i]);
				} else if (str3.length == 5) {
					str4 = new String[8];
					str4[0] = str3[0];
					str4[1] = "周" + str3[1] + "周}";
					String[] s5 = str3[2].split(" ");
					str4[2] = s5[1];
					str4[3] = s5[2];
					str4[4] = "";
					for (int i = 3; i < s5.length; i++) {
						str4[4] += s5[i];
					}
					str4[5] = "周" + str3[3] + "周}";
					String[] s6 = str3[4].split(" ");
					str4[6] = s6[1];
					str4[7] = s6[2];
					str1 = str4;
					for (int i = 0; i < 8; i++)
						System.out.println(str4[i]);
				} else if (str3.length == 7) {
					str4 = new String[12];
					str4[0] = str3[0];
					str4[1] = "周" + str3[1] + "周}";
					String[] s5 = str3[2].split(" ");
					str4[2] = s5[1];
					str4[3] = s5[2];
					str4[4] = "";
					for (int i = 3; i < s5.length; i++) {
						str4[4] += s5[i];
					}
					str4[5] = "周" + str3[3] + "周}";
					String[] s6 = str3[4].split(" ");
					str4[6] = s6[1];
					str4[7] = s6[2];
					str4[8] = "";
					for (int i = 3; i < s6.length; i++) {
						str4[8] += s5[i];
					}
					str4[9] = "周" + str3[5] + "周}";
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
				// if(s4.equals("周"))
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


		if (count == 3)// 一个信息
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[1] + "\n" + str1[2];// 教师加4
			}
		}


		if (count == huiche)// 一个信息
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// 教师加4
			}
		} else if (count == 2 * huiche)// 两个信息
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// 教师加4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("教师") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// 教师加4
			}
		} else if (count == 3 * huiche)// 3个信息
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// 教师加4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("教师") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// 教师加4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("教师") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// 教师加4
			}
		} else if (count == 4 * huiche)// 4个信息
		{
			System.out.println("444444444444444");
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// 教师加4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("教师") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// 教师加4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("教师") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// 教师加4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("教师") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// 教师加4

			}
		} else if (count == 5 * huiche)// 5个信息
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// 教师加4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("教师") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5]+ "\n";// 教师加4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("教师") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// 教师加4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("教师") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// 教师加4

			}
			if (zhoucichuli(str1[1 + 4 * huiche])) {
				xianshi = type.equals("教师") ? str1[20] + "\n" + str1[23] + "\n"
						+ str1[24] : str1[16] + "\n" + str1[18] + "\n"
						+ str1[19]+ "\n" + str1[17];// 教师加4

			}
		} else if (count == 6 * huiche)// 6个信息
		{
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// 教师加4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("教师") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// 教师加4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("教师") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// 教师加4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("教师") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// 教师加4

			}
			if (zhoucichuli(str1[1 + 4 * huiche])) {
				xianshi = type.equals("教师") ? str1[20] + "\n" + str1[23] + "\n"
						+ str1[24] : str1[16] + "\n" + str1[18] + "\n"
						+ str1[19]+ "\n" + str1[17];// 教师加4

			}
			if (zhoucichuli(str1[1 + 5 * huiche])) {
				xianshi = type.equals("教师") ? str1[25] + "\n" + str1[28] + "\n"
						+ str1[29] : str1[20] + "\n" + str1[22] + "\n"
						+ str1[23]+ "\n" + str1[21];// 教师加4

			}

		} else if (count == 7 * huiche)// 7个信息
		{
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// 教师加4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("教师") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// 教师加4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("教师") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// 教师加4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("教师") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// 教师加4

			}
			if (zhoucichuli(str1[1 + 4 * huiche])) {
				xianshi = type.equals("教师") ? str1[20] + "\n" + str1[23] + "\n"
						+ str1[24] : str1[16] + "\n" + str1[18] + "\n"
						+ str1[19]+ "\n" + str1[17];// 教师加4

			}
			if (zhoucichuli(str1[1 + 5 * huiche])) {
				xianshi = type.equals("教师") ? str1[25] + "\n" + str1[28] + "\n"
						+ str1[29] : str1[20] + "\n" + str1[22] + "\n"
						+ str1[23]+ "\n" + str1[21];// 教师加4

			}
			if (zhoucichuli(str1[1 + 6 * huiche])) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				xianshi = type.equals("教师") ? str1[30] + "\n" + str1[33] + "\n"
						+ str1[34] : str1[24] + "\n" + str1[26] + "\n"
						+ str1[27]+ "\n" + str1[25];// 教师加4

			}

		}else if (count == 8 * huiche)// 8个信息
		{
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if (zhoucichuli(str1[1])) {
				xianshi = type.equals("教师") ? str1[0] + "\n" + str1[3] + "\n"
						+ str1[4] : str1[0] + "\n" + str1[2] + "\n" + str1[3]+ "\n" + str1[1];// 教师加4
			}
			if (zhoucichuli(str1[1 + 1 * huiche])) {
				xianshi = type.equals("教师") ? str1[5] + "\n" + str1[8] + "\n"
						+ str1[9] : str1[4] + "\n" + str1[6] + "\n" + str1[7]+ "\n" + str1[5];// 教师加4
			}
			if (zhoucichuli(str1[1 + 2 * huiche])) {
				xianshi = type.equals("教师") ? str1[10] + "\n" + str1[13] + "\n"
						+ str1[14] : str1[8] + "\n" + str1[10] + "\n"
						+ str1[11]+ "\n" + str1[9];// 教师加4

			}
			if (zhoucichuli(str1[1 + 3 * huiche])) {
				xianshi = type.equals("教师") ? str1[15] + "\n" + str1[18] + "\n"
						+ str1[19] : str1[12] + "\n" + str1[14] + "\n"
						+ str1[15]+ "\n" + str1[13];// 教师加4

			}
			if (zhoucichuli(str1[1 + 4 * huiche])) {
				xianshi = type.equals("教师") ? str1[20] + "\n" + str1[23] + "\n"
						+ str1[24] : str1[16] + "\n" + str1[18] + "\n"
						+ str1[19]+ "\n" + str1[17];// 教师加4

			}
			if (zhoucichuli(str1[1 + 5 * huiche])) {
				xianshi = type.equals("教师") ? str1[25] + "\n" + str1[28] + "\n"
						+ str1[29] : str1[20] + "\n" + str1[22] + "\n"
						+ str1[23]+ "\n" + str1[21];// 教师加4

			}
			if (zhoucichuli(str1[1 + 6 * huiche])) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				xianshi = type.equals("教师") ? str1[30] + "\n" + str1[33] + "\n"
						+ str1[34] : str1[24] + "\n" + str1[26] + "\n"
						+ str1[27]+ "\n" + str1[25];// 教师加4

			}
			if (zhoucichuli(str1[1 + 7 * huiche])) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				xianshi = type.equals("教师") ? str1[35] + "\n" + str1[38] + "\n"
						+ str1[39] : str1[28] + "\n" + str1[30] + "\n"
						+ str1[31]+ "\n" + str1[29];// 教师加4

			}
		}
		System.out.println("显示：" + xianshi);
		return xianshi;

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(KebiaoFragment.this.getActivity(),"注意有错误课程请反馈",Toast.LENGTH_LONG).show();

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

	public boolean zhoucichuli(String str)// 传入需要处理的字段 如8，10-16
	{
		if (logintype.equals("教师")) {

			boolean xianshi = false;
			String[] fzarr = str.substring(0, str.length() - 5).split(",");
			System.out.println(str.substring(0, str.length() - 5) + "  "
					+ fzarr[0]);
			for (int i = 0; i < fzarr.length; i++) {
				if (fzarr[i].contains("周"))// 包含-
				{
					String[] fzarr0 = fzarr[i].split("周");
					System.out.println("??????" + fzarr0[0]);
					String[] fzarr1 = fzarr0[0].split("-");
					System.out.println(fzarr0[1]);
					System.out.println(fzarr1[0]);
					int startindex = Integer.parseInt(fzarr1[0]);
					int lastindex = Integer.parseInt(fzarr1[1]);
					String s1 = fzarr0[1].substring(1, 2);
					if (startindex <= zhouci && lastindex >= zhouci)// 是否在区间内
					{
						if (s1.equals("单") && zhouci % 2 != 0)
							xianshi = true;
						else if (s1.equals("双") && zhouci % 2 == 0)
							xianshi = true;
						else {
							xianshi = true;
						}
					}
				} else if (fzarr[i].contains("-"))// 包含-
				{
					String[] fzarr1 = fzarr[i].split("-");
					int startindex = Integer.parseInt(fzarr1[0]);
					int lastindex = Integer.parseInt(fzarr1[1]);
					if (startindex <= zhouci && lastindex >= zhouci)// 是否在区间内
					{
						xianshi = true;
					}
				} else {
					if (Integer.parseInt(fzarr[i]) == zhouci)// 8，10-16
					// 是否8=zhouci
					{
						xianshi = true;
					}
				}
			}
			return xianshi;
		} else {
			boolean xianshi = false;
			System.out.println(str);
			String cl = str.split("第")[str.split("第").length - 1];// 1-4周
			String cl1 = cl.substring(0, cl.length() - 2);// 1-4
			System.out.println(cl1);

			if (cl1.contains("周"))// 包含-
			{
				String[] fzarr0 = cl1.split("周");
				System.out.println("??????" + fzarr0[0]);
				String[] fzarr1 = fzarr0[0].split("-");
				System.out.println(fzarr0[1]);
				System.out.println(fzarr1[0]);
				int startindex = Integer.parseInt(fzarr1[0]);
				int lastindex = Integer.parseInt(fzarr1[1]);
				String s1 = fzarr0[1].substring(1, 2);
				if (startindex <= zhouci && lastindex >= zhouci)// 是否在区间内
				{
					if (s1.equals("单") && zhouci % 2 != 0)
						xianshi = true;
					else if (s1.equals("双") && zhouci % 2 == 0)
						xianshi = true;
					else {
						xianshi = true;
					}
				}
			} else if (cl1.contains("-"))// 包含-
			{
				String[] fzarr1 = cl1.split("-");
				int startindex = Integer.parseInt(fzarr1[0]);
				int lastindex = Integer.parseInt(fzarr1[1]);
				if (startindex <= zhouci && lastindex >= zhouci)// 是否在区间内
				{
					xianshi = true;
				}
			} else {
				if (Integer.parseInt(cl1) == zhouci)// 8，10-16
				// 是否8=zhouci
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
			String type = sp.getString("logintype", "学生");
			System.out.println(type);
			if (type.equals("学生")) {
				shixunArr[position] = "课程名称：" + ke.getTime() + "\n" + "教师："
						+ ke.getMonday() + "\n" + "学分：" + ke.getTuesday()
						+ "\n" + "起止周：" + ke.getWednesday() + "\n" + "上课时间："
						+ ke.getThursday() + "\n" + "上课地点：" + ke.getFriday();
			} else {
				tv2.setText(ke.getSaturated());
				shixunArr[position] = "课程名称：" + ke.getTime() + "\n" + "教师："
						+ ke.getMonday() + "\n" + "学分：" + ke.getTuesday()
						+ "\n" + "起止周：" + ke.getWednesday() + "\n" + "上课时间："
						+ ke.getThursday() + "\n" + "上课地点：" + ke.getFriday()
						+ "\n" + "教学班组成：" + ke.getSaturated();
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

	@SuppressLint("ResourceAsColor")
	public void updateKB(int index) {
		kechengTVid = 801;
		course_table_layout.removeAllViews();// 删除所有view
		zhouci = index;

		int x = 1;
		infos = dao.findAllKebiao();
		DisplayMetrics dm = new DisplayMetrics();
		KebiaoFragment.this.getActivity().getWindowManager()
				.getDefaultDisplay().getMetrics(dm);
		// 灞忓箷瀹藉害
		int width = dm.widthPixels;
		// 骞冲潎瀹藉害
		int aveWidth = width / 8;
		// 绗竴涓┖鐧芥牸瀛愯缃负25瀹?
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
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			String y = df.format(new Date()).split("-")[1];
			String d = df.format(new Date()).split("-")[2];
			if (monday <= 0) {
				empty.setText(Integer.parseInt(y) - 1 + "月");
				monColum.setText(DateUtils.getduiying(monday, 0,
						Integer.parseInt(y) - 1)
						+ "\n周一");
				tueColum.setText(DateUtils.getduiying(monday, 1,
						Integer.parseInt(y) - 1)
						+ "\n周二");
				wedColum.setText(DateUtils.getduiying(monday, 2,
						Integer.parseInt(y) - 1)
						+ "\n周三");
				thrusColum.setText(DateUtils.getduiying(monday, 3,
						Integer.parseInt(y) - 1)
						+ "\n周四");
				friColum.setText(DateUtils.getduiying(monday, 4,
						Integer.parseInt(y) - 1)
						+ "\n周五");
				satColum.setText(DateUtils.getduiying(monday, 5,
						Integer.parseInt(y) - 1)
						+ "\n周六");
				sunColum.setText(DateUtils.getduiying(monday, 6,
						Integer.parseInt(y) - 1)
						+ "\n周日");

			} else {
				empty.setText(Integer.parseInt(y) + "月");
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
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
							+ "\n周一");
					tueColum.setText(DateUtils.getduiying1(monday, 1,
							Integer.parseInt(y) - 1, lastday)
							+ "\n周二");
					wedColum.setText(DateUtils.getduiying1(monday, 2,
							Integer.parseInt(y) - 1, lastday)
							+ "\n周三");
					thrusColum.setText(DateUtils.getduiying1(monday, 3,
							Integer.parseInt(y) - 1, lastday)
							+ "\n周四");
					friColum.setText(DateUtils.getduiying1(monday, 4,
							Integer.parseInt(y) - 1, lastday)
							+ "\n周五");
					satColum.setText(DateUtils.getduiying1(monday, 5,
							Integer.parseInt(y) - 1, lastday)
							+ "\n周六");
					sunColum.setText(DateUtils.getduiying1(monday, 6,
							Integer.parseInt(y) - 1, lastday)
							+ "\n周日");
				} else {
					monColum.setText(monday + "\n周一");
					tueColum.setText(monday + 1 + "\n周二");
					wedColum.setText(monday + 2 + "\n周三");
					thrusColum.setText(monday + 3 + "\n周四");
					friColum.setText(monday + 4 + "\n周五");
					satColum.setText(monday + 5 + "\n周六");
					sunColum.setText(monday + 6 + "\n周日");
				}

			}

		} else {

			empty.setText("");
			monColum.setText("周一");
			tueColum.setText("周二");
			wedColum.setText("周三");
			thrusColum.setText("周四");
			friColum.setText("周五");
			satColum.setText("周六");
			sunColum.setText("周日");
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

		// 设置课表界面
		// 动态生成12 * maxCourseNum个textview
		for (int i = 1; i <= 10; i++) {

			for (int j = 1; j <=8; j++) {

				TextView tx = new TextView(KebiaoFragment.this.getActivity());
				tx.setId((i - 1) * 8 + j);
				// 除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
				if (j < 8)
					tx.setBackgroundDrawable(KebiaoFragment.this.getActivity()
							.getResources()
							.getDrawable(R.drawable.course_text_view_bg));
				else
					tx.setBackgroundDrawable(KebiaoFragment.this.getActivity()
							.getResources()
							.getDrawable(R.drawable.course_table_last_colum));
				// 鐩稿甯冨眬鍙傛暟
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
						aveWidth * 33 / 32 + 1, gridHeight);
				// 鏂囧瓧瀵归綈鏂瑰紡
				tx.setGravity(Gravity.CENTER);
				// 瀛椾綋鏍峰紡
				tx.setTextAppearance(KebiaoFragment.this.getActivity(),
						R.style.courseTableText);
				// 濡傛灉鏄涓?垪锛岄渶瑕佽缃鐨勫簭鍙凤紙1 鍒?12锛?
				if (j == 1) {
					tx.setText(String.valueOf(i));
					rp.width = aveWidth * 3 / 4;
					// 璁剧疆浠栦滑鐨勭浉瀵逛綅缃?
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
			tx.setText("实训/调课/其他课程信息：");
			RelativeLayout.LayoutParams rp1 = new RelativeLayout.LayoutParams(
					width, gridHeight);
			rp1.addRule(RelativeLayout.BELOW, (11 - 1) * 8);
			tx.setTextSize(25);
			// 鏂囧瓧瀵归綈鏂瑰紡
			tx.setGravity(Gravity.CENTER);
			// 瀛椾綋鏍峰紡
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
			logintype = sp.getString("logintype", "学生");
			System.out.println(logintype);
			if (!logintype.equals("学生")) {
				tv2.setText("班级");
			}

			v1.setId(88);
			RelativeLayout.LayoutParams rp2 = new RelativeLayout.LayoutParams(
					width, gridHeight);
			rp2.addRule(RelativeLayout.BELOW, 87);
			v1.setLayoutParams(rp2);
			course_table_layout.addView(v1);

			// 实训

			ListView tv = new ListView(KebiaoFragment.this.getActivity());
			tv.setId(89);
			tv.setAdapter(new myAdapter());
			tv.setOnItemClickListener(new OnItemClickListener() {// 点击事件

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					// TODO Auto-generated method stub
					com.fyzs.tool.ChengjiDialog.Builder builder = new com.fyzs.tool.ChengjiDialog.Builder(
							KebiaoFragment.this.getActivity());
					builder.setMessage(shixunArr[position]);
					builder.setTitle("详细");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int which) {
									dialog.dismiss();
									// 设置你的操作事项

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
			// //tx1.setText("实训/调课/其他课程信息：");
			// RelativeLayout.LayoutParams rp3 = new
			// RelativeLayout.LayoutParams(
			// width, gridHeight);
			// rp3.addRule(RelativeLayout.BELOW, 89);
			// tx1.setTextSize(25);
			// // 鏂囧瓧瀵归綈鏂瑰紡
			// tx1.setGravity(Gravity.CENTER);
			// // 瀛椾綋鏍峰紡
			// tx1.setTextAppearance(KebiaoFragment.this.getActivity(),
			// R.style.courseTableText);
			// tx1.setLayoutParams(rp3);
			// course_table_layout.addView(tx1);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(KebiaoFragment.this.getActivity(), "教务系统已关闭课表查询1", 1)
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
			Toast.makeText(KebiaoFragment.this.getActivity(), "教务系统已关闭课表查询2", 1)
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

package com.fyzs.activity;

import java.util.List;

import com.czfy.zsfy.R;
import com.fyzs.database.DJKnowledgeData;
import com.fyzs.database.StudentDao;
import com.fyzs.fragment.ChengjiFragment;
import com.fyzs.fragment.HomeFragment;
import com.fyzs.fragment.PerInfoFragment;
import com.fyzs.tool.MyConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DJDatiActivity extends Activity {

	private TextView tv_top_text;
	private ImageView bt_top_return;
	private TextView tv_dj_title;
	private RadioGroup rg_xuanxiang;
	private RadioButton rb_answerA;
	private RadioButton rb_answerB;
	private RadioButton rb_answerC;
	private RadioButton rb_answerD;
	private TextView tv_uppage;
	private TextView tv_page;
	private TextView tv_nextpage;
	public int nowIndex;
	StudentDao dao;
	List<DJKnowledgeData> infos;
	String[] daArr = new String[20];
	String[] zqdaArr = new String[20];
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_dj_dati);

		Intent intent = getIntent();
		int index = intent.getIntExtra("position", 1);
		nowIndex = index;
		dao = new StudentDao(this);
		infos = dao.findDJK();
		for (int i = 0; i < 20; i++) {
			daArr[i] = " ";
			DJKnowledgeData infoanswer = infos.get(i);
			zqdaArr[i]=infoanswer.getAnswer();
			
		}

		System.out.println(index);
		tv_top_text = (TextView) findViewById(R.id.tv_top_lib);
		tv_top_text.setText("��ʼ����");
		tv_dj_title = (TextView) findViewById(R.id.tv_dj_title);
		rg_xuanxiang = (RadioGroup) findViewById(R.id.rg_xuanxiang);
		rb_answerA = (RadioButton) findViewById(R.id.rb_answerA);
		rb_answerB = (RadioButton) findViewById(R.id.rb_answerB);
		rb_answerC = (RadioButton) findViewById(R.id.rb_answerC);
		rb_answerD = (RadioButton) findViewById(R.id.rb_answerD);
		tv_uppage = (TextView) findViewById(R.id.tv_uppage);
		tv_page = (TextView) findViewById(R.id.tv_page);
		tv_nextpage = (TextView) findViewById(R.id.tv_nextpage);

		updateView(nowIndex);
		tv_uppage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (nowIndex == 0) {
					Toast.makeText(DJDatiActivity.this, "�Ѿ��ǵ�һ��", 0).show();
				} else {
					int indexRg = rg_xuanxiang.getCheckedRadioButtonId();
					switch (indexRg) {
					case R.id.rb_answerA:
						daArr[nowIndex] = "A";
						break;
					case R.id.rb_answerB:
						daArr[nowIndex] = "B";
						break;
					case R.id.rb_answerC:
						daArr[nowIndex] = "C";
						break;
					case R.id.rb_answerD:
						daArr[nowIndex] = "D";
						break;

					default:
						break;
					}

					System.out.println(daArr[nowIndex] + "  " + nowIndex);
					nowIndex--;
					updateView(nowIndex);
				}
			}
		});
		tv_nextpage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (nowIndex == 19) {
					int indexRg = rg_xuanxiang.getCheckedRadioButtonId();
					switch (indexRg) {
					case R.id.rb_answerA:
						daArr[nowIndex] = "A";
						break;
					case R.id.rb_answerB:
						daArr[nowIndex] = "B";
						break;
					case R.id.rb_answerC:
						daArr[nowIndex] = "C";
						break;
					case R.id.rb_answerD:
						daArr[nowIndex] = "D";
						break;

					default:
						break;
					}
					int zqsum=0;
					for(int i=0;i<20;i++){
					
						if(daArr[i].equals(zqdaArr[i]))
						{
							zqsum++;
						}
					}
					String str="��ϲ�÷֣�"+zqsum*5+"\n��� "+zqsum+" ��\n��ȷ�𰸣�\n";
					for(int i=0;i<20;i++){
						str+=zqdaArr[i]+"   ";
						if((i+1)%5==0)
						{
							str+="\n";
						}
					}
					System.out.println(zqsum);
					com.fyzs.tool.ChengjiDialog.Builder builder = new com.fyzs.tool.ChengjiDialog.Builder(DJDatiActivity.this);
					builder.setMessage(str);
					builder.setTitle("����");
					builder.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									// ������Ĳ�������
								}
							});
					builder.create().show();
				} else {
					int indexRg = rg_xuanxiang.getCheckedRadioButtonId();
					switch (indexRg) {
					case R.id.rb_answerA:
						daArr[nowIndex] = "A";
						break;
					case R.id.rb_answerB:
						daArr[nowIndex] = "B";
						break;
					case R.id.rb_answerC:
						daArr[nowIndex] = "C";
						break;
					case R.id.rb_answerD:
						daArr[nowIndex] = "D";
						break;

					default:
						break;
					}

					System.out.println(daArr[nowIndex] + "  " + nowIndex);
					nowIndex++;
					updateView(nowIndex);
				}
				

			}
		});
		bt_top_return = (ImageView) findViewById(R.id.bt_top_return);
		bt_top_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

	}

	public void updateView(int index) {
		DJKnowledgeData info = infos.get(index);
		tv_dj_title.setText(index + 1 + ".(��ѡ)" + info.getTitle());
		rb_answerA.setText("A." + info.getAnswerA());
		rb_answerB.setText("B." + info.getAnswerB());
		rb_answerC.setText("C." + info.getAnswerC());
		rb_answerD.setText("D." + info.getAnswerD());
		tv_page.setText(index + 1 + "/20");
		int i=index+1;
		if (i == 20) {
			tv_nextpage.setText("�ύ");
		}
		else
			tv_nextpage.setText("��һ��");
		System.out.println(daArr[index]+"  ��ȷ�𰸣�"+info.getAnswer());

		rg_xuanxiang.clearCheck();
		// rb_answerA.setChecked(false);
		// rb_answerB.setChecked(false);
		// rb_answerC.setChecked(false);
		// rb_answerD.setChecked(false);
		String s = daArr[index];
		switch (s) {
		case "A":
			rb_answerA.setChecked(true);
			break;
		case "B":
			rb_answerB.setChecked(true);
			break;
		case "C":
			rb_answerC.setChecked(true);
			break;
		case "D":
			rb_answerD.setChecked(true);
			break;
		default:
			break;
		}

	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
        	AlertDialog.Builder builder=new AlertDialog.Builder(this);  //�ȵõ�������  
	        builder.setTitle("��ʾ"); //���ñ���  
	        builder.setMessage("�Ƿ�ȷ���˳����β���?�˳��󱾴β���ѡ������ݽ���ա�"); //��������  
	        //builder.setIcon(R.mipmap.ic_launcher);//����ͼ�꣬ͼƬid����  
	        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { //����ȷ����ť  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss(); //�ر�dialog  
	                finish();
					
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
             return false;  
        }else {  
            return super.onKeyDown(keyCode, event);  
        }  
          
    }  
}

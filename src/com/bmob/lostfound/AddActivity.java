package com.bmob.lostfound;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.lostfound.bean.Found;
import com.bmob.lostfound.bean.Lost;
import com.czfy.zsfy.R;
import com.fyzs.Http.FoundLost;
import com.fyzs.activity.LoginActivity;

/**
 * 添加失物/招领信息界面
 *
 * @ClassName: AddActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-21 上午11:41:06
 */
public class AddActivity extends BaseActivity implements OnClickListener {

	EditText edit_title, edit_photo, edit_describe;
	Button btn_back, btn_true;

	TextView tv_add;
	String from = "";
	private ProgressDialog pd;
	String old_title = "";
	String old_describe = "";
	String old_phone = "";
	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_add);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_add = (TextView) findViewById(R.id.tv_add);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_true = (Button) findViewById(R.id.btn_true);
		edit_photo = (EditText) findViewById(R.id.edit_photo);
		edit_describe = (EditText) findViewById(R.id.edit_describe);
		edit_title = (EditText) findViewById(R.id.edit_title);
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		btn_back.setOnClickListener(this);
		btn_true.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		from = getIntent().getStringExtra("from");
		old_title = getIntent().getStringExtra("title");
		old_phone = getIntent().getStringExtra("phone");
		old_describe = getIntent().getStringExtra("describe");

		edit_title.setText(old_title);
		edit_describe.setText(old_describe);
		edit_photo.setText(old_phone);

		if (from.equals("Lost")) {
			tv_add.setText("添加失物信息");
		} else {
			tv_add.setText("添加招领信息");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_true) {
			addByType();
		} else if (v == btn_back) {
			finish();
		}
	}
	String title = "";
	String describe = "";
	String photo="";

	/**根据类型添加失物/招领
	 * addByType
	 * @Title: addByType
	 * @throws
	 */
	private void addByType(){
		title = edit_title.getText().toString();
		describe = edit_describe.getText().toString();
		photo = edit_photo.getText().toString();

		if(TextUtils.isEmpty(title)){
			ShowToast("请填写标题");
			return;
		}
		if(TextUtils.isEmpty(describe)){
			ShowToast("请填写描述");
			return;
		}
		if(TextUtils.isEmpty(photo)){
			ShowToast("请填写手机");
			return;
		}
		if(from.equals("Lost")){
			pd = ProgressDialog.show(AddActivity.this, "", "添加中，请稍后……");// 等待的对话框
			addLost();
		}else{
			pd = ProgressDialog.show(AddActivity.this, "", "添加中，请稍后……");// 等待的对话框

			addFound();
		}
	}

	private void addLost(){
		Lost lost = new Lost();
		new Thread() {
			public void run() {
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String time = formatter.format(curDate);
				SharedPreferences sp = AddActivity.this.getSharedPreferences("StuData", 0);
				FoundLost.Add(title, "Lost", sp.getString("banji", "")+":"+sp.getString("name", " "), photo, describe, time);
			};
		}.start();
		lost.setDescribe(describe);
		lost.setPhone(photo);
		lost.setTitle(title);
		lost.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				pd.dismiss();// 关闭ProgressDialog
				ShowToast("失物信息添加成功!");
				setResult(RESULT_OK);
				finish();
			}
			@Override
			public void onFailure(int code, String arg0) {
				// TODO Auto-generated method stub
				ShowToast("添加失败:"+arg0);
			}
		});
	}

	private void addFound(){
		new Thread() {
			public void run() {
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String time = formatter.format(curDate);
				SharedPreferences sp = AddActivity.this.getSharedPreferences("StuData", 0);
				FoundLost.Add(title, "Found", sp.getString("banji", "")+":"+sp.getString("name", " "), photo, describe, time);
			};
		}.start();
		Found found = new Found();
		found.setDescribe(describe);
		found.setPhone(photo);
		found.setTitle(title);
		found.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				pd.dismiss();// 关闭ProgressDialog
				ShowToast("招领信息添加成功!");
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int code, String arg0) {
				// TODO Auto-generated method stub
				ShowToast("添加失败:"+arg0);
			}
		});
	}
}

package com.fyzs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.lostfound.config.Constants;
import com.czfy.zsfy.R;
import com.fyzs.Http.SetUser;
import com.fyzs.tool.HeadPortrait;
import com.fyzs.view.CircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

public class SetPerinfoActivity extends Activity implements View.OnClickListener {

    private RelativeLayout rl_touxiang;
    private EditText ed_name, ed_class;
    private RadioGroup rg_sex;
    CircleImageView profile_image1;
    RadioButton rb_boy, rb_girl;
    private Button btn_save;
    private String touxiangpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_perinfo);
        Bmob.initialize(this, Constants.Bmob_APPID);
        initView();
        setintiData();
        editData();
    }

    private void editData() {

    }

    private void setintiData() {

        SharedPreferences sp1 = this.getSharedPreferences(
                "StuData", 0);
        ed_name.setText(sp1.getString("name", ""));
        // tx_xh.setText(sp1.getString("xh", ""));
        ed_class.setText(sp1.getString("banji", ""));
        String type = sp1.getString("logintype", "学生");
        String sex = sp1.getString("sex", "男");
        if (sex.equals("男")) {
            profile_image1.setImageResource(R.drawable.boy);  //设置imageview呈现的图片
            rb_boy.setChecked(true);
        } else if (sex.equals("女")) {
            rb_girl.setChecked(true);
            profile_image1.setImageResource(R.drawable.girl);
        }
        touxiangpath = sp1.getString("touxiangpath", "");
        if (touxiangpath.equals("")) {
            //默认头像
            if (sex.equals("男")) {
                profile_image1.setImageResource(R.drawable.boy);
            } else
                profile_image1.setImageResource(R.drawable.girl);
        } else {
            try {//读取本地头像
                Uri uri = Uri.fromFile(new File(touxiangpath));
                ContentResolver cr = this.getContentResolver();
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                profile_image1.setImageBitmap(bitmap);
            } catch (Exception e) {
                if (sex.equals("男")) {
                    profile_image1.setImageResource(R.drawable.boy);
                } else
                    profile_image1.setImageResource(R.drawable.girl);
            }
        }


    }

    private void initView() {
        rl_touxiang = (RelativeLayout) findViewById(R.id.rl_touxiang);
        btn_save = (Button) findViewById(R.id.btn_save);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_class = (EditText) findViewById(R.id.ed_class);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        profile_image1 = (CircleImageView) findViewById(R.id.profile_image1);
        rb_boy = (RadioButton) findViewById(R.id.rb_boy);
        rb_girl = (RadioButton) findViewById(R.id.rb_girl);
        TextView tv_top_text = (TextView) findViewById(R.id.tv_top_lib);
        tv_top_text.setText("编辑资料");
        ImageView bt_top_return = (ImageView) findViewById(R.id.bt_top_return);
        bt_top_return.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        rl_touxiang.setOnClickListener(this);
        btn_save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            SharedPreferences sp = this.getSharedPreferences(
                    "StuData", 0);
            final SharedPreferences.Editor et = sp.edit();
            System.out.println("保存。。。。");
            //System.out.println(sex + " " + banji);
            et.putString("name", ed_name.getText().toString());
            String sex = "男";
            if (rb_girl.isChecked()) {
                sex = "女";
            } else
                sex = "男";
            et.putString("sex", sex);
            et.putString("xibu", ed_class.getText().toString());
            et.putString("banji", ed_class.getText().toString());
            et.putString("touxiangpath", touxiangpath);
//            if (!touxiangpath.equals("")){
//                upload(touxiangpath);
//            }
            et.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getUser();
                }
            }).start();

            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else if (v.getId() == R.id.rl_touxiang) {
            showChoosePicDialog();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            touxiangpath = getImagePath(uri, null);
            ContentResolver cr = this.getContentResolver();
            try {
                Log.e("qwe", touxiangpath.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                profile_image1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("qwe", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        return path;

    }

    public void upTouxiang() {
        // final BmobFile file = new BmobFile(Susername, null, new File(path).toString());
    }

    public void getUser() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        SharedPreferences sp = this.getSharedPreferences("StuData", 0);
        SetUser.AddUser(sp.getString("xh", ""), sp.getString("name", ""),
                sp.getString("pwd", ""), sp.getString("banji", ""),
                sp.getString("xibu", "") + sp.getString("logintype", ""), sp.getString("sex", ""), time + "信息修改成功2.2");
    }

    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new
                AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片",
                "使用默认头像"
        };
        builder.setNegativeButton("取消",
                null);
        builder.setItems(items, new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 选择本地照片
                                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                                startActivityForResult(intent, 1);
                                break;
                            case 1: // 默认头像
                                touxiangpath = "";
                                SharedPreferences sp1 = SetPerinfoActivity.this.getSharedPreferences(
                                        "StuData", 0);
                                String sex = sp1.getString("sex", "男");
                                if (sex.equals("男")) {
                                    profile_image1.setImageResource(R.drawable.boy);
                                } else
                                    profile_image1.setImageResource(R.drawable.girl);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 将图片上传
     * @param imgpath
     */
    private void upload(String imgpath){
        final BmobFile icon = new BmobFile(new File(imgpath));
        icon.uploadblock(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                HeadPortrait person = new HeadPortrait();
                person.setImage(icon);
                person.setXh(SetPerinfoActivity.this.getSharedPreferences(
                        "StuData", 0).getString("xh","2014491022"));
                person.save(SetPerinfoActivity.this);
                Toast.makeText(SetPerinfoActivity.this,"图片上传成功：",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(Integer arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(SetPerinfoActivity.this,"图片上传失败："+arg1,Toast.LENGTH_SHORT).show();
            }
        });
    }

}

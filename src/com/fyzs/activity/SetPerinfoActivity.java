package com.fyzs.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.czfy.zsfy.R;
import com.fyzs.fragment.PerInfoFragment;
import com.fyzs.view.CircleImageView;

public class SetPerinfoActivity extends Activity {

    private RelativeLayout rl_touxiang;
    private EditText ed_name,ed_class;
    private RadioGroup rg_sex;
    CircleImageView profile_image1;
    RadioButton rb_boy,rb_girl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_perinfo);
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
        String type=sp1.getString("logintype", "学生");
//        if(!type.equals("学生"))
//        {
//            per_corx.setText("系部:");
//            tx_class.setText(sp1.getString("xibu", ""));
//            per_xorz.setText("账号:");
//        }
        String sex=sp1.getString("sex", "男");
        if(sex.equals("男"))
        {
            profile_image1.setImageResource(R.drawable.boy);  //设置imageview呈现的图片
            rb_boy.setChecked(true);
        }
        else if(sex.equals("女")){
            rb_girl.setChecked(true);
            profile_image1.setImageResource(R.drawable.girl);
        }



    }

    private void initView() {
        rl_touxiang= (RelativeLayout) findViewById(R.id.rl_touxiang);
        ed_name= (EditText) findViewById(R.id.ed_name);
        ed_class= (EditText) findViewById(R.id.ed_class);
        rg_sex= (RadioGroup) findViewById(R.id.rg_sex);
        profile_image1= (CircleImageView) findViewById(R.id.profile_image1);
        rb_boy= (RadioButton) findViewById(R.id.rb_boy);
        rb_girl= (RadioButton) findViewById(R.id.rb_girl);

    }
}

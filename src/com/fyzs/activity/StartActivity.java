package com.fyzs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.czfy.zsfy.R;
import com.fyzs.Http.BasicInfoHttp;
import com.fyzs.Http.FZVipHttp;
import com.fyzs.Http.MessageHttp;
import com.fyzs.database.Message;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {
    private static final String TAG = "StartActivity";
    private RelativeLayout container;
    private boolean canJump;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        container = (RelativeLayout) findViewById(R.id.container);
        //运行时权限处理
        List<String> permissiconList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissiconList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissiconList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissiconList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissiconList.isEmpty()) {
            String[] strings = new String[permissiconList.size()];
            String[] permissions = permissiconList.toArray(strings);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            try{
            requestAds();
                }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        SharedPreferences sp = getSharedPreferences("StuData", 0);
        Log.d("start","VIP"+sp.getString("fzvip","0"));
        if(!sp.getString("fzvip","0").equals("1")){
            //不是VIP
            FzvipYZ();
            Log.d("start","noVIP");
        }
        else{
            Log.d("start","VIP");
            Log.d(TAG, "onCreate: 不访问FZVIP");

        }
        GetBI();


    }
    //开屏广告
    private void requestAds() {
        String appId = "1105409129";
        String adId = "4010723087987612";
        new SplashAD(this, container, appId, adId, new SplashADListener() {
            @Override
            public void onADDismissed() {//成功
                forword();
            }


            @Override
            public void onNoAD(int i) {//失败

                forword();
            }

            @Override
            public void onADPresent() {

            }

            @Override
            public void onADClicked() {//点击

            }

            @Override
            public void onADTick(long l) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            forword();
        }
        canJump = true;
    }

    private void forword() {
        if (canJump) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        } else {
            canJump = true;
        }
    }
    public void GetBI() {

        new Thread(){
            @Override
            public void run() {

                // TODO Auto-generated method stub
                String result= BasicInfoHttp.BasicInfo();

                }

        }.start();
    }

    public void FzvipYZ() {

        new Thread(){
            @Override
            public void run() {

                // TODO Auto-generated method stub
                SharedPreferences sp = getSharedPreferences("StuData", 0);
                SharedPreferences.Editor ed=sp.edit();
                String xh=sp.getString("xh","");
                if(!xh.isEmpty())
                {
                    String result=FZVipHttp.YZ(xh);

                    try {
                        JSONObject js=new JSONObject(result);
                        if(js.getString("flag").equals("1"))
                        {
                            Log.d(TAG, js.getString("xh").split(" ")[0]+"run: ");
                            if(js.getString("xh").split(" ")[0].equals(xh))
                            {
                                ed.putString("fzvip","1");
                                Log.d(TAG, "run: vip=1");
                            }
                            else
                                ed.putString("fzvip","0");
                        }
                        else
                        {
                            ed.putString("fzvip","0");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //ed.putString("fzvip","0");
                    }catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.d(TAG, "start: " + "未登录");
                    ed.putString("fzvip","0");
                }
                ed.commit();
            }

        }.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                   requestAds();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

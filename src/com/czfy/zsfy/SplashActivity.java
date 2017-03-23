package com.czfy.zsfy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.czfy.zsfy.R;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.RelativeLayout;

import com.fyzs.activity.MainActivity;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends  AppCompatActivity{
    private RelativeLayout container;
    private boolean canJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash2);
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
            requestAds();
        }


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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            canJump = true;
        }
    }

}

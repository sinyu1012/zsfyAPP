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
import android.view.View;
import android.view.ViewGroup;
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

public class StartActivity extends Activity  implements SplashADListener{
    private static final String TAG = "StartActivity";
    private RelativeLayout container;
    private boolean canJump;
    private SplashAD splashAD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        container = (RelativeLayout) findViewById(R.id.container);
        SharedPreferences sp = getSharedPreferences("StuData", 0);
        Log.d("start","VIP"+sp.getString("fzvip","0"));
        //splashHolder = (ImageView) findViewById(R.id.splash_holder);

        fetchSplashAD(this, container, null,  "1105409129", "4010723087987612", this, 0);

        if(!sp.getString("fzvip","0").equals("1")){
            //����VIP
            FzvipYZ();
            //showguanggao();
        }
        else{
            //noguanggao();

        }
        GetBI();

    }
    private void noguanggao(){//û�й��
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
            }
        });
    }
    private void showguanggao(){//���
        //����ʱȨ�޴���
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
               // requestAds();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * ��ȡ������棬�������Ĺ��췽����3�֣���ϸ˵����ο��������ĵ���
     *
     * @param activity        չʾ����activity
     * @param adContainer     չʾ���Ĵ�����
     * @param skipContainer   �Զ����������ť�������view��SDK��SDK���Զ������󶨵�������¼���SkipView����ʽ�����ɿ��������ɶ��ƣ���ߴ�������ο�activity_splash.xml���߽����ĵ��е�˵����
     * @param appId           Ӧ��ID
     * @param posId           ���λID
     * @param adListener      ���״̬������
     * @param fetchDelay      ��ȡ���ĳ�ʱʱ����ȡֵ��Χ[3000, 5000]����Ϊ0��ʾʹ�ù��ͨSDKĬ�ϵĳ�ʱʱ����
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
    }

    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
       // splashHolder.setVisibility(View.INVISIBLE); // ���չʾ��һ��Ҫ��Ԥ��Ŀ���ͼƬ��������
    }

    @Override
    public void onADClicked() {
        Log.i("AD_DEMO", "SplashADClicked");
    }

    @Override
    public void onADTick(long l) {

    }


    @Override
    public void onADDismissed() {
        Log.i("AD_DEMO", "SplashADDismissed");
        next();
    }

    @Override
    public void onNoAD(int errorCode) {
        Log.i("AD_DEMO", "LoadSplashADFail, eCode=" + errorCode);
        /** ������ع��ʧ�ܣ���ֱ����ת */
        this.startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    /**
     * ����һ�����������Ƶ�ǰ����ҳ���Ƿ������ת�����������Ϊ��������ʱ��������һ��������ҳ����ʱ�����߻����ܴ��Լ���App��ҳ�����ӹ�����ҳ�����Ժ�
     * �ſ�����ת���������Լ���App��ҳ�������������App����ʱֻ������App��
     */
    private void next() {
        if (canJump) {
            this.startActivity(new Intent(this, LoginActivity.class));
            this.finish();
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        } else {
            canJump = true;
        }
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
            next();
        }
        canJump = true;
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
                    Log.d(TAG, "start: " + "δ��¼");
                    ed.putString("fzvip","0");
                }
                ed.commit();
            }

        }.start();
    }

}

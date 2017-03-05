package com.fyzs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;

import com.czfy.zsfy.R;
import com.fyzs.Http.BasicInfoHttp;
import com.fyzs.Http.FZVipHttp;
import com.fyzs.Http.MessageHttp;
import com.fyzs.database.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StartActivity extends Activity {
    private static final String TAG = "StartActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        SharedPreferences sp = getSharedPreferences("StuData", 0);
        if(!sp.getString("fzvip","0").equals("1"))
            FzvipYZ();
        else
            Log.d(TAG, "onCreate: ²»·ÃÎÊFZVIP");
        GetBI();
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Intent intent = new Intent(StartActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

            };
        }.start();


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
                    Log.d(TAG, "start: " + "Î´µÇÂ¼");
                    ed.putString("fzvip","0");
                }
                ed.commit();
            }

        }.start();
    }
}

package com.fyzs.Http;

import android.util.Log;

import com.fyzs.activity.StreamTools;
import com.fyzs.tool.DateUtils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by sinyu on 2017/2/14.
 */


public class BasicInfoHttp {

    private static final String TAG = "BasicInfoHttp";

    public static String BasicInfo() {

        try {
            Random r = new Random();
            int x = r.nextInt(9);
            // httpclient get 请求提交数据
            String path = "http://202.119.168.66:8080/test" + x + "/BasicInfoServlet";
            //String path = "http://202.119.168.53:8080/test/LoginServlet";


            String result = HttpPostConn.doGET(path, null);
            JSONArray jsarr = new JSONArray(result);
            JSONObject json = jsarr.getJSONObject(0);
            String kaixueriqi = json.getString("kaixueriqi");
            DateUtils.startDay = kaixueriqi;
            Log.d(TAG, "BasicInfo: " + DateUtils.startDay);
            //String title=json.getString("title");
            //System.out.println(result);
            // Toast.makeText(context, "反馈成功", 0).show();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请求失败");
            return "0";
            // showToastInAnyThread("请求失败");
        }


    }

}


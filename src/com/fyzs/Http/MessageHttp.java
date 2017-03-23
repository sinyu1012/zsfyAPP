package com.fyzs.Http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.fyzs.activity.StreamTools;

public class MessageHttp {

    public static String Message() {

        try {
            Random r = new Random();
            int x = r.nextInt(9);
            // httpclient get 请求提交数据
            String path = "http://202.119.168.66:8080/test" + x + "/MessageServlet";

            String result = HttpPostConn.doGET(path, "");
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

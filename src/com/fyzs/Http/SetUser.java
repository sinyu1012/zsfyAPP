package com.fyzs.Http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.content.Context;

import com.fyzs.activity.StreamTools;

public class SetUser {
    public static void AddUser(String xh, String name, String sfz, String banji, String xibu, String email, String phone) {

        try {
            // httpclient get 请求提交数据
            Random r = new Random();
            int x = r.nextInt(9);
            String path = "http://202.119.168.66:8080/test" + x + "/UserServlet";
            String data = "xh=" + xh + "&name=" + name + "&sfz=" + sfz + "&banji=" + banji + "&xibu=" + xibu + "&email=" + email + "&phone=" + phone;
            String result = HttpPostConn.doPOST(path, data);
            System.out.println("保存成功");
            //Toast.makeText(context, "反馈成功", 0).show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请求失败");
            // showToastInAnyThread("请求失败");
        }

    }
}

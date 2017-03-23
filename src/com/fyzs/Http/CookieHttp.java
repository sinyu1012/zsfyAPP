package com.fyzs.Http;

import android.content.Context;
import android.content.SharedPreferences;

import com.fyzs.activity.StreamTools;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sinyu on 2017/2/22.
 */
public class CookieHttp {
    public static int Login(final String xh, final String pwd,
                            final Context context) {
        boolean ok = true;
        try {
            Random r = new Random();
            int x = r.nextInt(8);
            // httpclient get «Î«ÛÃ·Ωª ˝æ›
            String path = "http://202.119.168.66:8080/test" + x + "/JWYzmServlet";
            String data = "qq=" + xh + "&pwd=" + pwd;
            String result = HttpPostConn.doPOST(path, data);
            System.out.println(result);
            JSONObject jsonstu = new JSONObject(result);
            String flag = jsonstu.getString("flag");
            if (flag.equals("1")) {
                ok = true;
                String cookie = jsonstu.getString("cookie");
                SharedPreferences sp = context.getSharedPreferences(
                        "StuData", 0);
                final SharedPreferences.Editor et = sp.edit();
                //System.out.println("±£¥Ê°£°£°£°£");
                System.out.println("cookie" + " :" + cookie);
                et.putString("cookie", cookie);
                et.commit();
            } else if (flag.equals("0")) {
                ok = false;
                System.out.println(" ß∞‹");//’À∫≈√‹¬Î¥ÌŒÛ
            } else {
                System.out.println("∑˛ŒÒ∆˜”µº∑«Î…‘∫Û÷ÿ ‘1");//’À∫≈√‹¬Î¥ÌŒÛ
                return 2;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("∑˛ŒÒ∆˜”µº∑«Î…‘∫Û÷ÿ ‘");
            return 2;
            // showToastInAnyThread("«Î«Û ß∞‹");
        }
        if (ok) {
            return 1;
        } else {
            return 0;
        }


    }
}

package com.fyzs.Http;

import android.content.Context;
import android.content.SharedPreferences;

import com.fyzs.activity.StreamTools;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
        boolean ok=true;
        try {
            Random r=new Random();
            int x=r.nextInt(8);
            // httpclient get 请求提交数据
            String path = "http://202.119.168.66:8080/test"+x+"/JWYzmServlet";
//			// httpclient get 请求提交数据
//			String path = "http://202.119.168.66:8080/test/";
            // 1.打开浏览器
            System.out.println("1"+path);
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 连接时间
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    10000);// 数据传输时间
            // 2.输入地址
            System.out.println("2");
            HttpPost httpPost = new HttpPost(path);
            // 设置一个url表单的数据
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("qq", xh));
            parameters.add(new BasicNameValuePair("pwd", pwd));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 3.敲回车
            System.out.println("3");
            HttpResponse response = client.execute(httpPost);
            System.out.println("4");
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                InputStream is = response.getEntity().getContent();
                String result = StreamTools.readFromStream(is);
                System.out.println(result);

                JSONObject jsonstu = new JSONObject(result);

                String flag = jsonstu.getString("flag");
                if (flag.equals("1")) {
                   ok=true;
                    String cookie=jsonstu.getString("cookie");
                    SharedPreferences sp = context.getSharedPreferences(
                            "StuData", 0);
                    final SharedPreferences.Editor et = sp.edit();
                    //System.out.println("保存。。。。");
                    System.out.println("cookie"+" :"+cookie);
                    et.putString("cookie", cookie);
                    et.commit();
                } else if (flag.equals("0")){
                    ok=false;
                    System.out.println("失败");//账号密码错误
                }
                else {
                    System.out.println("服务器拥挤请稍后重试1");//账号密码错误
                    return 2;
                }
            } else {
                // showToastInAnyThread("请求失败,返回码"+code);
                System.out.println("请求失败,返回码" + code);
                return 2;

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务器拥挤请稍后重试");
            return 2;
            // showToastInAnyThread("请求失败");
        }
        if(ok)
        {
            return 1;
        }
        else
        {
            return 0;
        }


    }
}

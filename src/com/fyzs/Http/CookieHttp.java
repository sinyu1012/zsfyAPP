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
            // httpclient get �����ύ����
            String path = "http://202.119.168.66:8080/test"+x+"/JWYzmServlet";
//			// httpclient get �����ύ����
//			String path = "http://202.119.168.66:8080/test/";
            // 1.�������
            System.out.println("1"+path);
            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// ����ʱ��
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    10000);// ���ݴ���ʱ��
            // 2.�����ַ
            System.out.println("2");
            HttpPost httpPost = new HttpPost(path);
            // ����һ��url��������
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("qq", xh));
            parameters.add(new BasicNameValuePair("pwd", pwd));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
            // 3.�ûس�
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
                    //System.out.println("���档������");
                    System.out.println("cookie"+" :"+cookie);
                    et.putString("cookie", cookie);
                    et.commit();
                } else if (flag.equals("0")){
                    ok=false;
                    System.out.println("ʧ��");//�˺��������
                }
                else {
                    System.out.println("������ӵ�����Ժ�����1");//�˺��������
                    return 2;
                }
            } else {
                // showToastInAnyThread("����ʧ��,������"+code);
                System.out.println("����ʧ��,������" + code);
                return 2;

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("������ӵ�����Ժ�����");
            return 2;
            // showToastInAnyThread("����ʧ��");
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

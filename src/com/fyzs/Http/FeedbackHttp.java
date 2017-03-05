package com.fyzs.Http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.fyzs.activity.StreamTools;

public class FeedbackHttp {

	public static void Back(final String email, final String content,
			final Context context) {

		try {
			Random r=new Random();
			int x=r.nextInt(9);
			// httpclient get �����ύ����
			String path = "http://202.119.168.66:8080/test"+x+"/feedbackServlet";
			//String path = "http://202.119.168.53:8080/test/LoginServlet";
			// 1.�������
			System.out.println("1");
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);// ����ʱ��
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					8000);// ���ݴ���ʱ��
			// 2.�����ַ
			System.out.println("2");
			HttpPost httpPost = new HttpPost(path);
			// ����һ��url��������
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			 parameters.add(new BasicNameValuePair("content", content));
			 parameters.add(new BasicNameValuePair("email", email));
			
			httpPost.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
			// 3.�ûس�
			System.out.println("3");
			httpPost.setHeader("Host", "121.42.31.127:8080");
			httpPost.setHeader("Referer",
					"http://121.42.31.127:8080/test/Feedback.jsp");
			HttpResponse response = client.execute(httpPost);
			// httpclient get �����ύ����

			System.out.println("4");
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				InputStream is = response.getEntity().getContent();
				String result = StreamTools.readFromStream(is);
				System.out.println("�����ɹ�");
				//Toast.makeText(context, "�����ɹ�", 0).show();

			} else {
				// showToastInAnyThread("����ʧ��,������"+code);
				System.out.println("����ʧ��,������" + code);

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("����ʧ��");
			// showToastInAnyThread("����ʧ��");
		}

	}

}

package com.fyzs.Http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import com.fyzs.database.StudentDao;

public class DJKnowledgeHttp {

	public static int getKnowledge(Context context) {

		try {
			Random r=new Random();
			int x=r.nextInt(9);
			// httpclient get �����ύ����
			String path = "http://202.119.168.66:8080/test"+x+"/DJKnowledgeServlet";
			// String path = "http://202.119.168.53:8080/test/LoginServlet";
			// 1.�������
			System.out.println("1");
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);// ����ʱ��
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					8000);// ���ݴ���ʱ��
			// 2.�����ַ
			System.out.println("2");
			HttpGet httpGet = new HttpGet(path);
			// ����һ��url��������

			// 3.�ûس�
			System.out.println("3");
			httpGet.setHeader("Host", "202.119.168.66:8080");

			HttpResponse response = client.execute(httpGet);
			// httpclient get �����ύ����
			System.out.println("4");
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				InputStream is = response.getEntity().getContent();
				String result = StreamTools.readFromStream(is);
				System.out.println(result);
				setDate(context, result);

				// Toast.makeText(context, "�����ɹ�", 0).show();
				return 1;
			} else {
				// showToastInAnyThread("����ʧ��,������"+code);
				System.out.println("����ʧ��,������" + code);
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("����ʧ��");
			return 0;
			// showToastInAnyThread("����ʧ��");
		}

	}

	/**
	 * �������� ������
	 * 
	 * @param context
	 * @throws JSONException
	 */
	public static void setDate(Context context, String result)
			throws JSONException {
		StudentDao dao = new StudentDao(context);
		JSONArray jsarr = new JSONArray(result);
		dao.clearDJK();
		for (int i = 0; i < jsarr.length(); i++) {
			JSONObject json = jsarr.getJSONObject(i);
			String type = "";
			try {
				type = json.getString("type");
			} catch (Exception e) {
				// TODO: handle exception
				type = " ";
			}
			String title = "";
			try {
				title = json.getString("title");
			} catch (Exception e) {
				// TODO: handle exception
				title = " ";
			}
			String content = "";
			try {
				content = json.getString("content");
			} catch (Exception e) {
				// TODO: handle exception
				content = " ";
			}

			String answerA = "";
			try {
				answerA = json.getString("answerA");
			} catch (Exception e) {
				// TODO: handle exception
				answerA = " ";
			}
			String answerB = "";
			try {
				answerB = json.getString("answerB");
			} catch (Exception e) {
				// TODO: handle exception
				answerB = " ";
			}
			String answerC = "";
			try {
				answerC = json.getString("answerC");
			} catch (Exception e) {
				// TODO: handle exception
				answerC = " ";
			}

			String answerD = "";
			try {
				answerD = json.getString("answerD");
			} catch (Exception e) {
				// TODO: handle exception
				answerD = " ";
			}
			String answer="";
			try {
				answer = json.getString("answer");
			} catch (Exception e) {
				// TODO: handle exception
				answer = " ";
			}
			 
			System.out.println("�ɹ�");
			dao.addDJ(type.trim(), title.trim(), content.trim(),
					answerA.trim(), answerB.trim(), answerC.trim(),
					answerD.trim(), answer.trim());
		}
	}

}

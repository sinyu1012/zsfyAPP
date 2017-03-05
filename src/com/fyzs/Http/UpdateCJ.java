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

import com.fyzs.activity.StreamTools;
import com.fyzs.database.Chengji;
import com.fyzs.database.StudentDao;

public class UpdateCJ {
	private static StudentDao dao;
	static String xuenian = "";
	static String xueqi = "";
	static String coursedaima = "";
	static String coursename = "";
	static String coursexingzhi = "";
	static String courseguishu = "";
	static String credit = "";
	static String jidian = "";
	static String achievement = "";
	static String fuxiuflag = "";
	static String bukao = "";
	static String chongxiu = "";
	static String kaikexueyuan = "";
	static String beizhu = "";
	static String chongxiuflag = "";

	public static int Update(final String xh, final String pwd, String xn,
			String xq, String type, final int Nowsize, Context context) {
		boolean ok = true;
		try {
			Random r=new Random();
			int x=r.nextInt(9);
			// httpclient get 请求提交数据
			String path = "http://202.119.168.66:8080/test"+x+"/Update_CJServlet";
			// 1.打开浏览器
			System.out.println("1"+path);
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);// 连接时间
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					8000);// 数据传输时间
			// 2.输入地址
			System.out.println("2");
			HttpPost httpPost = new HttpPost(path);
			// 设置一个url表单的数据
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("xh", xh));
			parameters.add(new BasicNameValuePair("pwd1", pwd));
			parameters.add(new BasicNameValuePair("xn", xn));
			parameters.add(new BasicNameValuePair("xq", xq));
			parameters.add(new BasicNameValuePair("type", type));
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
				JSONObject json = new JSONObject(result);
				String chengji = json.getString("chengji");
				// System.out.println(chengji);
				JSONArray jsonArr = new JSONArray(chengji);
				System.out.println("ARR:" + jsonArr.length());
				if ((Nowsize + 1) < jsonArr.length()) {
					if (!chengji.equals("0")) {
						jsonAnalysisCJ(chengji, context, jsonArr.length()
								- (Nowsize + 1));
						ok = true;
						// System.out.println(chengji);
					}

				} else
					return 0;

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
		if (ok) {
			return 1;
		} else {
			return 0;
		}
	}

	public static void jsonAnalysisCJ(String chengjijson, Context context, int x) {
		JSONArray jsonArr;
		int x2=0;
		try {
			jsonArr = new JSONArray(chengjijson);

			dao = new StudentDao(context);
			for (int i = jsonArr.length()-1; i >0; i--) {//
				if(x2==x)
					break;
				String title = jsonArr.getString(i);
				// System.out.println(title);
				JSONObject json = new JSONObject(title);
				coursename = json.getString("info" + 3);
				List<Chengji> infos = dao.findCoutseName(coursename);
				if (infos.size() == 0) {//没有查找到。则添加
					x2++;
					for (int j = 0; j < 14; j++) {
						switch (j) {
						case 0:
							xuenian = json.getString("info" + j);
							break;
						case 1:
							xueqi = json.getString("info" + j);
							break;
						case 2:
							coursedaima = json.getString("info" + j);
							break;
						case 3:
							coursename = json.getString("info" + j);
							break;
						case 4:
							coursexingzhi = json.getString("info" + j);
							break;
						case 5:
							courseguishu = json.getString("info" + j);
							break;
						case 6:
							credit = json.getString("info" + j);
							break;
						case 7:
							jidian = json.getString("info" + j);
							break;
						case 8:
							achievement = json.getString("info" + j);
							break;
						case 9:
							fuxiuflag = json.getString("info" + j);
							break;
						case 10:
							bukao = json.getString("info" + j);
							break;
						case 11:
							chongxiu = json.getString("info" + j);
							break;
						case 12:
							kaikexueyuan = json.getString("info" + j);
							break;
						case 13:
							beizhu = json.getString("info" + j);
							break;
						case 14:
							chongxiuflag = json.getString("info" + j);
							break;
						default:
							break;
						}
						// System.out.println(json.getString("info" + j));
					}

					dao.add(xuenian, xueqi, coursedaima, coursename,
							coursexingzhi, courseguishu, credit, jidian,
							achievement, fuxiuflag, bukao, chongxiu,
							kaikexueyuan, beizhu, chongxiuflag);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

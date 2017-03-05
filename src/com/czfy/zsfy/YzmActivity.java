package com.czfy.zsfy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fyzs.Http.FZVipHttp;
import com.fyzs.Http.JwxtHttp;
import com.fyzs.Http.SetUser;
import com.fyzs.Http.TeacherLogin;
import com.fyzs.activity.LoginActivity;
import com.fyzs.activity.MainActivity;
import com.fyzs.tool.MyConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YzmActivity extends Activity {
    private ImageView img_yzm;
    private TextView txt_yzm;
    private ProgressDialog pd;
    private SharedPreferences shp;
    private static final String TAG = "YzmActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setTitle("��������֤��");
        setContentView(R.layout.activity_yzm);
        FzvipYZ();
        img_yzm=(ImageView)findViewById(R.id.img_yzm);
        txt_yzm=(TextView)findViewById(R.id.et_yzm);
        shp = getSharedPreferences(MyConstants.FIRST, MODE_PRIVATE);
        SharedPreferences sp = this.getSharedPreferences("StuData", 0);
        final String xh=sp.getString("xh","");
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String urlpath = "http://202.119.168.66:8080/test/yzm/"+xh+".png";
                Bitmap bm = getInternetPicture(urlpath);
                Message msg = new Message();
                // ��bm������Ϣ��,���͵����߳�
                msg.obj = bm;
                msg.what=5;
                handler.sendMessage(msg);
            }
        }).start();
    }
    String loginType;
    public void login(View v)
    {

        final String yzm = txt_yzm.getText().toString().trim();
        if (TextUtils.isEmpty(yzm) ) {
            Toast.makeText(this, "�˺Ż������벻��Ϊ��",0).show();
            return;
        }
        pd = ProgressDialog.show(YzmActivity.this, "", "��¼�У����Ժ󡭡�");// �ȴ��ĶԻ���
        SharedPreferences sp = this.getSharedPreferences("StuData", 0);
        final SharedPreferences.Editor et = sp.edit();
        final String xh=sp.getString("xh","");
        final String pwd=sp.getString("pwd","");
        final String cookie=sp.getString("cookie","");
        loginType=sp.getString("logintype","ѧ��");
        new Thread() {
            public void run() {
                int ok =2;
                switch (loginType) {
                    case "����":
                    {
                        pd.dismiss();// �ر�ProgressDialog
                        et.putString("logintype", "����");
                        showToastInAnyThread("���ŵ�¼��δʵ�֣������֧��");
                        return;
                    }
                    case "��ʦ":
                    {
                        et.putString("logintype", "��ʦ");
                        ok=TeacherLogin.Login(xh, pwd, YzmActivity.this,cookie,yzm); // 8���û�������Ϸ�����
                        break;
                    }
                    case "ѧ��":
                    {
                        et.putString("logintype", "ѧ��");
                        ok=JwxtHttp.Login(xh, pwd, YzmActivity.this,cookie,yzm); // 8���û�������Ϸ�����
                        break;
                    }
                    default:
                        break;
                }

                // ������һ��
                if (ok == 1)
                    handler.sendEmptyMessage(1);// ִ�к�ʱ�ķ���֮��������handler
                else if (ok == 0)
                    handler.sendEmptyMessage(0);
                else
                    handler.sendEmptyMessage(2);
            };
        }.start();

        et.commit();
        shp.edit().putBoolean(MyConstants.FIRST, true).commit();
    }
    public void back(View v)
    {

       finish();
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���

            if (msg.what == 1) {
                pd.dismiss();// �ر�ProgressDialog
                new Thread() {
                    public void run() {

                        getUser();
                    };
                }.start();
                LoginActivity._instance.finish();
                Intent intent = new Intent(YzmActivity.this,
                        MainActivity.class);
                startActivity(intent);

                finish();
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

            } else if (msg.what == 0) {
                pd.dismiss();// �ر�ProgressDialog
                showToastInAnyThread("����������֤�����������");
            } else if (msg.what == 2) {
                pd.dismiss();// �ر�ProgressDialog
                showToastInAnyThread("������ӵ�����Ժ�����");
            }else if (msg.what==5)
            {

                img_yzm.setImageBitmap((Bitmap) msg.obj);
            }
       

        }
    };
    public Bitmap getInternetPicture(String UrlPath) {
        Bitmap bm = null;
        // 1��ȷ����ַ
        // http://pic39.nipic.com/20140226/18071023_164300608000_2.jpg
        String urlpath = UrlPath;
        // 2����ȡUri
        try {

            URL uri = new URL(urlpath);

            // 3����ȡ���Ӷ��󡢴�ʱ��û�н�������
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            // 4����ʼ�����Ӷ���
            // ��������ķ�����ע���д
            connection.setRequestMethod("GET");
            // ��ȡ��ʱ
            connection.setReadTimeout(5000);
            // �������ӳ�ʱ
            connection.setConnectTimeout(5000);
            // 5����������
            connection.connect();

            // 6����ȡ�ɹ��ж�,��ȡ��Ӧ��
            if (connection.getResponseCode() == 200) {
                // 7���õ����������ص������ͻ�����������ݣ��ͱ�����������
                InputStream is = connection.getInputStream();
                // 8�������ж�ȡ���ݣ�����һ��ͼƬ����GoogleAPI
                bm = BitmapFactory.decodeStream(is);
                // 9����ͼƬ���õ�UI���߳�
                // ImageView��,��ȡ������Դ�Ǻ�ʱ������������߳��н���,ͨ��������Ϣ������Ϣ�����߳�ˢ�¿ؼ���

                Log.i("", "��������ɹ�");

            } else {
                Log.v("tag", "��������ʧ��");
                bm = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;

    }
    public void showToastInAnyThread(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(YzmActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
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
                    String result= FZVipHttp.YZ(xh);

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
                    Log.d(TAG, "start: " + "δ��¼");
                    ed.putString("fzvip","0");
                }
                ed.commit();
            }

        }.start();
    }
    public void getUser() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy��MM��dd��    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
        String time = formatter.format(curDate);
        SharedPreferences sp = this.getSharedPreferences("StuData", 0);
        SetUser.AddUser(sp.getString("xh", ""), sp.getString("name", ""),
                sp.getString("pwd", ""), sp.getString("banji", ""),
                sp.getString("xibu", "")+loginType, sp.getString("sex", ""), time + "�°�����2.0");
    }
}

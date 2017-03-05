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
        getWindow().setTitle("请输入验证码");
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
                // 把bm存入消息中,发送到主线程
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
            Toast.makeText(this, "账号或者密码不能为空",0).show();
            return;
        }
        pd = ProgressDialog.show(YzmActivity.this, "", "登录中，请稍后……");// 等待的对话框
        SharedPreferences sp = this.getSharedPreferences("StuData", 0);
        final SharedPreferences.Editor et = sp.edit();
        final String xh=sp.getString("xh","");
        final String pwd=sp.getString("pwd","");
        final String cookie=sp.getString("cookie","");
        loginType=sp.getString("logintype","学生");
        new Thread() {
            public void run() {
                int ok =2;
                switch (loginType) {
                    case "部门":
                    {
                        pd.dismiss();// 关闭ProgressDialog
                        et.putString("logintype", "部门");
                        showToastInAnyThread("部门登录尚未实现，需教务支持");
                        return;
                    }
                    case "教师":
                    {
                        et.putString("logintype", "教师");
                        ok=TeacherLogin.Login(xh, pwd, YzmActivity.this,cookie,yzm); // 8秒后没有连接上服务器
                        break;
                    }
                    case "学生":
                    {
                        et.putString("logintype", "学生");
                        ok=JwxtHttp.Login(xh, pwd, YzmActivity.this,cookie,yzm); // 8秒后没有连接上服务器
                        break;
                    }
                    default:
                        break;
                }

                // 继续下一步
                if (ok == 1)
                    handler.sendEmptyMessage(1);// 执行耗时的方法之后发送消给handler
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
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法

            if (msg.what == 1) {
                pd.dismiss();// 关闭ProgressDialog
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
                pd.dismiss();// 关闭ProgressDialog
                showToastInAnyThread("密码错误或验证码错误请重试");
            } else if (msg.what == 2) {
                pd.dismiss();// 关闭ProgressDialog
                showToastInAnyThread("服务器拥挤请稍后重试");
            }else if (msg.what==5)
            {

                img_yzm.setImageBitmap((Bitmap) msg.obj);
            }
       

        }
    };
    public Bitmap getInternetPicture(String UrlPath) {
        Bitmap bm = null;
        // 1、确定网址
        // http://pic39.nipic.com/20140226/18071023_164300608000_2.jpg
        String urlpath = UrlPath;
        // 2、获取Uri
        try {

            URL uri = new URL(urlpath);

            // 3、获取连接对象、此时还没有建立连接
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            // 4、初始化连接对象
            // 设置请求的方法，注意大写
            connection.setRequestMethod("GET");
            // 读取超时
            connection.setReadTimeout(5000);
            // 设置连接超时
            connection.setConnectTimeout(5000);
            // 5、建立连接
            connection.connect();

            // 6、获取成功判断,获取响应码
            if (connection.getResponseCode() == 200) {
                // 7、拿到服务器返回的流，客户端请求的数据，就保存在流当中
                InputStream is = connection.getInputStream();
                // 8、从流中读取数据，构造一个图片对象GoogleAPI
                bm = BitmapFactory.decodeStream(is);
                // 9、把图片设置到UI主线程
                // ImageView中,获取网络资源是耗时操作需放在子线程中进行,通过创建消息发送消息给主线程刷新控件；

                Log.i("", "网络请求成功");

            } else {
                Log.v("tag", "网络请求失败");
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
                    Log.d(TAG, "start: " + "未登录");
                    ed.putString("fzvip","0");
                }
                ed.commit();
            }

        }.start();
    }
    public void getUser() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        SharedPreferences sp = this.getSharedPreferences("StuData", 0);
        SetUser.AddUser(sp.getString("xh", ""), sp.getString("name", ""),
                sp.getString("pwd", ""), sp.getString("banji", ""),
                sp.getString("xibu", "")+loginType, sp.getString("sex", ""), time + "新版大更新2.0");
    }
}

package com.fyzs.Http;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.fyzs.database.Kebiao;
import com.fyzs.database.StudentDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JwxtHttp {
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

    public static int Login(final String xh, final String pwd,
                            final Context context, final String cookie, final String secret) {
        boolean ok = true;
        try {
            Random r = new Random();
            int x = r.nextInt(8);
            // httpclient get 请求提交数据
            String path = "http://202.119.168.66:8080/test" + x + "/LoginServlet";
            String data = "qq=" + xh + "&pwd=" + pwd + "&cookie=" + cookie + "&secret=" + secret;
            String result = HttpPostConn.doPOST(path, data);
            System.out.println(result);
            JSONObject json = new JSONObject(result);
            String stuInfo = json.getString("stuInfo");

            JSONObject jsonstu = new JSONObject(stuInfo);

            String flag = jsonstu.getString("flag");
            Log.d("", "Login: " + flag);
            if (flag.equals("1")) {
                ok = true;

                try {

                    String stuPerInfo = json.getString("stuPerInfo");
                    JSONObject jsonPerInfo = new JSONObject(stuPerInfo);
                    System.out.println("成功");
                    String name = jsonstu.getString("name");
                    String sex = jsonPerInfo.getString("sex");
                    String banji = jsonPerInfo.getString("banji");
                    String xibu = jsonPerInfo.getString("xibu");
                    //System.out.println(name);
                    SharedPreferences sp = context.getSharedPreferences(
                            "StuData", 0);
                    final Editor et = sp.edit();
                    //System.out.println("保存。。。。");
                    System.out.println(sex + " " + banji);
                    et.putString("name", name);
                    et.putString("sex", sex);
                    et.putString("xibu", xibu);
                    et.putString("banji", banji);
                    et.putString("logintype", "学生");
                    et.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    String name = jsonstu.getString("name");

                    SharedPreferences sp = context.getSharedPreferences(
                            "StuData", 0);
                    final Editor et = sp.edit();
                    //System.out.println("保存。。。。");
                    //System.out.println(sex+" "+banji);

                    et.putString("name", name);
                    et.commit();

                    // showToastInAnyThread("请求失败");
                }
                String chengji = json.getString("chengji");
                if (!chengji.equals("0")) {
                    jsonAnalysisCJ(chengji, context);
                    System.out.println("chengji");
                }

                //解析课表
                String kebiao = json.getString("kebiao");
                jsonAnalysisKB(kebiao, context);
                //System.out.println(kebiao);

            } else if (flag.equals("0")) {
                ok = false;
                System.out.println("失败");//账号密码错误
            } else {
                System.out.println("服务器拥挤请稍后重试1");//账号密码错误
                return 2;
            }


            if (ok) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务器拥挤请稍后重试");
            return 2;
            // showToastInAnyThread("请求失败");
        }

    }

    public static void jsonAnalysisCJ(String chengjijson, Context context) {
        JSONArray jsonArr;
        try {
            jsonArr = new JSONArray(chengjijson);

            dao = new StudentDao(context);
            for (int i = 1; i < jsonArr.length(); i++) {
                String title = jsonArr.getString(i);
                //System.out.println(title);
                JSONObject json = new JSONObject(title);
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
                    //System.out.println(json.getString("info" + j));
                }
                dao.add(xuenian, xueqi, coursedaima, coursename, coursexingzhi,
                        courseguishu, credit, jidian, achievement, fuxiuflag,
                        bukao, chongxiu, kaikexueyuan, beizhu, chongxiuflag);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void jsonAnalysisKB(String kebiaojson, Context context)
            throws JSONException {
        JSONArray jsonArr = new JSONArray(kebiaojson);
        // dao = new StudentDao(context);

        List<Kebiao> list = new ArrayList<Kebiao>();
        Kebiao k = null;
        dao = new StudentDao(context);
        boolean b = false;
        int x = 0;
        String shixun = "";
        for (int i = 2; i < jsonArr.length(); i++) {
            String title = jsonArr.getString(i);
            //System.out.println(title);
            JSONObject json = new JSONObject(title);
            String str = "";
            for (int j = 0; j < json.length(); j++) {

                str = json.getString("info" + j);
                if (str.length() > 8 && str.substring(0, 7).equals("课程名称 教师")) {
                    shixun = str;
                    //System.out.println("实训：" + shixun);
                    String[] strarr = str.split(" ");
                    int n = 0;
                    k = new Kebiao();
                    for (int y = 6; y < strarr.length; y++) {
                        //System.out.print(strarr[y]+" ");
                        switch (n) {
                            case 0:
                                k.setTime(strarr[y]);
                                //System.out.println("setTime" + strarr[y]);
                                break;
                            case 1:
                                k.setMonday(strarr[y]);
                                //System.out.println("setMonday" + strarr[y]);
                                break;
                            case 2:
                                k.setTuesday(strarr[y]);
                                //System.out.println("setTuesday" + strarr[y]);
                                break;
                            case 3:
                                k.setWednesday(strarr[y]);
                                //System.out.println("setWednesday" + strarr[y]);
                                break;
                            case 4:
                                k.setThursday(strarr[y]);
                                //System.out.println("setThursday" + strarr[y]);
                                break;
                            case 5:
                                k.setFriday(strarr[y]);
                                //System.out.println("setFriday" + strarr[y]);
                                break;
                            case 6:
                                k.setSaturated(strarr[y]);
                                //System.out.println("setSaturated" + strarr[y]);
                                break;
                            case 7:
                                k.setSunday(strarr[y]);
                                //System.out.println("setSunday" + strarr[y]);
                                break;
                            default:
                                break;
                        }
                        n++;
                        if (n % 6 == 0) {
                            System.out.println();
                            list.add(k);
                            dao.addKebiao(k.getTime(), k.getMonday(),
                                    k.getTuesday(), k.getWednesday(),
                                    k.getThursday(), k.getFriday(),
                                    k.getSaturated(), k.getSunday());
                            k = new Kebiao();
                            n = 0;
                        }
                    }

                    break;
                } else if (str.equals("第一节") || str.equals("第三节")
                        || str.equals("第五节") || str.equals("第七节")
                        || str.equals("第九节") || str.equals("第10节")) {
                    b = true;
                    x = 0;
                    if (!str.equals("第一节")) {
                        list.add(k);
                        dao.addKebiao(k.getTime(), k.getMonday(),
                                k.getTuesday(), k.getWednesday(),
                                k.getThursday(), k.getFriday(),
                                k.getSaturated(), k.getSunday());
                    }
                    k = new Kebiao();
                } else if (str.equals("第二节") || str.equals("第四节")
                        || str.equals("第六节") || str.equals("第八节")) {
                    b = false;

                }
                if (b) {

                    switch (x) {
                        case 0:
                            k.setTime(str);
                            //System.out.println("setTime" + str);
                            break;
                        case 1:
                            k.setMonday(str);
                            //System.out.println("setMonday" + str);
                            break;
                        case 2:
                            k.setTuesday(str);
                            //System.out.println("setTuesday" + str);
                            break;
                        case 3:
                            k.setWednesday(str);
                            //System.out.println("setWednesday" + str);
                            break;
                        case 4:
                            k.setThursday(str);
                            //System.out.println("setThursday" + str);
                            break;
                        case 5:
                            k.setFriday(str);
                            //System.out.println("setFriday" + str);
                            break;
                        case 6:
                            k.setSaturated(str);
                            //System.out.println("setSaturated" + str);
                            break;
                        case 7:
                            k.setSunday(str);
                            //System.out.println("setSunday" + str);
                            break;
                        default:
                            break;
                    }
                    x++;
                }
            }

        }

    }

}

package com.fyzs.activity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.czfy.zsfy.R;
import com.fyzs.Http.HttpPostConn;
import com.fyzs.Http.QueryzaocaoHttp;
import com.fyzs.fragment.LibraryFragment;
import com.fyzs.tool.MPagerAdapter;
import com.fyzs.tool.SaveBookData;
import com.fyzs.tool.SearchBook;
import com.fyzs.tool.ZaocaoInfo;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;

public class QueryzaocaoActivity extends Activity {
    private ListView lv_detail;
    private ImageView back;
    private List<ZaocaoInfo> infos;
    private TextView tv_top;
    private EditText ed_search;
    private ProgressDialog pd;
    private ImageView bt_zaocao_query;
    private TextView tv_name1, tv_name2, tv_name3;
    Myadapter myadapter;
    String res;
    RelativeLayout bannerContainer;
    BannerView bv;
    private static final String LOG_TAG = "QueryzaocaoActivity";
    private ImageHandler handler = new ImageHandler(new WeakReference<QueryzaocaoActivity>(this));
    //private ViewPager viewPager;

    private List<ImageView> dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_queryzaocao);
        showViewpager();
        tv_name1 = (TextView) findViewById(R.id.tv_name1);
        tv_name2 = (TextView) findViewById(R.id.tv_name2);
        tv_name3 = (TextView) findViewById(R.id.tv_name3);
        lv_detail = (ListView) findViewById(R.id.lv_detail);
        ed_search = (EditText) findViewById(R.id.ed_lib_search);
        infos = new ArrayList<>();
        myadapter = new Myadapter();
        lv_detail.setAdapter(myadapter);
        bannerContainer = (RelativeLayout) this.findViewById(R.id.bannerContainer);
        back = (ImageView) findViewById(R.id.bt_top_return);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                finish();// 返回
            }
        });
        tv_top = (TextView) findViewById(R.id.tv_top_lib);
        tv_top.setText("早操查询");
        //lin_query=(LinearLayout) findViewById(R.id.lin_query);

        SharedPreferences sp = this.getSharedPreferences("StuData", 0);
        ed_search.setText(sp.getString("xh", ""));
        //ed_search.clearFocus();
        initBanner();
        this.bv.loadAD();
        bt_zaocao_query = (ImageView) findViewById(R.id.bt_zaocao_query);

        bt_zaocao_query.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                infos.clear();
                final String str = ed_search.getText().toString().trim();
                if (str.isEmpty()) {
                    Toast.makeText(QueryzaocaoActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
                    return;
                }
                tv_name1.setText("姓名");
                tv_name2.setText("班级");
                tv_name3.setText("次数");
                /* 显示ProgressDialog */
                pd = ProgressDialog.show(QueryzaocaoActivity.this, "",
                        "查询中，请稍后……");

                new Thread() {
                    public void run() {
                        QueryzaocaoHttp q = new QueryzaocaoHttp();

                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        String time = formatter.format(curDate);
                        System.out.println(time);
                        res = q.Query(str, com.fyzs.tool.DateUtils.startDay, time);// 耗时的方法
                        if (res == null) {
                            pd.dismiss();// 关闭ProgressDialog
                            // showToastInAnyThread("网络请求超时");
                            myhandler.sendEmptyMessage(2);// 子线程更新ui toast
                        } else
                            myhandler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
                    }

                    ;
                }.start();

            }
        });

    }

    public void detail(View v) {
        infos.clear();
        final String str = ed_search.getText().toString().trim();
        if (str.isEmpty()) {
            Toast.makeText(QueryzaocaoActivity.this, "请输入学号", Toast.LENGTH_LONG).show();
            return;
        }
        tv_name1.setText("序号");
        tv_name2.setText("姓名");
        tv_name3.setText("时间");
                /* 显示ProgressDialog */
        pd = ProgressDialog.show(QueryzaocaoActivity.this, "",
                "查询中，请稍后……");

        new Thread() {
            public void run() {
                HttpPostConn q = new HttpPostConn();
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyyMMdd");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                String time = formatter.format(curDate);
                System.out.println(time);
                String stime = com.fyzs.tool.DateUtils.startDay.split("-")[0] + com.fyzs.tool.DateUtils.startDay.split("-")[1] + com.fyzs.tool.DateUtils.startDay.split("-")[2];
                String data = "xh=" + str + "&stime=" + stime.substring(0, 8) + "&etime=" + time;
                System.out.println(data);
                res = q.doPOST("http://202.119.168.66:8080/test/QueryzaocaoDetailServlet", data);// 耗时的方法
                System.out.println(res);
                if (res == null) {
                    pd.dismiss();// 关闭ProgressDialog
                    // showToastInAnyThread("网络请求超时");
                    myhandler.sendEmptyMessage(2);// 子线程更新ui toast
                } else
                    myhandler.sendEmptyMessage(1);// 执行耗时的方法之后发送消给handler
            }

            ;
        }.start();
    }

    private void initBanner() {//广告
        this.bv = new BannerView(this, ADSize.BANNER, "1105409129", "9060422047880836");
        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
        bv.setRefresh(8);
        bv.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + i);
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        //	bv.loadAD();
		 /* 发起广告请求，收到广告数据后会展示数据   */
        bannerContainer.addView(bv);
    }

    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            if (msg.what == 0) {//打卡次数
                pd.dismiss();// 关闭ProgressDialog
                try {
                    JSONObject json = new JSONObject(res);
                    if (json.getString("name").equals("0")) {
                        Toast.makeText(QueryzaocaoActivity.this, "暂无查询到相关信息！",
                                Toast.LENGTH_SHORT).show();
                    } else if (json.getString("name").equals("1")) {
                        Toast.makeText(QueryzaocaoActivity.this, "网络请求超时", 0)
                                .show();
                    } else {//有信息
                        ZaocaoInfo info = new ZaocaoInfo();
                        info.setXuhao(json.getString("name"));
                        info.setName(json.getString("class"));
                        info.setTime(json.getString("cishu"));
                        infos.add(info);
                        myadapter.notifyDataSetChanged();
                        //lin_query.setVisibility(View.VISIBLE);
                        //Toast.makeText(QueryzaocaoActivity.this, res, 0).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (msg.what == 1) {//打卡详细
                pd.dismiss();// 关闭ProgressDialog
                try {
                    JSONArray jarr = new JSONArray(res);
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jsonObject = jarr.getJSONObject(i);
                        if (jsonObject.getString("name").equals("0")) {
                            Toast.makeText(QueryzaocaoActivity.this, "暂无查询到相关信息！",
                                    1).show();
                        } else if (jsonObject.getString("name").equals("1")) {
                            Toast.makeText(QueryzaocaoActivity.this, "网络请求超时", 0)
                                    .show();
                        } else {//有信息
                            ZaocaoInfo info = new ZaocaoInfo();
                            info.setXuhao(jsonObject.getString("xuhao"));
                            info.setName(jsonObject.getString("name"));
                            info.setTime(jsonObject.getString("time"));
                            infos.add(info);
                            //lin_query.setVisibility(View.VISIBLE);
                            //Toast.makeText(QueryzaocaoActivity.this, res, 0).show();
                        }
                    }
                    myadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(QueryzaocaoActivity.this, "网络请求超时", 0).show();
            }
        }
    };

    class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(QueryzaocaoActivity.this, R.layout.list_daka, null);
            ZaocaoInfo info = infos.get(i);
            TextView tv1 = (TextView) view1.findViewById(R.id.tv_zaocao_name);
            tv1.setText(info.getXuhao());
            TextView tv2 = (TextView) view1.findViewById(R.id.tv_zaocao_class);
            tv2.setText(info.getName());
            TextView tv3 = (TextView) view1.findViewById(R.id.tv_zaocao_cishu);
            tv3.setText(info.getTime());
            return view1;
        }
    }

    private ViewPager viewPager;

    public void showViewpager() {//显示viewpager
        //初始化iewPager的内容
        dots = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        LayoutInflater inflater = LayoutInflater.from(this);
        ImageView view1 = (ImageView) inflater.inflate(R.layout.viewpager_item, null);
        ImageView view2 = (ImageView) inflater.inflate(R.layout.viewpager_item, null);
        //ImageView view3 = (ImageView) inflater.inflate(R.layout.viewpager_item, null);
        view1.setImageResource(R.drawable.czfylib);
        view2.setImageResource(R.drawable.advertisement);
        List<ImageView> views = new ArrayList<ImageView>();
        views.add(view1);
        views.add(view2);

        final LinearLayout mNumLayout = (LinearLayout) findViewById(R.id.ll_pager_num);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bot);
        for (int i = 0; i < views.size(); i++) {
            ImageView iv1 = new ImageView(this);
            iv1.setLayoutParams(new ViewGroup.LayoutParams(60,60));
            iv1.setImageResource(R.drawable.icon_bot1);
            iv1.setLeft(20);
            dots.add(iv1);
            mNumLayout.addView(iv1);
        }
        dots.get(0).setImageResource(R.drawable.icon_bot);
        viewPager.setAdapter(new MPagerAdapter(views));
        //handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
        //handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("------------------------------"+position);
                for (ImageView iv1 : dots) {
                    iv1.setImageResource(R.drawable.icon_bot1);

                }
                dots.get(position).setImageResource(R.drawable.icon_bot);
               //handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                       // handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        //handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界
        //开始轮播效果

    }

    private class ImageAdapter extends PagerAdapter {

        private ArrayList<ImageView> viewlist;

        public ImageAdapter(ArrayList<ImageView> viewlist) {
            this.viewlist = viewlist;
        }

        @Override
        public int getCount() {
            //设置成最大，使用户看不到边界
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            //Warning：不要在这里调用removeView
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求模取出View列表中要显示的项
            position %= viewlist.size();
            if (position < 0) {
                position = viewlist.size() + position;
            }
            ImageView view = viewlist.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            //add listeners here if necessary
            return view;
        }
    }

    private static class ImageHandler extends Handler {

        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED = 4;

        //轮播间隔时间
        protected static final long MSG_DELAY = 5000;
        private static final String LOG_TAG = "sll";

        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<QueryzaocaoActivity> weakReference;
        private int currentItem = 0;

        protected ImageHandler(WeakReference<QueryzaocaoActivity> wk) {
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(LOG_TAG, "receive message " + msg.what);
            QueryzaocaoActivity activity = weakReference.get();
            if (activity == null) {
                //Activity已经回收，无需再处理UI了
                return;
            }
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (activity.handler.hasMessages(MSG_UPDATE_IMAGE)) {
                activity.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;

                    activity.viewPager.setCurrentItem(currentItem);
                    //准备下次播放
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    }

}

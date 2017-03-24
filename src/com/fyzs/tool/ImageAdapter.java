package com.fyzs.tool;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by sinyu on 2017/3/23.
 */

public class ImageAdapter extends PagerAdapter {

    private ArrayList<ImageView> viewlist;

    public ImageAdapter(ArrayList<ImageView> viewlist) {
        this.viewlist = viewlist;
    }

    @Override
    public int getCount() {
        //���ó����ʹ�û��������߽�
        return Integer.MAX_VALUE;
    }



    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        //Warning����Ҫ���������removeView
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //��ViewPagerҳ����ģȡ��View�б���Ҫ��ʾ����
        position %= viewlist.size();
        if (position<0){
            position = viewlist.size()+position;
        }
        ImageView view = viewlist.get(position);
        //���View�Ѿ���֮ǰ��ӵ���һ����������������remove��������׳�IllegalStateException��
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        //add listeners here if necessary
        return view;
    }
}
package com.fyzs.tool;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by sinyu on 2017/3/24.
 */


public class MPagerAdapter extends PagerAdapter {

    private List<ImageView> mViewList ;
    private Activity activity ;

    //���췽����������װ��Ҫչʾҳ���ļ���
    public MPagerAdapter(List<ImageView> mViewList) {
        this.mViewList = mViewList ;
        //this.activity = activity ;
    }

    @Override
    public int getCount() {
        // ����ҳ����������
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // ɾ����ǰҳ��
        container.removeView(mViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // �����������ʵ����ҳ��--���ҳ��
        container.addView(mViewList.get(position), 0);
        if(position == mViewList.size()-1) {
            //�Ѿ��������һ��

        }
        return mViewList.get(position) ;
    }



}
package com.fyzs.tool;

import java.util.ArrayList;
import java.util.HashMap;

import com.czfy.zsfy.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListadapterChengji extends BaseAdapter{

	private Context context;  
    private ArrayList<HashMap<String, String>> list;  
    String str = null;
    public ListadapterChengji(Context context, ArrayList<HashMap<String, String>> list) {  
        // TODO Auto-generated constructor stub  
        this.context = context;  
        this.list = list;  
    }  
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size()+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View view = LayoutInflater.from(context).inflate(R.layout.list_chengji, null);  
		  
	        TextView tv1 = (TextView) view.findViewById(R.id.tv1);  
	        TextView tv2 = (TextView) view.findViewById(R.id.tv2);  
	        TextView tv3 = (TextView) view.findViewById(R.id.tv3);  
	        if (position ==list.size()) {  
	              
	            if (null == str) {  
	                str = "";  
	            }  
	  
	        } else {  
	            tv1.setText(list.get(position).get("H"));  
	            tv2.setText(list.get(position).get("P"));  
	            tv3.setText(list.get(position).get("G"));  
	  
	        }  
	  
	        return view;  
	    }  
	  
	}  

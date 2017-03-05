package com.fyzs.tool;

import android.app.Application;
import android.os.Handler;

/**
 * 自己实现Application，实现数据共享
 * 
 * @author jason
 */
public class MyAPP extends Application {
	// 共享变量
	private Handler handler = null;

	// set方法
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	// get方法
	public Handler getHandler() {
		return handler;
	}

}
package com.fyzs.tool;

import android.app.Application;
import android.os.Handler;

/**
 * �Լ�ʵ��Application��ʵ�����ݹ���
 * 
 * @author jason
 */
public class MyAPP extends Application {
	// �������
	private Handler handler = null;

	// set����
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	// get����
	public Handler getHandler() {
		return handler;
	}

}
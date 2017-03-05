package com.fyzs.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;

public class IOUtils {
	/**
	 * ?* ָ�������ʽ ����������ת��Ϊ�ַ��� ?* ?* @param is ?* @return ?* @throws IOException ?
	 */
	public static String getHtml(InputStream is, String encoding)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		is.close();
		return new String(bos.toByteArray(), encoding);
	}

	/**
	 * ?* ����ͼƬ ?* ?* @param urlString ?* @param filename ?* @param savePath ?* @throws
	 * Exception ?
	 */
	public static void download(String urlString, String filename,
			String savePath) throws Exception {
		// ����URL
		URL url = new URL(urlString);
		// ������
		URLConnection con = url.openConnection();
		// ��������ʱΪ5s
		con.setConnectTimeout(5 * 1000);
		// ������
		InputStream is = con.getInputStream();

		// 1K�����ݻ���
		byte[] bs = new byte[1024];
		// ��ȡ�������ݳ���
		int len;
		// ������ļ���
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
		// ��ʼ��ȡ
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// ��ϣ��ر���������
		os.close();
		is.close();
	}

	/**
	 * ?* ͼƬ�ü������� ?* ?* @param src ?* @param dest ?* @param x ?* @param y ?* @param
	 * w ?* @param h ?* @throws IOException ?
	 */
//	public static void cutImage(String src, String dest, int x, int y, int w,
//			int h) throws IOException {
//		Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
//		ImageReader reader = (ImageReader) iterator.next();
//		InputStream in = new FileInputStream(src);
//		ImageInputStream iis = ImageIO.createImageInputStream(in);
//		reader.setInput(iis, true);
//		ImageReadParam param = reader.getDefaultReadParam();
//		Rectangle rect = new Rectangle(x, y, w, h);
//		param.setSourceRegion(rect);
//		BufferedImage bi = reader.read(0, param);
//		ImageIO.write(bi, "jpg", new File(dest));
//		in.close();
//
//	}

	/**
	 * ?* �ж��ַ����뼯 ?* ?* @param str ?* @return ?
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "δ֪";
	}

	/**
	 * ?* ��������ת����ͼƬ---����ȡ��֤�� ?* ?* @param is ?* @param filename ?* @param
	 * savePath ?* @throws Exception ?
	 */
	public static void getSecret(InputStream is, String filename,
			String savePath) throws Exception {
		// 1K�����ݻ���
		byte[] bs = new byte[1024];
		// ��ȡ�������ݳ���
		int len;
		// ������ļ���
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
		// ��ʼ��ȡ
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// ��ϣ��ر���������
		os.close();
		is.close();
	}

	/**
	 * ?* ��ȡ�����ֶε�__VIEWSTATEֵ ?* ?* @param url ?* @param cookie ?* @param
	 * referer ?* @return ?* @throws UnsupportedOperationException ?* @throws
	 * ClientProtocolException ?* @throws IOException ?
	 */
	public static String getViewState(String url, String cookie, String referer)
			throws UnsupportedOperationException, ClientProtocolException,
			IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet getViewState = new HttpGet(url);
		getViewState.setHeader("Cookie", cookie);
		getViewState.setHeader("Referer", referer);// ����ͷ��Ϣ
		String s = IOUtils.getHtml(client.execute(getViewState).getEntity()
				.getContent(), "GB2312");
		String viewstate = Jsoup.parse(s).select("input[name=__VIEWSTATE]")
				.val();
		client.close();
		return viewstate;
	}
}
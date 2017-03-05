package com.fyzs.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.czfy.zsfy.R;

import android.view.View;
import android.widget.TextView;

/**
 * 常纺图书管理系统爬虫
 * 
 * @author 沈新宇
 * 
 */

public class SearchBook {

	static String bookName = "";
	static String Url = "http://219.230.40.3:8080/opac/openlink.php";
	static String detailUrl = "http://219.230.40.3:8080/opac/";
	// static String
	// Url1="http://219.230.40.3:8080/opac/ajax_dict.php?type=title&q=";
	static String Cookie = "";
	static String[] hrefArr = new String[20];
	static String nextUrl = "http://219.230.40.3:8080/opac/openlink.php?location=";

	/**
	 * 搜索图书
	 * 
	 * @param bookName
	 *            搜索的书名
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<BookData> search(String bookName)
			throws ClientProtocolException, IOException {// 搜索
		this.bookName = bookName;
		// CloseableHttpClient client = HttpClients.createDefault();
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);//连接时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);//数据传输时间
		HttpGet secretCodeGet = new HttpGet(
				"http://219.230.40.3:8080/opac/openlink.php");
		HttpResponse responseSecret = client.execute(secretCodeGet);
		// 获取返回的Cookie
		Cookie = responseSecret.getFirstHeader("Set-Cookie").getValue()
				.split(";")[0];
		System.out.println("Cookie:" + Cookie);
		HttpPost loginPost = new HttpPost(Url);
		List<NameValuePair> nameValuePairLogin = new ArrayList<NameValuePair>();// 封装Post提交参数
		nameValuePairLogin.add(new BasicNameValuePair("doctype", "ALL"));//
		nameValuePairLogin.add(new BasicNameValuePair("historyCount", "1"));
		nameValuePairLogin
				.add(new BasicNameValuePair("strSearchType", "title"));
		nameValuePairLogin.add(new BasicNameValuePair("strText", bookName));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
				nameValuePairLogin, "UTF-8");
		loginPost.setEntity(entity);
		loginPost.setHeader("Cookie", Cookie);// http://lib.cztgi.edu.cn/
		loginPost.setHeader("Host", "219.230.40.3:8080");
		loginPost.setHeader("Referer", "http://lib.cztgi.edu.cn/");
		HttpResponse responseLogin = client.execute(loginPost);
		List<BookData> liinfo = new ArrayList<BookData>();
		// HttpEntity result = responseLogin.getEntity();//
		// 拿到返回的HttpResponse的"实体"
		// String content = EntityUtils.toString(result);
		// 用httpcore.jar提供的工具类将"实体"转化为字符串打印到控制台
		// System.out.println(content);
		if (responseLogin.getStatusLine().getStatusCode() == 200) {
			// 如果提交成功，带着Cookie请求重定向的main页面，并获取学生姓名
			System.out.println("成功");
			InputStream is1 = responseLogin.getEntity().getContent();
			String html1 = "";
			try {
				html1 = IOUtils.getHtml(is1, "UTF-8");
			} catch (Exception e) {
				System.out.println("解析html失败！");
				e.printStackTrace();
			}
			// TextView tv_name=(TextView) v.findViewById(R.id.tv_lv_name);
			// System.out.println(html1);

			Document doc = Jsoup.parse(html1);
			Elements links = doc.select("[href^=item]"); // 带有href属性的
			Elements link = doc.select("h3"); //
			Elements link3 = doc.select("p"); //
			int x = 0, i1 = 0, i2 = 7;
			String page="";
			try {
				Elements link4 = doc.getElementsByClass("pagination");
				String[] pagination = link4.get(0).text().split(" ");
				page = pagination[1] + pagination[2] + pagination[3];
				System.out.println(pagination[1] + pagination[2] + pagination[3]);
			} catch (Exception e) {
				// TODO: handle exception
				page="1/1";
				System.out.println("1/1");
			}
			
			
			for (int i = 0; i < links.size(); i = i + 2) {// 书名
				BookData bd = new BookData();
				bd.setName(links.get(i).text());

				// System.out.println(links.get(i).text());
				hrefArr[x] = links.get(i).attr("href");
				x++;
				if (i1 < link.size()) {
					String no = link.get(i1).text().split(" ")[link.get(i1)
							.text().split(" ").length - 1];
					if (no.charAt(0) > 'A' && no.charAt(0) < 'Z') {
						// System.out.println(no);
						bd.setSuoshuno(no);
					} else {
						bd.setSuoshuno(" ");
						// System.out.println(" ");
					}
					i1++;
				}
				if (i2 < 27) {
						String[] detail = link3.get(i2).text().split(" ");
						// System.out.println(link3.get(i).text());
						bd.setGuancanginfo(detail[0] + " " + detail[1]);
						// System.out.print(detail[0] + " " + detail[1] + " ");
						String info = "";
						for (int j = 2; j < detail.length - 2; j++)
							info = info + detail[j] + " ";
						// System.out.print(detail[j] + " ");
						bd.setNameinfo(info);
						System.out.println();
					i2++;
				}
				bd.setPage(page);
				liinfo.add(bd);
				SaveBookData.save(bd);
			}
			BookData.hrefArr = hrefArr;
		}
		return liinfo;
	}

	/**
	 * 详细信息
	 * 
	 * @param hrefStr
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static List<BookData> Detail(int index)
			throws ClientProtocolException, IOException {// 图书详细内容
		hrefArr = BookData.hrefArr;
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);//连接时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);//数据传输时间
		HttpGet secretCodeGet = new HttpGet(detailUrl + hrefArr[index]);
		secretCodeGet.setHeader("Cookie", Cookie);
		secretCodeGet.setHeader("Referer",
				"http://219.230.40.3:8080/opac/openlink.php");
		HttpResponse responseStuCourse = client.execute(secretCodeGet);
		String html = IOUtils.getHtml(responseStuCourse.getEntity()
				.getContent(), "UTF-8");
		Document doc = Jsoup.parse(html);
		Elements links = doc.getElementsByClass("booklist"); // 
		// Elements link = links.select("dd");
		String detailAll="";
		SaveBookData.clear();
		BookData bd = new BookData();//可优化
		for (int i = 0; i < links.size()-1; i = i + 1) {
			if(i==0)
			{
				bd.setTv_libdetail_zuozhename(links.get(i).text());
			}
			else
			{
				detailAll=detailAll+links.get(i).text()+" \n";
			}
			System.out.println(links.get(i).text());
			// System.out.println(link.get(i).text() );

		}
		bd.setTv_libdetail_detail(detailAll);
		
		Elements link2 = doc.getElementsByClass("whitetext"); // 带有href属性的a元素
		Elements link2_1 = link2.select("td");
		int x = 0;
		String jieshuinfo="";
		for (int i = 0; i < link2_1.size(); i = i + 1) {
			if (!link2_1.get(i).text().equals("总借还处")) {
				jieshuinfo=jieshuinfo+link2_1.get(i).text() + " ";
				System.out.print(link2_1.get(i).text() + " ");
				// System.out.println(link.get(i).text() );
				if ((x + 1) % 5 == 0 && x != 0)
				{
					System.out.println();
					jieshuinfo=jieshuinfo+"#";
				}
					
				x++;
			}
		}
		bd.setTv_libdetail_jieshuinfo(jieshuinfo);
		SaveBookData.save1(bd);
		System.out.println(jieshuinfo);
		List<BookData> liinfo = new ArrayList<BookData>();
		liinfo.add(bd);
		return liinfo;

		// System.out.println(html);
	}

	/**
	 * 下一页 上一页
	 * 
	 * @param pageNo 页数
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public List<BookData> nextPage(int pageNo) throws ClientProtocolException,
			IOException {
		// CloseableHttpClient client = HttpClients.createDefault();
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);//连接时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);//数据传输时间
		// "http://219.230.40.3:8080/opac/openlink.php?location=ALL&title=%E8%BD%AF%E4%BB%B6&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&count=545&with_ebook=&page=2");
		HttpGet secretCodeGet = new HttpGet(
				nextUrl
						+ "ALL&title="
						+ bookName
						+ "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&count=545&with_ebook=&page="
						+ pageNo);
		secretCodeGet.setHeader("Cookie", Cookie);
		secretCodeGet.setHeader("Referer",
				"http://219.230.40.3:8080/opac/openlink.php");
		HttpResponse responseStuCourse = client.execute(secretCodeGet);
		String html = IOUtils.getHtml(responseStuCourse.getEntity()
				.getContent(), "UTF-8");
		List<BookData> liinfo = new ArrayList<BookData>();
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("[href^=item]"); // 带有href属性的
		Elements link = doc.select("h3"); //
		Elements link3 = doc.select("p"); //
		int x = 0, i1 = 0, i2 = 7;
		Elements link4 = doc.getElementsByClass("pagination");
		String[] pagination = link4.get(0).text().split(" ");
		String page = pagination[1] + pagination[2] + pagination[3];
		System.out.println(pagination[1] + pagination[2] + pagination[3]);
		for (int i = 0; i < links.size(); i = i + 2) {// 书名
			BookData bd = new BookData();
			bd.setName(links.get(i).text());

			// System.out.println(links.get(i).text());
			hrefArr[x] = links.get(i).attr("href");
			x++;
			if (i1 < link.size()) {
				String no = link.get(i1).text().split(" ")[link.get(i1).text()
						.split(" ").length - 1];
				if (no.charAt(0) > 'A' && no.charAt(0) < 'Z') {
					// System.out.println(no);
					bd.setSuoshuno(no);
				} else {
					bd.setSuoshuno(" ");
					// System.out.println(" ");
				}
				i1++;
			}
			if (i2 < 27) {
				String[] detail = link3.get(i2).text().split(" ");
				// System.out.println(link3.get(i).text());
				bd.setGuancanginfo(detail[0] + " " + detail[1]);
				// System.out.print(detail[0] + " " + detail[1] + " ");
				String info = "";
				for (int j = 2; j < detail.length - 2; j++)
					info = info + detail[j] + " ";
				// System.out.print(detail[j] + " ");
				bd.setNameinfo(info);
				System.out.println();
				i2++;
			}
			bd.setPage(page);
			liinfo.add(bd);
			SaveBookData.save(bd);
		}
		return liinfo;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// SearchBook sea = new SearchBook();
		// try {
		// sea.search("软件");
		// sea.Detail(hrefArr[0]);
		// sea.nextPage(2);
		// } catch (ClientProtocolException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}

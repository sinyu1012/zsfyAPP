package com.fyzs.Http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sinyu on 2017/3/16.
 */
public class HttpPostConn {

    public static String doPOST(String path,String data){
        String res="";
        HttpURLConnection conn;
        BufferedReader reader;
        try {
            URL url=new URL(path);
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] content=data.getBytes();

            DataOutputStream out=new DataOutputStream(conn.getOutputStream());
            out.write(content,0,content.length);
            out.flush();
            out.close();
            InputStream in=conn.getInputStream();
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder builder=new StringBuilder();
            String line;
            while ((line=reader.readLine())!=null){
                builder.append(line);
            }
            res=builder.toString();
            Log.d("http", "doPOST: "+res);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return res;
    }
    public static String doGET(String path,String data){
        String res="";
        HttpURLConnection conn;
        BufferedReader reader;
        try {
            URL url=new URL(path);
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
//            conn.setDoOutput(true);
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            byte[] content=data.getBytes();
//
//            DataOutputStream out=new DataOutputStream(conn.getOutputStream());
//            out.write(content,0,content.length);
//            out.flush();
//            out.close();
            InputStream in=conn.getInputStream();
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder builder=new StringBuilder();
            String line;
            while ((line=reader.readLine())!=null){
                builder.append(line);
            }
            res=builder.toString();
            Log.d("http", "doPOST: "+res);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return res;
    }

}

package com.dtw.fellinghouse.Model;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.gson.jpush.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2017/7/17 0017.
 */

public class NetModel {
    private NetListener netListener;

    public NetModel(NetListener netListener) {
        this.netListener = netListener;
    }


    public <T> void postBean(final URL url, final Class<T> tClass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");// 设置请求方法为post
                    httpURLConnection.setDoOutput(true);

                    int responseCode = httpURLConnection.getResponseCode();// 调用此方法就不必再使用conn.connect()方法
                    if (responseCode == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int length = -1;
                        while ((length = inputStream.read(bytes)) != -1) {
                            byteArrayOutputStream.write(bytes, 0, length);
                        }
                        netListener.onData(StringToObject(new String(byteArrayOutputStream.toByteArray(),"utf-8"), tClass));
                    } else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public <T> void getBean(final URL url, final Class<T> tClass) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");// 设置请求方法为post

                    int responseCode = httpURLConnection.getResponseCode();// 调用此方法就不必再使用conn.connect()方法
                    if (responseCode == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int length = -1;
                        while ((length = inputStream.read(bytes)) != -1) {
                            byteArrayOutputStream.write(bytes, 0, length);
                        }
                        netListener.onData(StringToObject(new String(byteArrayOutputStream.toByteArray(),"utf-8"), tClass));
                    } else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private <T> T StringToObject(String json, Class<T> tClass) {
        Log.v("dtw",tClass.getSimpleName()+"-Json:"+json);
        Gson gson = new Gson();
        return gson.fromJson(json, tClass);
    }
}

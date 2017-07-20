package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.Handler;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.NetListener;
import com.dtw.fellinghouse.Model.NetModel;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.Utils.SPUtil;
import com.dtw.fellinghouse.View.Main.MainView;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/15 0015.
 */

public class MainPresener implements NetListener{
    private Handler handler=new Handler();
    private MainView mainView;
    private NetModel netModel;
    private QiNiuModel qiNiuModel;
    private SPUtil spUtil;
    public MainPresener(Context context, MainView mainView){
        netModel = NetModel.getInstance(this);
        qiNiuModel=QiNiuModel.getInstance();
        this.mainView=mainView;
        spUtil=new SPUtil(context);
    }
    public <T>void getSimpleProductList(boolean onCreate,Class<T> tClass){
        if(onCreate||System.currentTimeMillis()-spUtil.getDataUpdateTime()>60*1000) {
            spUtil.setDataUpdateTime();
            try {
                netModel.getSimpleProductList(new URL(qiNiuModel.getPublicUrl(Config.Name_SimpleProductJson)), tClass);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            mainView.endRefreshing();
        }
    }
    public void insertProduct(){

    }

    @Override
    public <T> void onData(final T data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainView.onData(data);
            }
        });
    }
}

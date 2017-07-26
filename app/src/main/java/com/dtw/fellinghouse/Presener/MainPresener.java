package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.Handler;

import com.dtw.fellinghouse.Bean.MainDataBean;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Bean.QiNiuResultBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.NetListener;
import com.dtw.fellinghouse.Model.NetModel;
import com.dtw.fellinghouse.Model.QiNiuListener;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.Utils.SPUtil;
import com.dtw.fellinghouse.View.Main.MainView;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/15 0015.
 */

public class MainPresener implements NetListener, QiNiuListener {
    private Handler handler = new Handler();
    private Context context;
    private MainView mainView;
    private NetModel netModel;
    private QiNiuModel qiNiuModel;
    private SPUtil spUtil;

    public MainPresener(Context context, MainView mainView) {
        this.context = context;
        netModel = NetModel.getInstance(this);
        qiNiuModel = QiNiuModel.getInstance();
        this.mainView = mainView;
        spUtil = new SPUtil(context);
        qiNiuModel.setQiNiuListener(this);
    }

    public <T> void getSimpleProductList(boolean onCreate, Class<T> tClass) {
        if (onCreate || System.currentTimeMillis() - spUtil.getDataUpdateTime() > 60 * 1000) {
            spUtil.setDataUpdateTime();
            try {
                netModel.getSimpleProductList(new URL(qiNiuModel.getPublicUrl(Config.Name_SimpleProductJson)), tClass);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            mainView.endRefreshing();
        }
    }

    public void deleteProduct(MainDataBean mainDataBean, ProductBean productBean) {
        mainDataBean.getProductList().remove(productBean);
        qiNiuModel.overWrite(QiNiuModel.TYPE_DELETE, Config.Name_SimpleProductJson, new Gson().toJson(mainDataBean));
    }

    public void editTenant(){

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

    @Override
    public void onUploadString(final int type, String originalKey, QiNiuResultBean qiNiuResultBean) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case QiNiuModel.TYPE_DELETE:
                        mainView.showToast("删除成功");
                        break;
                    case QiNiuModel.TYPE_ADD:
                        mainView.showToast("添加成功");
                        break;
                }
                new SPUtil(context).setDataUpdateTime(System.currentTimeMillis() - 60 * 1000);
            }
        });
    }

    @Override
    public void onUploadBitmap(int type,String originalKey, QiNiuResultBean qiNiuResultBean) {

    }

    @Override
    public void onUploadError(int type,int code, String msg) {

    }
}

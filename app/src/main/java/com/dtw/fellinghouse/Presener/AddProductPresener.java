package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.dtw.fellinghouse.Bean.LocationQQBean;
import com.dtw.fellinghouse.Bean.LocationsBean;
import com.dtw.fellinghouse.Bean.MainDataBean;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Bean.QiNiuResultBean;
import com.dtw.fellinghouse.Bean.LocationTranslatedBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.NetListener;
import com.dtw.fellinghouse.Model.NetModel;
import com.dtw.fellinghouse.Model.QiNiuListener;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.Utils.SPUtil;
import com.dtw.fellinghouse.Utils.UriUtil;
import com.dtw.fellinghouse.View.AddProduct.AddProductView;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class AddProductPresener implements QiNiuListener,NetListener {
    private Handler handler;
    private Context context;
    private AddProductView addProductView;
    private QiNiuModel qiNiuModel;
    private NetModel netModel;
    private MainDataBean mainDataBean;
    private List<String> qiniuImageNamelist = new ArrayList<>();
    private int uploadimgNum=-1;
    private ProductBean productBean;

    public AddProductPresener(Context context, AddProductView addProductView) {
        this.context = context;
        this.addProductView = addProductView;
        this.handler = new Handler();
        this.qiNiuModel = new QiNiuModel();
        qiNiuModel.setQiNiuListener(this);
        netModel=new NetModel(this);
    }

    public void insertProduct(MainDataBean mainDataBean, ProductBean productBeanWithOutImageList, List<String> uriList) {
        this.mainDataBean = mainDataBean;
        this.productBean = productBeanWithOutImageList;
        uploadimgNum=uriList.size();
        if(uploadimgNum<1){
            addProductView.showToast("至少需要添加一张图片");
        }
        for (String imgUri : uriList) {
            try {
                qiNiuModel.insertImg(QiNiuModel.TYPE_ADD,null, UriUtil.getBitmapFormUri(context, Uri.parse(imgUri)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUploadString(final int type, String originalKey, QiNiuResultBean qiNiuResultBean) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case QiNiuModel.TYPE_ADD:
                        addProductView.showToast("添加成功");
                        break;
                    case QiNiuModel.TYPE_DELETE:
                        addProductView.showToast("删除成功");
                        break;
                }
                new SPUtil(context).setDataUpdateTime(0);
                addProductView.finish();
            }
        });
        Log.v("dtw", qiNiuResultBean.toString());
    }

    @Override
    public void onUploadBitmap(int type,String originalKey, QiNiuResultBean qiNiuResultBean) {
        qiniuImageNamelist.add(qiNiuResultBean.getKey());
        if(qiniuImageNamelist.size()==uploadimgNum){
            productBean.setProductImgNameList(qiniuImageNamelist);
            mainDataBean.getProductList().add(productBean);
            qiNiuModel.overWriteJson(type,Config.Name_SimpleProductJson,new Gson().toJson(mainDataBean));
        }
    }

    @Override
    public void onUploadError(int type,int code, String msg) {
        Log.v("dtw", "upload errorCode:" + code + " msg:" + msg);
    }

    public void getLocationDescripe(LocationsBean locationsBean){
        try {
            netModel.getBean(new URL(Config.LocationTranslateQQAPI+locationsBean.getLat()+","+locationsBean.getLng()), LocationTranslatedBean.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        try {
//            netModel.getBean(new URL(Config.LocationDescripeQQAPI+locationsBean.getLat()+","+locationsBean.getLng()),LocationQQBean.class);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public <T> void onData(T data) {
        Log.v("dtw",data.getClass().getSimpleName());
        if(data instanceof LocationTranslatedBean){
            LocationTranslatedBean locationTranslatedBean = (LocationTranslatedBean) data;
            try {
                if(locationTranslatedBean.getLocations().size()>0) {
                    netModel.getBean(new URL(Config.LocationDescripeQQAPI + locationTranslatedBean.getLocations().get(0).getLat() + "," + locationTranslatedBean.getLocations().get(0).getLng()), LocationQQBean.class);
                }
                } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else if(data instanceof LocationQQBean) {
            addProductView.onLocationBean((LocationQQBean) data);
        }
    }
}

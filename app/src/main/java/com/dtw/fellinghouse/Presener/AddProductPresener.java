package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.dtw.fellinghouse.Bean.MainDataBean;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Bean.QiNiuResultBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.QiNiuListener;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.Utils.SPUtil;
import com.dtw.fellinghouse.Utils.UriUtil;
import com.dtw.fellinghouse.View.AddProduct.AddProductView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class AddProductPresener implements QiNiuListener {
    private Handler handler;
    private Context context;
    private AddProductView addProductView;
    private QiNiuModel qiNiuModel;
    private MainDataBean mainDataBean;
    private List<String> qiniuImageNamelist = new ArrayList<>();
    private int uploadimgNum=-1;
    private ProductBean productBean;

    public AddProductPresener(Context context, AddProductView addProductView) {
        this.context = context;
        this.addProductView = addProductView;
        this.handler = new Handler();
        this.qiNiuModel = qiNiuModel.getInstance();
        qiNiuModel.setQiNiuListener(this);
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
                qiNiuModel.insert(QiNiuModel.TYPE_ADD,null, UriUtil.getBitmapFormUri(context, Uri.parse(imgUri)));
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
            qiNiuModel.overWrite(type,Config.Name_SimpleProductJson,new Gson().toJson(mainDataBean));
        }
    }

    @Override
    public void onUploadError(int type,int code, String msg) {
        Log.v("dtw", "upload errorCode:" + code + " msg:" + msg);
    }
}

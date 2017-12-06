package com.dtw.fellinghouse.Presener;

import android.content.Context;
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
import com.dtw.fellinghouse.View.EditProduct.EditProductView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class EditProductPresener implements QiNiuListener {
    private Handler handler;
    private Context context;
    private EditProductView editProductView;
    private QiNiuModel qiNiuModel;
    private MainDataBean mainDataBean;
    private List<String> qiniuImageNamelist = new ArrayList<>();
    private int uploadimgNum=-1;
    private ProductBean editProductBean;

    public EditProductPresener(Context context, EditProductView editProductView) {
        this.context = context;
        this.editProductView = editProductView;
        this.handler = new Handler();
        this.qiNiuModel = new QiNiuModel();
        qiNiuModel.setQiNiuListener(this);
    }

    public void insertProduct(MainDataBean mainDataBean, ProductBean productBeanWithOutImageList, List<String> uriList) {
        this.mainDataBean = mainDataBean;
        this.editProductBean = productBeanWithOutImageList;
        uploadimgNum=uriList.size();
        if(uploadimgNum>0) {
            for (String imgUri : uriList) {
                try {
                    qiNiuModel.insertImg(QiNiuModel.TYPE_EDIT, null, UriUtil.getBitmapFormUri(context, Uri.parse(imgUri)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            mainDataBean.getProductList().add(editProductBean);
            qiNiuModel.overWriteJson(QiNiuModel.TYPE_EDIT, Config.Name_SimpleProductJson, new Gson().toJson(mainDataBean));
        }
    }

    public void deleteProduct(MainDataBean mainDataBean, ProductBean deleteProduct) {
        for(int i=0;i<mainDataBean.getProductList().size();i++){
            if(mainDataBean.getProductList().get(i).getId()==deleteProduct.getId()){
                mainDataBean.getProductList().remove(i);
                break;
            }
        }
        qiNiuModel.overWriteJson(QiNiuModel.TYPE_DELETE, Config.Name_SimpleProductJson, new Gson().toJson(mainDataBean));
    }

    @Override
    public void onUploadString(final int type, String originalKey, QiNiuResultBean qiNiuResultBean) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case QiNiuModel.TYPE_ADD:
                        editProductView.showToast("添加成功");
                        break;
                    case QiNiuModel.TYPE_DELETE:
                        editProductView.showToast("删除成功");
                        break;
                    case QiNiuModel.TYPE_EDIT:
                        editProductView.showToast("修改成功");
                        break;
                }
                new SPUtil(context).setDataUpdateTime(0);
                editProductView.finish();
            }
        });
    }

    @Override
    public void onUploadBitmap(int type,String originalKey, QiNiuResultBean qiNiuResultBean) {
        qiniuImageNamelist.add(qiNiuResultBean.getKey());
        if(qiniuImageNamelist.size()==uploadimgNum){
            editProductBean.getProductImgNameList().addAll(qiniuImageNamelist);
            mainDataBean.getProductList().add(editProductBean);
            qiNiuModel.overWriteJson(type,Config.Name_SimpleProductJson,new Gson().toJson(mainDataBean));
        }
    }

    @Override
    public void onUploadError(int type,int code, String msg) {
        Log.v("dtw", "upload errorCode:" + code + " msg:" + msg);
    }
}

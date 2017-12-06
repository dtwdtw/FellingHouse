package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.Handler;

import com.dtw.fellinghouse.Bean.MainDataBean;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Bean.QiNiuResultBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.QiNiuListener;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.Utils.SPUtil;
import com.dtw.fellinghouse.View.Tenant.TenantView;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class TenantPresener implements QiNiuListener {
    private Handler handler;
    private Context context;
    private QiNiuModel qiNiuModel;
    private TenantView tenantView;

    public TenantPresener(Context context,TenantView tenantView) {
        handler = new Handler();
        this.context=context;
        this.tenantView=tenantView;
        qiNiuModel = new QiNiuModel();
        qiNiuModel.setQiNiuListener(this);
    }

    public void insertProduct(MainDataBean mainDataBean, ProductBean productBean) {
        mainDataBean.getProductList().add(productBean);
        qiNiuModel.overWriteJson(QiNiuModel.TYPE_EDIT, Config.Name_SimpleProductJson, new Gson().toJson(mainDataBean));

    }

    @Override
    public void onUploadString(int type, String originalKey, QiNiuResultBean qiNiuResultBean) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tenantView.showToast("更新租客信息成功");
                new SPUtil(context).setDataUpdateTime(0);
                tenantView.finish();
            }
        });
    }

    @Override
    public void onUploadBitmap(int type, String originalKey, QiNiuResultBean qiNiuResultBean) {

    }

    @Override
    public void onUploadError(int type, int code, String msg) {

    }
}

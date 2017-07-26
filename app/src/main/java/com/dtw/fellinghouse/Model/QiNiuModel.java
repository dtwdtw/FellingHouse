package com.dtw.fellinghouse.Model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Bean.QiNiuResultBean;
import com.dtw.fellinghouse.Config;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class QiNiuModel {
    private static QiNiuModel qiNiuModel;
    private QiNiuListener qiNiuListener;
    private String accessKey = "HvU90834iovj59vqPjS2wqQNU8_Mg4szJS-gt8Xg";
    private String secretKey = "RDwROZoaiSaVjJzMyMZamdPWpYvlolxoVDGt9psM";
    private String bucket = "fellinghouseimage";
    private Configuration cfg = new Configuration(Zone.zone1());
    public static final int TYPE_DELETE=1001;
    public static final int TYPE_ADD=1002;
    public static final int TYPE_EDIT=1003;

    private QiNiuModel() {

    }

    public static QiNiuModel getInstance() {
        if (qiNiuModel == null) {
            qiNiuModel = new QiNiuModel();
        }
        return qiNiuModel;
    }

    public void setQiNiuListener(QiNiuListener qiNiuListener) {
        this.qiNiuListener = qiNiuListener;
    }

    public void insert(int type,String name, String value) {
        upLoad(type,name, value, getInsertToken());
    }

    public void insert(int type,String name, Bitmap bitmap) {
        upLoad(type,name, bitmap, getInsertToken());
    }

    public void overWrite(int type,String name, String value) {
        upLoad(type,name, value, getOverWriteToken(name));
    }

    public void overWrite(int type,String name, Bitmap bitmap) {
        upLoad(type,name, bitmap, getOverWriteToken(name));
    }

    private String getInsertToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        return auth.uploadToken(bucket, null, expireSeconds, putPolicy);
    }

    private String getOverWriteToken(String fileName) {
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        return auth.uploadToken(bucket, fileName, expireSeconds, putPolicy);
    }

    private void upLoad(final int type,final String key, final Bitmap bitmap, final String token) {
        final Configuration cfg = new Configuration(Zone.zone1());
        final UploadManager uploadManager = new UploadManager(cfg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String originalKey=key;
                int type_up=type;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                ByteArrayInputStream byteInputStream = new ByteArrayInputStream(baos.toByteArray());
                try {
                    Response response = uploadManager.put(byteInputStream, key, token, null, null);
                    //解析上传成功的结果
                    QiNiuResultBean qiNiuResultBean = new Gson().fromJson(response.bodyString(), QiNiuResultBean.class);
                    if (qiNiuListener != null) {
                        qiNiuListener.onUploadBitmap(type_up,originalKey,qiNiuResultBean);
                    }
                } catch (QiniuException ex) {
                    if (qiNiuListener != null) {
                        qiNiuListener.onUploadError(type_up,ex.code(), ex.error());
                    }
                }
            }
        }).start();
    }

    private void upLoad(final int type, final String name, final String value, final String token) {
        final UploadManager uploadManager = new UploadManager(cfg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String originalKey=name;
                int type_up=type;
                try {
                    Response response = uploadManager.put(value.getBytes(), name, token);
                    //解析上传成功的结果
                    QiNiuResultBean qiNiuResultBean = new Gson().fromJson(response.bodyString(), QiNiuResultBean.class);
                    if (qiNiuListener != null) {
                        qiNiuListener.onUploadString(type_up,originalKey,qiNiuResultBean);
                    }
                } catch (QiniuException ex) {
                    ex.printStackTrace();
                    if (qiNiuListener != null) {
                        qiNiuListener.onUploadError(type_up,ex.code(), ex.error());
                    }
                }
            }
        }).start();
    }

    public String getPrivateUrl(String name) {
        String domainOfBucket = "http://ot9t0q9wl.bkt.clouddn.com";
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String tempUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        return tempUrl;
    }

    public String getPublicUrl(String name) {
        String domainOfBucket = "http://ot9t0q9wl.bkt.clouddn.com";
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        return finalUrl;
    }

    public void deleteFile(final String name, @NonNull final BaseCallBack baseCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Auth auth = Auth.create(accessKey, secretKey);
                BucketManager bucketManager = new BucketManager(auth, cfg);
                try {
                    bucketManager.delete(bucket, name);
                } catch (QiniuException ex) {
                    //如果遇到异常，说明删除失败
                    ex.printStackTrace();
                    baseCallBack.onResult(ex.code(), ex.error());
                }
                baseCallBack.onResult(Config.Code_Success, "删除成功");
            }
        }).start();
    }

    //功能待完善
    private void getDataTraffic() {
        Auth auth = Auth.create(accessKey, secretKey);
        CdnManager c = new CdnManager(auth);
        String[] domains = new String[]{
                bucket
        };
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = simpleDateFormat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
//开始和结束日期
        String fromDate = simpleDateFormat.format(calendar.getTime());
        String toDate = currentDateandTime;
//数据粒度，支持的取值为 5min ／ hour ／day
        String granularity = "day";
        try {
            CdnResult.FluxResult fluxResult = c.getFluxData(domains, fromDate, toDate, granularity);
            //处理得到的结果数据
            //...
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    interface BaseCallBack {
        void onResult(int code, String msg);
    }
}

package com.dtw.fellinghouse.Model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.dtw.fellinghouse.Bean.QiNiuResultBean;
import com.dtw.fellinghouse.Config;
import com.google.gson.Gson;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

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
    private QiNiuListener qiNiuListener;
    private String accessKey = "HvU90834iovj59vqPjS2wqQNU8_Mg4szJS-gt8Xg";
    private String secretKey = "RDwROZoaiSaVjJzMyMZamdPWpYvlolxoVDGt9psM";
    private String DomainImg = "http://ot9t0q9wl.bkt.clouddn.com";
    private String DomainJson = "http://ou531g0fp.bkt.clouddn.com";
    private String DomainUser = "http://ou53zqmns.bkt.clouddn.com";
    private String Bucket_Img ="fellinghouseimage";
    private String Bucket_User="user";
    private String Bucket_Json="fellinghousejson";

    private Configuration cfg = new Configuration(Zone.zone1());
    public static final int TYPE_DELETE = 1001;
    public static final int TYPE_ADD = 1002;
    public static final int TYPE_EDIT = 1003;
    public static final int TYPE_SETADMIN=1004;

    public QiNiuModel() {

    }

    public void setQiNiuListener(QiNiuListener qiNiuListener) {
        this.qiNiuListener = qiNiuListener;
    }

    public void insertJson(int type, String name, String value) {
        upLoad(type, name, value, getInsertToken(Bucket_Json));
    }

    public void insertImg(int type, String name, Bitmap bitmap) {
        upLoad(type, name, bitmap, getInsertToken(Bucket_Img));
    }

    public void overWriteJson(int type, String name, String value) {
        upLoad(type, name, value, getOverWriteToken(Bucket_Json, name));
    }

    public void overWriteImg(int type, String name, Bitmap bitmap) {
        upLoad(type, name, bitmap, getOverWriteToken(Bucket_Img, name));
    }

    public void insertUser(int type,String name,String value){
        upLoad(type,name,value,getInsertToken(Bucket_User));
    }

    public void overWriteUser(int type,String name,String value){
        upLoad(type,name,value,getOverWriteToken(Bucket_User,name));
    }

    private String getInsertToken(String bucketName) {
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"imgBucket\":\"$(imgBucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        return auth.uploadToken(bucketName, null, expireSeconds, putPolicy);
    }

    private String getOverWriteToken(String bucketName, String fileName) {
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"imgBucket\":\"$(imgBucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        return auth.uploadToken(bucketName, fileName, expireSeconds, putPolicy);
    }

    private void upLoad(final int type, final String key, final Bitmap bitmap, final String token) {
        final Configuration cfg = new Configuration(Zone.zone1());
        final UploadManager uploadManager = new UploadManager(cfg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String originalKey = key;
                int type_up = type;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                ByteArrayInputStream byteInputStream = new ByteArrayInputStream(baos.toByteArray());
                try {
                    Response response = uploadManager.put(byteInputStream, key, token, null, null);
                    //解析上传成功的结果
                    QiNiuResultBean qiNiuResultBean = new Gson().fromJson(response.bodyString(), QiNiuResultBean.class);
                    if (qiNiuListener != null) {
                        qiNiuListener.onUploadBitmap(type_up, originalKey, qiNiuResultBean);
                    }
                } catch (QiniuException ex) {
                    if (qiNiuListener != null) {
                        qiNiuListener.onUploadError(type_up, ex.code(), ex.error());
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
                String originalKey = name;
                int type_up = type;
                try {
                    Response response = uploadManager.put(value.getBytes(), name, token);
                    //解析上传成功的结果
                    QiNiuResultBean qiNiuResultBean = new Gson().fromJson(response.bodyString(), QiNiuResultBean.class);
                    if (qiNiuListener != null) {
                        qiNiuListener.onUploadString(type_up, originalKey, qiNiuResultBean);
                    }
                } catch (QiniuException ex) {
                    ex.printStackTrace();
                    if (qiNiuListener != null) {
                        qiNiuListener.onUploadError(type_up, ex.code(), ex.error());
                    }
                }
            }
        }).start();
    }

//    public String getPrivateImgUrl(String keyName) {
//        return getPrivateUrl(DomainImg, keyName);
//    }

    public String getPrivateJsonUrl(String keyName) {
        return getPrivateUrl(DomainJson, keyName);
    }

    public String getPrivateUserUrl(String keyName) {
        return getPrivateUrl(DomainUser, keyName);
    }

    public String getPublicImgUrl(String keyName) {
        return getPublicUrl(DomainImg, keyName);
    }

//    public String getPublicJsonUrl(String keyName) {
//        return getPublicUrl(DomainJson, keyName);
//    }

//    public String getPublicUserUrl(String keyName) {
//        return getPublicUrl(DomainUser, keyName);
//    }

    private String getPrivateUrl(String domain, String keyName) {
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(keyName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String publicUrl = String.format("%s/%s", domain, encodedFileName);
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String tempUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        return tempUrl;
    }

    private String getPublicUrl(String domain, String keyName) {
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(keyName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String finalUrl = String.format("%s/%s", domain, encodedFileName);
        return finalUrl;
    }

    public void deleteFile(final String keyName, @NonNull final BaseCallBack baseCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Auth auth = Auth.create(accessKey, secretKey);
                BucketManager bucketManager = new BucketManager(auth, cfg);
                try {
                    bucketManager.delete(Bucket_Img, keyName);
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
                Bucket_Img
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

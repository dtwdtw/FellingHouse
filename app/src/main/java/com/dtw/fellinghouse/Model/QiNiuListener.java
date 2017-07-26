package com.dtw.fellinghouse.Model;

import com.dtw.fellinghouse.Bean.QiNiuResultBean;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public interface QiNiuListener {
    void onUploadString(int type,String originalKey,QiNiuResultBean qiNiuResultBean);
    void onUploadBitmap(int type,String originalKey,QiNiuResultBean qiNiuResultBean);
    void onUploadError(int type,int code,String msg);
}

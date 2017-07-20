package com.dtw.fellinghouse.Model;

import com.dtw.fellinghouse.Bean.QiNiuResultBean;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public interface QiNiuListener {
    void onUpload(QiNiuResultBean qiNiuResultBean);
    void onUploadError(int code,String msg);
}

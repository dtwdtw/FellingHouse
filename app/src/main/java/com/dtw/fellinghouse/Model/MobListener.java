package com.dtw.fellinghouse.Model;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public interface MobListener {
    void onSuccess(int code,String msg);
    void onFail(int code,String msg);
}

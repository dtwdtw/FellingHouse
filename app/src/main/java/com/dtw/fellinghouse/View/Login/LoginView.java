package com.dtw.fellinghouse.View.Login;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public interface LoginView {
    void onFail(int code,String msg);
    void onSuccess(int code,String msg);
    void showToast(String msg);
}

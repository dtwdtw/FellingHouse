package com.dtw.fellinghouse.View.SmsCodeLogin;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public interface SmsCodeLoginView {
    void showToast(String msg);
    void goBackWidthData();
    void setCodeBtnText(String text);
    void setEnableCodeBtn(boolean enable);
    void setEnableRegistBtn(boolean enable);
}

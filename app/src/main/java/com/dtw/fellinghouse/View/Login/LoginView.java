package com.dtw.fellinghouse.View.Login;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public interface LoginView {
    void showToast(String msg);
    void goBack(UserInfo userInfo);
}

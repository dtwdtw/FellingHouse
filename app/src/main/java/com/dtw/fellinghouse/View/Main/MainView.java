package com.dtw.fellinghouse.View.Main;

import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/17 0017.
 */

public interface MainView {
    <T>void onData(T data);
    void endRefreshing();
    void showToast(String msg);
    void onActivityResult(int Request_Code, int Result_Code, Intent intent);
    void isAdmin(boolean isAdmin);
}

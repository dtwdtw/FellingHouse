package com.dtw.fellinghouse.Model;

import com.dtw.fellinghouse.Config;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public interface UserListener {
    void addUser(int code,String msg);
    void setAdmin(int code,String msg);
}

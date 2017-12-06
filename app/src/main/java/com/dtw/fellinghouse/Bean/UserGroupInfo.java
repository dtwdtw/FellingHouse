package com.dtw.fellinghouse.Bean;

import com.google.gson.jpush.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

public class UserGroupInfo {

    /**
     * lastID : 0
     * User : [{"name":"hello","id":0,"password":"a1234","phoneNum":"12341234123","gender":"male","createTime":"2012/12/25 21:23:25","lastLoginTime":"2017/12/25 21:23:25"}]
     */

    private int lastID;
    private List<UserBean> User;

    public int getLastID() {
        return lastID;
    }

    public void setLastID(int lastID) {
        this.lastID = lastID;
    }

    public List<UserBean> getUser() {
        return User;
    }

    public void setUser(List<UserBean> User) {
        this.User = User;
    }

    public String toString(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }
}

package com.dtw.fellinghouse.Bean;

import com.google.gson.jpush.Gson;

public class UserBean {
    /**
     * name : hello
     * id : 0
     * password : a1234
     * phoneNum : 12341234123
     * gender : male
     * createTime : 2012/12/25 21:23:25
     * lastLoginTime : 2017/12/25 21:23:25
     */

    private String name;
    private int id;
    private String password;
    private String phoneNum;
    private String gender;
    private String createTime;
    private String lastLoginTime;
    private boolean isAdmin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String toString(){
        return new Gson().toJson(this);
    }
}
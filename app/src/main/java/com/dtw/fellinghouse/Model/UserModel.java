package com.dtw.fellinghouse.Model;

import android.util.Log;

import com.dtw.fellinghouse.Bean.QiNiuResultBean;
import com.dtw.fellinghouse.Bean.UserBean;
import com.dtw.fellinghouse.Bean.UserGroupInfo;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Utils.PhoneNumUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class UserModel implements NetListener, QiNiuListener {
    private UserListener userListener;
    private QiNiuModel qiNiuModel;
    private NetModel netModel;

    public UserModel() {
        netModel = new NetModel(this);
        qiNiuModel = new QiNiuModel();
        qiNiuModel.setQiNiuListener(this);
    }

    public void getAdminUserInfo(String phoneNum) {
        try {
            netModel.postBean(new URL(qiNiuModel.getPrivateUserUrl(PhoneNumUtil.getQiNiuFileName(phoneNum))), UserInfo.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }

    @Override
    public <T> void onData(T data) {
    }

    public void setUserAdmin(final String phoneNum, final boolean isAdmin) {
        try {
            netModel.postBean(new URL(qiNiuModel.getPrivateUserUrl(PhoneNumUtil.getQiNiuFileName(phoneNum))), UserGroupInfo.class, new NetModel.OnComplateDataListener() {
                @Override
                public <T> void onData(int code, T data) {
                    if (code == 200) {
                        if (data instanceof UserGroupInfo) {
                            UserGroupInfo userGroupInfo = (UserGroupInfo) data;
                            List<UserBean> userBeanList = userGroupInfo.getUser();
                            for (UserBean userBean : userBeanList) {
                                if (phoneNum.equals(userBean.getPhoneNum())) {
                                    if(userBean.isAdmin()){
                                        userListener.setAdmin(Config.Code_Success,"该用户已经是管理员");
                                    }else {
                                        userBean.setAdmin(isAdmin);
                                        Log.v("dtw", userGroupInfo.toString());
                                        qiNiuModel.overWriteUser(QiNiuModel.TYPE_SETADMIN, PhoneNumUtil.getQiNiuFileName(phoneNum), userGroupInfo.toString());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(final UserBean userBean) {
        try {
            netModel.postBean(new URL(qiNiuModel.getPrivateUserUrl(PhoneNumUtil.getQiNiuFileName(userBean.getPhoneNum()))), UserGroupInfo.class, new NetModel.OnComplateDataListener() {
                @Override
                public <T> void onData(int responceCode, T data) {
                    if (responceCode == 200) {
                        if (data instanceof UserGroupInfo) {
                            UserGroupInfo userGroupInfo = (UserGroupInfo) data;
                            int lastID = userGroupInfo.getLastID();
                            userBean.setId(lastID);
                            lastID++;
                            userGroupInfo.setLastID(lastID);
                            userGroupInfo.getUser().add(userBean);
                            Log.v("dtw", userGroupInfo.toString());
                            Log.v("dtw", "filename:" + PhoneNumUtil.getQiNiuFileName(userBean.getPhoneNum()));
                            qiNiuModel.overWriteUser(QiNiuModel.TYPE_ADD, PhoneNumUtil.getQiNiuFileName(userBean.getPhoneNum()), userGroupInfo.toString());
                        } else {
                            Log.v("dtw", "T error");
                        }
                    } else {
                        if (responceCode == 404) {
                            List<UserBean> userBeanList = new ArrayList<UserBean>();
                            UserGroupInfo userGroupInfo = new UserGroupInfo();
                            userGroupInfo.setLastID(1);
                            userBean.setId(0);
                            userGroupInfo.setUser(userBeanList);
                            userGroupInfo.getUser().add(userBean);
                            qiNiuModel.insertUser(QiNiuModel.TYPE_ADD, PhoneNumUtil.getQiNiuFileName(userBean.getPhoneNum()), userGroupInfo.toString());

                        }
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUploadString(int type, String originalKey, QiNiuResultBean qiNiuResultBean) {
        if (userListener != null) {
            switch (type) {
                case QiNiuModel.TYPE_ADD:
                    userListener.addUser(Config.Code_Success, "添加用户成功");
                    break;
                case QiNiuModel.TYPE_SETADMIN:
                    userListener.setAdmin(Config.Code_Success, "修改用户管理员成功");
                    break;
            }
        }
    }

    @Override
    public void onUploadBitmap(int type, String originalKey, QiNiuResultBean qiNiuResultBean) {

    }

    @Override
    public void onUploadError(int type, int code, String msg) {

    }
}

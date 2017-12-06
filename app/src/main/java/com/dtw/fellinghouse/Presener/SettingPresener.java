package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.Handler;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.JMessageModel;
import com.dtw.fellinghouse.Model.UserListener;
import com.dtw.fellinghouse.Model.UserModel;
import com.dtw.fellinghouse.View.Setting.SettingView;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class SettingPresener implements UserListener{
    private Context context;
    private SettingView settingView;
    private JMessageModel jMessageModel;
    private UserModel userModel;
    private Handler handler;
    public SettingPresener(Context context, SettingView settingView){
        handler=new Handler();
        this.context=context;
        this.settingView=settingView;
        jMessageModel=JMessageModel.getInstance(context);
        userModel=new UserModel();
        userModel.setUserListener(this);
    }

    public int getNotifyFlag(){
        return jMessageModel.getNotifyFlag();
    }

    public void setNotifyFlag(int flag){
        jMessageModel.setNotifyFlag(flag);
    }

    public void setUserAdmin(String userName){
        userModel.setUserAdmin(userName,true);
    }

    @Override
    public void addUser(int code, String msg) {

    }

    @Override
    public void setAdmin(final int code, final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(code== Config.Code_Success){
                    settingView.showToast(msg);
                }
            }
        });
    }
}

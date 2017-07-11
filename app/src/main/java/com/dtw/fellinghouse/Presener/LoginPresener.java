package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.Handler;

import com.dtw.fellinghouse.Bean.UserInfoBean;
import com.dtw.fellinghouse.Model.JMessageModel;
import com.dtw.fellinghouse.View.Login.LoginView;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class LoginPresener{
    private Handler handler=new Handler();
    private Context context;
    private LoginView loginView;
    private JMessageModel jMessageModel;
    public LoginPresener(Context context,LoginView loginView){
        this.context=context;
        this.loginView=loginView;
        jMessageModel=JMessageModel.getInstance(context);
    }
    public void login(String phoneNum,String password){
        jMessageModel.login(phoneNum, password, new JMessageModel.BaseCallBack() {
            @Override
            public void onResult(int code, String msg) {
                switch (code){
                    case 0:
                        loginView.goBack(new UserInfoBean());
                        break;
                }
            }
        });
    }

    public void onViewDestory(){
    }
}

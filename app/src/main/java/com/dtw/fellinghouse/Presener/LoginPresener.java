package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.dtw.fellinghouse.Model.JMessageModel;
import com.dtw.fellinghouse.Utils.SPUtil;
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
    public void login(final String phoneNum, final String password){
        jMessageModel.login(phoneNum, password, new JMessageModel.BaseCallBack() {
            @Override
            public void onResult(int code, String msg) {
                switch (code){
                    case 0:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loginView.goBack(jMessageModel.getMyInfo());
                            }
                        });
                        break;
                }
            }
        });
    }

    public void onViewDestory(){
    }
}

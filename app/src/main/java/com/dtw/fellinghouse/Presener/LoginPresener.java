package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.Handler;
import android.util.LogPrinter;

import com.dtw.fellinghouse.Model.MobListener;
import com.dtw.fellinghouse.Model.MobModel;
import com.dtw.fellinghouse.View.Login.LoginView;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class LoginPresener implements MobListener{
    public final static int Code_CodeSended=0;
    public final static int Code_CodeVerified=1;
    private Handler handler=new Handler();
    private Context context;
    private LoginView loginView;
    public LoginPresener(Context context,LoginView loginView){
        this.context=context;
        this.loginView=loginView;
    }
    public void login(String phoneNum,String password){
        MobModel.getInstance(context).sendMsgCode(phoneNum);
    }

    @Override
    public void onCodeSendSuccess() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loginView.onSuccess(Code_CodeSended,"短信验证码已发送");
            }
        });
    }

    @Override
    public void onCodeVerified() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loginView.onSuccess(Code_CodeVerified,"短信验证成功");
            }
        });
    }
}

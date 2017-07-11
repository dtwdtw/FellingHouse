package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.MobListener;
import com.dtw.fellinghouse.Model.MobModel;
import com.dtw.fellinghouse.View.Regist.RegistView;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class RegistPresener implements MobListener{
    private Handler handler=new Handler();
    private Context context;
    private RegistView registView;
    private MobModel mobModel;
    private CountDownTimer countDownTimer;
    public RegistPresener(Context context,RegistView registView){
        this.context=context;
        this.registView=registView;
        mobModel=MobModel.getInstance(context);
        mobModel.setMobListener(this);
    }
    public void getCode(String phoneNum){
        registView.setEnableCodeBtn(false);
        mobModel.sendMsgCode(phoneNum);
    }
    public void getVoiceCode(String phoneNum){
        registView.setEnableCodeBtn(false);
        mobModel.sendVoiceCode(phoneNum);
    }
    public void verifyCode(String phoneNum,String code){
        registView.setEnableRegistBtn(false);
        mobModel.verifyMsgCode(phoneNum,code);
    }
    public void onViewDetory(){
        mobModel.destoryMob();
    }

    @Override
    public void onSuccess(int code, final String msg) {
        switch (code){
            case Config.Code_CodeSend:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        registView.showToast(msg);
                        if(countDownTimer==null) {
                            countDownTimer = new CountDownTimer(1000 * 60, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    registView.setCodeBtnText(millisUntilFinished/1000+"s");
                                }

                                @Override
                                public void onFinish() {
                                    registView.setEnableCodeBtn(true);
                                    registView.setCodeBtnText("获取验证码");
                                }
                            };
                            countDownTimer.start();
                        }else{
                            countDownTimer.start();
                        }
                    }
                });
                break;
            case Config.Code_CodeVerify:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        registView.showToast(msg);
                        registView.setEnableRegistBtn(true);
                        registView.goBackWidthData();
                    }
                });
                break;
        }
    }

    @Override
    public void onFail(int code, final String msg) {
        switch (code){
            case Config.Code_CodeSend:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        registView.showToast(msg);
                        registView.setEnableCodeBtn(true);
                    }
                });
                break;
            case Config.Code_CodeVerify:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        registView.showToast(msg);
                        registView.setEnableRegistBtn(true);
                    }
                });
                break;
            default:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        registView.showToast(msg);
                        registView.setEnableCodeBtn(true);
                        registView.setEnableRegistBtn(true);
                    }
                });
                break;
        }
    }
}

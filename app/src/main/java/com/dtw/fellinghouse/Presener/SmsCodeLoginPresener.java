package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.MobListener;
import com.dtw.fellinghouse.Model.MobModel;
import com.dtw.fellinghouse.View.SmsCodeLogin.SmsCodeLoginView;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class SmsCodeLoginPresener implements MobListener{
    private Handler handler=new Handler();
    private Context context;
    private SmsCodeLoginView smsCodeLoginView;
    private MobModel mobModel;
    private CountDownTimer countDownTimer;
    public SmsCodeLoginPresener(Context context,SmsCodeLoginView smsCodeLoginView){
        this.context=context;
        this.smsCodeLoginView=smsCodeLoginView;
        mobModel= MobModel.getInstance(context);
        mobModel.setMobListener(this);
    }
    public void getCode(String phoneNum){
        smsCodeLoginView.setEnableCodeBtn(false);
        mobModel.sendMsgCode(phoneNum);
    }
    public void getVoiceCode(String phoneNum){
        smsCodeLoginView.setEnableCodeBtn(false);
        mobModel.sendVoiceCode(phoneNum);
    }
    public void verifyCode(String phoneNum,String code){
        smsCodeLoginView.setEnableRegistBtn(false);
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
                        smsCodeLoginView.showToast(msg);
                        if(countDownTimer==null) {
                            countDownTimer = new CountDownTimer(1000 * 60, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    smsCodeLoginView.setCodeBtnText(millisUntilFinished/1000+"s");
                                }

                                @Override
                                public void onFinish() {
                                    smsCodeLoginView.setEnableCodeBtn(true);
                                    smsCodeLoginView.setCodeBtnText("获取验证码");
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
                        smsCodeLoginView.showToast(msg);
                        smsCodeLoginView.setEnableRegistBtn(true);
                        smsCodeLoginView.goBackWidthData();
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
                        smsCodeLoginView.showToast(msg);
                        smsCodeLoginView.setEnableCodeBtn(true);
                    }
                });
                break;
            case Config.Code_CodeVerify:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        smsCodeLoginView.showToast(msg);
                        smsCodeLoginView.setEnableRegistBtn(true);
                    }
                });
                break;
            case -1:
            default:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        smsCodeLoginView.showToast(msg);
                        smsCodeLoginView.setEnableCodeBtn(true);
                        smsCodeLoginView.setEnableRegistBtn(true);
                    }
                });
                break;
        }
    }
}

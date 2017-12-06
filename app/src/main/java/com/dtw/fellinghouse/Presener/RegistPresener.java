package com.dtw.fellinghouse.Presener;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import com.dtw.fellinghouse.Bean.UserBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.JMessageListener;
import com.dtw.fellinghouse.Model.JMessageModel;
import com.dtw.fellinghouse.Model.MobListener;
import com.dtw.fellinghouse.Model.MobModel;
import com.dtw.fellinghouse.Model.UserModel;
import com.dtw.fellinghouse.Utils.PhoneNumUtil;
import com.dtw.fellinghouse.View.Regist.RegistView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class RegistPresener implements MobListener {
    private Handler handler = new Handler();
    private Context context;
    private RegistView registView;
    private MobModel mobModel;
    private JMessageModel jMessageModel;
    private UserModel userModel;
    private CountDownTimer countDownTimer;
    private String phoneNum, password;

    public RegistPresener(Context context, RegistView registView) {
        this.context = context;
        this.registView = registView;
        mobModel = MobModel.getInstance(context);
        jMessageModel = JMessageModel.getInstance(context);
        mobModel.setMobListener(this);
        userModel=new UserModel();
    }

    public void getCode(String phoneNum) {
        registView.setEnableCodeBtn(false);
        mobModel.sendMsgCode(phoneNum);
    }

    public void getVoiceCode(String phoneNum) {
        registView.setEnableCodeBtn(false);
        mobModel.sendVoiceCode(phoneNum);
    }

    public void verifyCode(String phoneNum, String password, String code) {
        this.phoneNum = phoneNum;
        this.password = password;
        registView.setEnableRegistBtn(false);
        mobModel.verifyMsgCode(phoneNum, code);
    }

    public void onViewDetory() {
        mobModel.destoryMob();
    }

    @Override
    public void onSuccess(int code, final String msg) {
        switch (code) {
            case Config.Code_CodeSend:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        registView.showToast(msg);
                        if (countDownTimer == null) {
                            countDownTimer = new CountDownTimer(1000 * 60, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    registView.setCodeBtnText(millisUntilFinished / 1000 + "s");
                                }

                                @Override
                                public void onFinish() {
                                    registView.setEnableCodeBtn(true);
                                    registView.setCodeBtnText("获取验证码");
                                }
                            };
                            countDownTimer.start();
                        } else {
                            countDownTimer.start();
                        }
                    }
                });
                break;
            case Config.Code_CodeVerify:
                Log.v("dtw", "验证码验证成功");
                jMessageModel.regist(phoneNum, password, new JMessageModel.BaseCallBack() {
                    @Override
                    public void onResult(final int code, final String msg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                switch (code) {
                                    case 0:
                                        registView.showToast(msg);
                                        registView.setEnableRegistBtn(true);
                                        UserBean userBean=new UserBean();
                                        userBean.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                                        userBean.setName(phoneNum);
                                        userBean.setPhoneNum(phoneNum);
                                        userBean.setPassword(password);
                                        Log.v("dtw",userBean.toString());
                                        Log.v("dtw", PhoneNumUtil.getQiNiuFileName(userBean.getPhoneNum()));
                                        userModel.addUser(userBean);
                                        registView.goBackWidthData();
                                        break;
                                    case 898001:
                                        registView.showToast("该手机号已注册");
                                        break;
                                    default:
                                        Log.v("dtw", "code:" + code + "  msg:" + msg);
                                        break;
                                }
                            }
                        });
                    }
                });
                break;
        }
    }

    @Override
    public void onFail(int code, final String msg) {
        switch (code) {
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

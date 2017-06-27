package com.dtw.fellinghouse.Model;

import android.content.Context;

import com.dtw.fellinghouse.Config;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class MobModel extends EventHandler {
    private Context context;
    private MobListener mobListener;
    private static MobModel mobModel;
    private MobModel(Context context){
        this.context=context;
        MobSDK.init(context);
    }
    public static MobModel getInstance(Context context){
        if(mobModel==null){
            mobModel=new MobModel(context);
        }
        return mobModel;
    }
    public void setMobListener(MobListener mobListener){
        this.mobListener=mobListener;
    }

    @Override
    public void afterEvent(int event, int result, Object data) {
        super.afterEvent(event, event, event);
        if (result == SMSSDK.RESULT_COMPLETE) {
            //回调完成
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //提交验证码成功
                mobListener.onCodeSendSuccess();
            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                //获取验证码成功
                mobListener.onCodeVerified();
            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                //返回支持发送验证码的国家列表
            }
        }else{
            ((Throwable)data).printStackTrace();
        }
    }
    public void destoryMob(){
        SMSSDK.unregisterEventHandler(this);
    }
    public void sendMsgCode(String phoneNum){
        SMSSDK.getVerificationCode(Config.MobChina,phoneNum);
    }
    public void verifyMsgCode(String phoneNum,String msgCode){
        SMSSDK.submitVerificationCode(Config.MobChina, phoneNum, msgCode);
    }
}
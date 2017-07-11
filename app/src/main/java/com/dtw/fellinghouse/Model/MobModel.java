package com.dtw.fellinghouse.Model;

import android.content.Context;
import android.util.Log;

import com.dtw.fellinghouse.Config;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

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
        SMSSDK.registerEventHandler(this);
    }

    @Override
    public void afterEvent(int event, int result, Object data) {
        super.afterEvent(event, event, event);
        if (result == SMSSDK.RESULT_COMPLETE) {
            //回调完成
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //提交验证码成功
                mobListener.onSuccess(Config.Code_CodeVerify,"验证成功");
            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                //获取验证码成功
                mobListener.onSuccess(Config.Code_CodeSend,"短信验证码已发送");
            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                //返回支持发送验证码的国家列表
            }
        }else{
            Throwable throwable=(Throwable)data;
            throwable.printStackTrace();
            try {
                JSONObject jsonObject=new JSONObject(throwable.getMessage());
                switch (jsonObject.getInt("status")){
                    case 603:
                        mobListener.onFail(Config.Code_CodeSend,"不合法的手机号码");
                        break;
                    case 477:
                        mobListener.onFail(Config.Code_CodeSend,"该手机号已达当日最大发送量");
                        break;
                    case 462:
                        mobListener.onFail(Config.Code_CodeSend,"请一分钟后再试");
                        break;
                    case 476:
                        mobListener.onFail(Config.Code_CodeSend,"超出开发商验证次数");
                        break;
                    case 468:
                        mobListener.onFail(Config.Code_CodeVerify,"验证码错误");
                        break;
                    case 466:
                        mobListener.onFail(Config.Code_CodeVerify,"验证码为空");
                        break;
                    case 467:
                        mobListener.onFail(Config.Code_CodeVerify,"验证码超时");
                        break;
                    default:
                        mobListener.onFail(-1,throwable.getMessage());
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mobListener.onFail(-1,throwable.getMessage());
            }
        }
    }
    public void destoryMob(){
        SMSSDK.unregisterEventHandler(this);
    }
    public void sendMsgCode(String phoneNum){
        SMSSDK.getVerificationCode(Config.MobChina,phoneNum);
    }
    public void sendVoiceCode(String phoneNum){
        SMSSDK.getVoiceVerifyCode(Config.MobChina,phoneNum);
    }
    public void verifyMsgCode(String phoneNum,String msgCode){
        SMSSDK.submitVerificationCode(Config.MobChina, phoneNum, msgCode);
    }
}
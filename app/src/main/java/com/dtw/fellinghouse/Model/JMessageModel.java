package com.dtw.fellinghouse.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.View.Chart.ChartActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class JMessageModel {
    private static JMessageModel jMessageModel;
    private JMessageListener jMessageListener;
    private Context context;
    private boolean chartActivityON=false;

    private JMessageModel(Context context) {
        this.context = context;
        JMessageClient.registerEventReceiver(this);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(context);
    }

    public static JMessageModel getInstance(Context context) {
        if (jMessageModel == null) {
            jMessageModel = new JMessageModel(context);
        }
        return jMessageModel;
    }

    public void setjMessageListener(JMessageListener jMessageListener) {
        this.jMessageListener = jMessageListener;
    }

    public void regist(String phoneNum, String password, final BaseCallBack baseCallBack){
        JMessageClient.register(phoneNum, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                baseCallBack.onResult(i,s);
            }
        });
    }

    public void login(String phoneNum, String password, final BaseCallBack baseCallBack){
        JMessageClient.login(phoneNum, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                baseCallBack.onResult(i,s);
                Log.v("dtw","code:"+i+"  msg:"+s);
            }
        });
    }

    public void setChartActivityON(boolean on){
        chartActivityON=on;
    }

    public void logout(){
        JMessageClient.logout();
    }

    public void enterConversation(String name){
        JMessageClient.enterSingleConversation(name);
    }

    public void exitConversation(){
        JMessageClient.exitConversation();
    }

    public Message sendTextMesage(String targetName, String textMessage) {
        Conversation conversation = Conversation.createSingleConversation(targetName);
        Message message = conversation.createSendTextMessage(textMessage);
        JMessageClient.sendMessage(message);
        return message;
    }

    public Message sendImageMessage(String targetName, File imageMessage) {
        Conversation conversation = Conversation.createSingleConversation(targetName);
        Message message = null;
        try {
            message = conversation.createSendImageMessage(imageMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JMessageClient.sendMessage(message);
        Log.v("dtw",message.toString());
        return message;
    }

    public Message sendMultiTextMessage(List<String> targetList,String textMessage){
        for(String targetName:targetList){
            sendTextMesage(targetName,textMessage);
        }
        return JMessageClient.createSingleTextMessage(Config.Key_Admin,textMessage);
    }

    public Message sendMultiImageMessage(List<String> targetList,File imageMessage){
        for(String targetName:targetList){
            sendImageMessage(targetName,imageMessage);
        }
        Message message=null;
        try {
            message=JMessageClient.createSingleImageMessage(Config.Key_Admin,imageMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void getSingleConversation(String targetName) {
        Conversation conversation = Conversation.createSingleConversation(targetName);
        jMessageListener.onMessage(conversation.getAllMessage());
    }

    public void getLocalConversation(){
        jMessageListener.onLocalConversation(JMessageClient.getConversationList());
    }

    public void deleteConversation(String targetName){
        JMessageClient.deleteSingleConversation(targetName);
    }

    public UserInfo getMyInfo(){
        return JMessageClient.getMyInfo();
    }

    public void getUserInfo(String name){
        JMessageClient.getUserInfo(name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {

            }
        });
    }

    public void setNotifyFlag(int flag){
        switch(flag){
            case Config.NotifyDefault:
                JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DEFAULT);
                break;
            case Config.NotifyDisEnable:
                JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_DISABLE);
                break;
            case Config.NotifySilence:
                JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_SILENCE);
                break;
        }
    }

    public int getNotifyFlag(){
        return JMessageClient.getNotificationFlag();
    }

    public void onEventMainThread(MessageEvent event) {
        if (jMessageListener != null) {
            jMessageListener.onMessage(event.getMessage());
        }
    }

    public void onEventMainThread(NotificationClickEvent event) {
        if(!chartActivityON) {
            Intent intent = new Intent(context, ChartActivity.class);
            intent.putExtra(Config.Key_Is_Admin,getMyInfo().getSignature().equals(Config.Key_Admin));
            context.startActivity(intent);//自定义跳转到指定页面
        }else{
            jMessageListener.changeConversation(event.getMessage());
        }
    }

    public interface BaseCallBack{
        void onResult(int code, String msg);
    }

}

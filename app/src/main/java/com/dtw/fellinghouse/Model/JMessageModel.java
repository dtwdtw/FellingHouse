package com.dtw.fellinghouse.Model;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.dtw.fellinghouse.View.Chart.ChartActivity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class JMessageModel {
    private static JMessageModel jMessageModel;
    private JMessageListener jMessageListener;
    private List<Message> messageList = new ArrayList<>();
    private Context context;

    private JMessageModel(Context context) {
        this.context = context;
        JMessageClient.registerEventReceiver(this);
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

    public void sendTextMesage(String targetName, String textMessage) {
        Conversation conversation = Conversation.createSingleConversation(targetName);
        Message message = conversation.createSendTextMessage(textMessage);
        JMessageClient.sendMessage(message);
    }

    public void getSingleConversation(String targetName) {
        Conversation conversation = JMessageClient.getSingleConversation(targetName);
        jMessageListener.onMessage(conversation.getAllMessage());
    }

    public void onEventMainThread(MessageEvent event) {
        if (jMessageListener != null) {
            jMessageListener.onMessage(event.getMessage());
        }
    }

    public void onEventMainThread(NotificationClickEvent event) {
        Intent intent = new Intent(context, ChartActivity.class);
        context.startActivity(intent);//自定义跳转到指定页面
    }

}

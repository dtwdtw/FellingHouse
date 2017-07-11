package com.dtw.fellinghouse.Presener;

import android.content.Context;

import com.dtw.fellinghouse.Model.JMessageListener;
import com.dtw.fellinghouse.Model.JMessageModel;
import com.dtw.fellinghouse.View.Chart.ChartView;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class ChartPresener implements JMessageListener {
    private Context context;
    private ChartView chartView;
    private JMessageModel jMessageModel;

    public ChartPresener(Context context,ChartView chartView) {
        this.context=context;
        this.chartView = chartView;
        jMessageModel=JMessageModel.getInstance(context);
        jMessageModel.setjMessageListener(this);
    }

    public void onViewResume(String name){
        jMessageModel.enterConversation(name);
    }

    public void onViewPause(){
        jMessageModel.exitConversation();
    }

    @Override
    public void onMessage(Message message) {
        chartView.onMessage(message);
    }

    @Override
    public void onMessage(List<Message> messageList) {
        chartView.onMessage(messageList);
    }

    public void getLocalMessages(String targetName) {
        jMessageModel.getSingleConversation(targetName);
    }

    public Message sendTextMessage(String name,String msg){
        return jMessageModel.sendTextMesage(name,msg);
    }

    public Message sendImageMessage(String name,File msg){
        return jMessageModel.sendImageMessage(name,msg);
    }

    public void deleteConversation(String targetName){
        jMessageModel.deleteConversation(targetName);
    }
}

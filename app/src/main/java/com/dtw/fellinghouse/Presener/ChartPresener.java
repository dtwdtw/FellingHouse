package com.dtw.fellinghouse.Presener;

import android.content.Context;

import com.dtw.fellinghouse.Model.JMessageListener;
import com.dtw.fellinghouse.Model.JMessageModel;
import com.dtw.fellinghouse.View.Chart.ChartView;

import java.util.List;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class ChartPresener implements JMessageListener {
    ChartView chartView;

    public ChartPresener(ChartView chartView) {
        this.chartView = chartView;
    }

    @Override
    public void onMessage(Message message) {
        chartView.onMessage(message);
    }

    @Override
    public void onMessage(List<Message> messageList) {
        chartView.onMessage(messageList);
    }

    public void getLocalMessages(Context context, String targetName) {
        JMessageModel.getInstance(context).getSingleConversation(targetName);
    }
}

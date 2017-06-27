package com.dtw.fellinghouse.View.Chart;

import java.util.List;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public interface ChartView {
    void onMessage(Message message);
    void onMessage(List<Message> messageList);
}

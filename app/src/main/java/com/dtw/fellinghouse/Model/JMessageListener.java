package com.dtw.fellinghouse.Model;

import java.util.List;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public interface JMessageListener {
    void onMessage(Message message);
    void onMessage(List<Message> messageList);
}

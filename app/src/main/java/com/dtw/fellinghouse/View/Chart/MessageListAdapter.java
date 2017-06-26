package com.dtw.fellinghouse.View.Chart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtw.fellinghouse.R;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private Context context;
    private List<Message> messageList;
    public final int TYPE_TEXT = 0;
    public final int TYPE_IMAGE = 1;
    public final int TYPE_VOICE = 2;
    public final int TYPE_CUSTOM = 9;
    public final int TYPE_SEND = 10;
    public final int TYPE_RECEIVE = 20;

    public MessageListAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (messageList.get(position).getDirect()) {
            case receive:
                return TYPE_RECEIVE;
            case send:
                return TYPE_SEND;
            default:
                return TYPE_RECEIVE;
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_RECEIVE:
                itemView = LayoutInflater.from(context).inflate(R.layout.message_item_receive, parent, false);
                break;
            case TYPE_SEND:
                itemView = LayoutInflater.from(context).inflate(R.layout.message_item_send, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.message_item_receive, parent, false);
                break;
        }
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView img;
        FrameLayout voice;
        public MessageViewHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.text_text);
            img= (ImageView) itemView.findViewById(R.id.img_img);
            voice= (FrameLayout) itemView.findViewById(R.id.layout_voicd);
        }
    }
}

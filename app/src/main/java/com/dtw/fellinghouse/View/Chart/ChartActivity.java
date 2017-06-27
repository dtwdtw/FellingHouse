package com.dtw.fellinghouse.View.Chart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class ChartActivity extends BaseActivity implements ChartView{
    private List<Message> messageList=new ArrayList<>();
    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView= (RecyclerView) findViewById(R.id.recycleview_message_list);
        messageListAdapter=new MessageListAdapter(this,messageList);
        recyclerView.setAdapter(messageListAdapter);
    }

    @Override
    public void onMessage(Message message) {
        messageList.add(message);
        messageListAdapter.notifyItemInserted(messageList.size()-1);
    }

    @Override
    public void onMessage(List<Message> messageList) {
        messageList.addAll(messageList);
        messageListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

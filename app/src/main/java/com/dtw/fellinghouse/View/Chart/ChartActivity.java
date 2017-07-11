package com.dtw.fellinghouse.View.Chart;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dtw.fellinghouse.Bean.KeyBoardMoreItemBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Presener.ChartPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.Utils.SPUtil;
import com.dtw.fellinghouse.Utils.UriUtil;
import com.dtw.fellinghouse.View.BaseActivity;
import com.jawnnypoo.physicslayout.Physics;
import com.jawnnypoo.physicslayout.PhysicsLinearLayout;

import org.jbox2d.dynamics.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.Message;

import static android.R.attr.path;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class ChartActivity extends BaseActivity implements ChartView ,SoftKeyBoardListener.OnSoftKeyBoardChangeListener,SimpleOnRecycleItemClickListener{
    private List<Message> messageListMain = new ArrayList<>();
    private List<KeyBoardMoreItemBean> keyBoardMoreItemBeanList=new ArrayList<>();
    private RecyclerView recyclerView,keyBoardMoreRV;
    private EditText msgEdit;
    private ImageButton sendImgBtn, moreImgBtn;
    private MessageListAdapter messageListAdapter;
    private ChartPresener chartPresener;
    private boolean isKeyboardShow=false;
    private boolean isMoreRVShow=false;
    private InputMethodManager imm;
    private PhysicsLinearLayout messagePhysical;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycleview_message_list);
        keyBoardMoreRV= (RecyclerView) findViewById(R.id.recycle_more_keyboard);
        msgEdit = (EditText) findViewById(R.id.edit_text);
        sendImgBtn = (ImageButton) findViewById(R.id.imgbtn_send);
        moreImgBtn = (ImageButton) findViewById(R.id.imgbtn_more);
        messagePhysical= (PhysicsLinearLayout) findViewById(R.id.physical_message);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewItemDecoration());
        messageListAdapter = new MessageListAdapter(this, messageListMain);
        messageListAdapter.setSimpleOnRecycleItemClickListener(this);
        recyclerView.setAdapter(messageListAdapter);

        KeyBoardMoreItemBean keyBoardMoreItemBean=new KeyBoardMoreItemBean();
        keyBoardMoreItemBean.setImgResID(R.drawable.gallery);
        keyBoardMoreItemBean.setTitle("图库");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        keyBoardMoreItemBean.setIntent(intent);
        keyBoardMoreItemBeanList.add(keyBoardMoreItemBean);

        MoreKeyboardAdapter moreKeyboardAdapter=new MoreKeyboardAdapter(this,keyBoardMoreItemBeanList);
        moreKeyboardAdapter.setSimpleOnRecycleItemClickListener(this);
        keyBoardMoreRV.setLayoutManager(new GridLayoutManager(this,4));
        keyBoardMoreRV.setAdapter(moreKeyboardAdapter);

        SoftKeyBoardListener.setListener(this,this);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        //判断是否需要显示发送按钮
        msgEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    sendImgBtn.setVisibility(View.VISIBLE);
                    moreImgBtn.setVisibility(View.GONE);
                } else {
                    sendImgBtn.setVisibility(View.GONE);
                    moreImgBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        msgEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    hideMoreRV();
                }
                return false;
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    hideMoreRV();
                    imm.hideSoftInputFromWindow(msgEdit.getWindowToken(), 0);
                    unlockContentHeightDelayed();
                }
                return false;
            }
        });

        chartPresener = new ChartPresener(this, this);
        chartPresener.getLocalMessages(Config.MasterName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart, menu);
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_send:
                messageListMain.add(chartPresener.sendTextMessage(Config.MasterName, msgEdit.getText().toString()));
                msgEdit.getText().clear();
                messageListAdapter.notifyItemInserted(messageListMain.size() - 1);
                recyclerView.scrollToPosition(messageListMain.size()-1);
                break;
            case R.id.imgbtn_more:
                if(isKeyboardShow) {
                    lockContentHeight();
                    imm.hideSoftInputFromWindow(msgEdit.getWindowToken(), 0);
                    showMoreRV();
                }else{
                    showMoreRV();
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lockContentHeight();
                        }
                    },50);
                }
                break;
//            case R.id.edit_text:
//                hideMoreRV();
//                Log.v("dtw","edittextClick");
//                break;
            case R.id.recycleview_message_list:
                unlockContentHeightDelayed();
                imm.hideSoftInputFromWindow(msgEdit.getWindowToken(), 0);
                hideMoreRV();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.delete_conversation:
                movieChild(recyclerView,messagePhysical);
                messageListMain.clear();
                chartPresener.deleteConversation(Config.MasterName);
                messageListAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chartPresener.onViewResume(Config.MasterName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chartPresener.onViewPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Config.Request_Code_Request_Image) {
            if(resultCode==RESULT_OK) {
                try {
                    messageListMain.add(chartPresener.sendImageMessage(Config.MasterName,UriUtil.getFileFormUri(this,data.getData())));
                    messageListAdapter.notifyItemInserted(messageListMain.size() - 1);
                    recyclerView.scrollToPosition(messageListMain.size()-1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
//            Intent intent = new Intent(this, ImageFilterActivity.class);
//            intent.setData(data.getData());
//            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if(isMoreRVShow){
            hideMoreRV();
            unlockContentHeightDelayed();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMessage(Message message) {
        messageListMain.add(message);
        messageListAdapter.notifyItemInserted(messageListMain.size() - 1);
        recyclerView.scrollToPosition(messageListMain.size() - 1);
    }

    @Override
    public void onMessage(List<Message> messageList) {
        messageListMain.addAll(messageList);
        messageListAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageListMain.size() - 1);
    }

    @Override
    public void onRecycleItemClick(String className, View v, int position) {
        if(MessageListAdapter.class.getName().equals(className)){

        }else if(MoreKeyboardAdapter.class.getName().equals(className)){
            startActivityForResult(keyBoardMoreItemBeanList.get(position).getIntent(),Config.Request_Code_Request_Image);
            hideMoreRV();
            unlockContentHeightDelayed();
        }
    }

    private void movieChild(ViewGroup parentFrom,ViewGroup parentTo){

    }

    private void lockContentHeight(){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                recyclerView.getLayoutParams();
        params.height = recyclerView.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        msgEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams=((LinearLayout.LayoutParams) recyclerView.getLayoutParams());
                layoutParams.weight=1.0F;
                recyclerView.setLayoutParams(layoutParams);
            }
        }, 50);
    }

    @Override
    public void keyBoardShow(int height) {
        isKeyboardShow =true;
        new SPUtil(this).setKeyBoardHeight(height);
        recyclerView.scrollToPosition(messageListMain.size()-1);
        Log.v("dtw","height:"+height);
    }

    @Override
    public void keyBoardHide(int height) {
        isKeyboardShow =false;
        if(!isMoreRVShow) {
            unlockContentHeightDelayed();
        }
        Log.v("dtw","onhideimm");
    }

    public void showMoreRV(){
        int backedHeight = new SPUtil(this).getKeyBoardHeight();
        if (backedHeight > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) keyBoardMoreRV.getLayoutParams();
            layoutParams.height = backedHeight;
        }
        keyBoardMoreRV.setVisibility(View.VISIBLE);
        recyclerView.scrollToPosition(messageListMain.size() - 1);
        isMoreRVShow=true;
        Log.v("dtw","showHeight:"+backedHeight);
    }

    public void hideMoreRV(){
        keyBoardMoreRV.setVisibility(View.GONE);
        isMoreRVShow=false;
    }
}

package com.dtw.fellinghouse.View;

import android.view.View;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public interface SimpleOnRecycleItemClickListener {
    void onRecycleItemClick(String adapterClassName,View v,int position);
    void onRecycleItemLongClick(String adapterClassName,View v,int position);
}

package com.dtw.fellinghouse.View.Chart;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dtw.fellinghouse.Utils.ScreenUtil;

/**
 * Created by Administrator on 2017/6/16 0016.
 */

public class RecycleViewItemDecoration extends RecyclerView.ItemDecoration {
    int left,top,right,bottom;
    public RecycleViewItemDecoration(int left,int top,int right,int bottom){
        this.left=left;
        this.top=top;
        this.right=right;
        this.bottom=bottom;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(left,top,right,bottom);
    }
}

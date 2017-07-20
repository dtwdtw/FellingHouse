package com.dtw.fellinghouse.Model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17 0017.
 */

public interface NetListener {
    <T>void onData(T dataList);
}

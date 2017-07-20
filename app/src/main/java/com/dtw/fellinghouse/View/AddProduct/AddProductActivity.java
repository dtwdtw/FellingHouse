package com.dtw.fellinghouse.View.AddProduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.BaseActivity;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class AddProductActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivity(intent);
    }
}

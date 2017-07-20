package com.dtw.fellinghouse.View.Main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.Chart.ChartActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class ProductBrowserDialog extends Dialog implements OnBannerListener, View.OnClickListener {
    private Context context;
    private ProductBean productBean;
    private Banner imageBrowserBanner;
    private ImageButton infoImBtn;
    private TextView nameText,descripeText,locationText;
    public ProductBrowserDialog(@NonNull ProductBean productBean,@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context=context;
        this.productBean=productBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product_browser);
        nameText= (TextView) findViewById(R.id.text_name);
        nameText.setText(productBean.getName());
        descripeText= (TextView) findViewById(R.id.text_descripe);
        descripeText.setText(productBean.getDescripe());
        locationText= (TextView) findViewById(R.id.text_location);
        locationText.setText(productBean.getLocationName());

        imageBrowserBanner= (Banner) findViewById(R.id.banner_dialog_imagebrowser);
        imageBrowserBanner.setImageLoader(new DialogImageBrowserImageLoader());
        imageBrowserBanner.setImages(productBean.getProductImgNameList());
        imageBrowserBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        imageBrowserBanner.setOnBannerListener(this);
        imageBrowserBanner.start();

        infoImBtn= (ImageButton) findViewById(R.id.imgbtn_info);
        infoImBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgbtn_info:
                context.startActivity(new Intent(context, ChartActivity.class));
                this.dismiss();
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {
        this.dismiss();
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }
}

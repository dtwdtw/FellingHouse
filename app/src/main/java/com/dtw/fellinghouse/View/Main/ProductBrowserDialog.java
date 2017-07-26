package com.dtw.fellinghouse.View.Main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Config;
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
    private TextView nameText,descripeText,locationText,priceDayText,priceWeekText,priceMonthText,stateText;
    private boolean isLogin;
    public ProductBrowserDialog(@NonNull ProductBean productBean,@NonNull Context context, boolean isLogin,@StyleRes int themeResId) {
        super(context, themeResId);
        this.context=context;
        this.productBean=productBean;
        this.isLogin=isLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product_browser);
        nameText= (TextView) findViewById(R.id.text_name);
        descripeText= (TextView) findViewById(R.id.text_descripe);
        locationText= (TextView) findViewById(R.id.text_location);
        priceDayText= (TextView) findViewById(R.id.text_price_day);
        priceWeekText= (TextView) findViewById(R.id.text_price_week);
        priceMonthText= (TextView) findViewById(R.id.text_price_month);
        stateText= (TextView) findViewById(R.id.text_state);

        nameText.setText(productBean.getName());
        descripeText.setText(productBean.getDescripe());
        locationText.setText(productBean.getLocationName());
        priceDayText.setText(productBean.getPriceDay()>0?("日租:"+productBean.getPriceDay()):"");
        priceWeekText.setText(productBean.getPriceDay()>0?("  周租:"+productBean.getPriceWeek()):"");
        priceMonthText.setText(productBean.getPriceDay()>0?("  月租:"+productBean.getPriceMonth()):"");
        stateText.setText(TextUtils.isEmpty(productBean.getTenantName())?"状态:急切的等待新主人":"状态：已有贵宾入住");

        imageBrowserBanner= (Banner) findViewById(R.id.banner_dialog_imagebrowser);
        imageBrowserBanner.setImageLoader(new DialogImageBrowserImageLoader());
        imageBrowserBanner.setImages(productBean.getProductImgNameList());
        imageBrowserBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        imageBrowserBanner.setOnBannerListener(this);
        imageBrowserBanner.start();

        infoImBtn= (ImageButton) findViewById(R.id.imgbtn_info);
        infoImBtn.setOnClickListener(this);
        if(!isLogin){
            infoImBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgbtn_info:
                Intent intent=new Intent(context,ChartActivity.class);
                intent.putExtra(Config.Key_Product,productBean);
                context.startActivity(intent);
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

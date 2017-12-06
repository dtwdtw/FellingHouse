package com.dtw.fellinghouse.View.Main;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.QiNiuModel;
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
    private ImageButton infoImBtn, navigeteImgBtn;
    private TextView nameText, descripeText, locationText, priceDayText, priceWeekText, priceMonthText, stateText;
    private boolean isAdmin = false;
    private boolean isLogin;
    private Rect rect;
    private Window window;

    public ProductBrowserDialog(@NonNull ProductBean productBean, @NonNull Context context, boolean isLogin, boolean isAdmin, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.productBean = productBean;
        this.isLogin = isLogin;
        this.isAdmin = isAdmin;
        this.rect = rect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product_browser);
        nameText = (TextView) findViewById(R.id.text_name);
        descripeText = (TextView) findViewById(R.id.text_descripe);
        locationText = (TextView) findViewById(R.id.text_location);
        priceDayText = (TextView) findViewById(R.id.text_price_day);
        priceWeekText = (TextView) findViewById(R.id.text_price_week);
        priceMonthText = (TextView) findViewById(R.id.text_price_month);
        stateText = (TextView) findViewById(R.id.text_state);

        nameText.setText(productBean.getName());
        descripeText.setText(productBean.getDescripe());
        locationText.setText(productBean.getLocationName());
        priceDayText.setText(productBean.getPriceDay() > 0 ? ("日租:" + productBean.getPriceDay()) : "");
        priceWeekText.setText(productBean.getPriceDay() > 0 ? ("  周租:" + productBean.getPriceWeek()) : "");
        priceMonthText.setText(productBean.getPriceDay() > 0 ? ("  月租:" + productBean.getPriceMonth()) : "");
        stateText.setText(productBean.getState() ? "状态:急切的等待新主人" : "状态：已有贵宾入住");

        imageBrowserBanner = (Banner) findViewById(R.id.banner_dialog_imagebrowser);
        imageBrowserBanner.setImageLoader(new DialogImageBrowserImageLoader());
        imageBrowserBanner.setImages(productBean.getProductImgNameList());
        imageBrowserBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        imageBrowserBanner.setOnBannerListener(this);
        imageBrowserBanner.start();

        navigeteImgBtn = (ImageButton) findViewById(R.id.imgbtn_navigate);
        navigeteImgBtn.setOnClickListener(this);

        infoImBtn = (ImageButton) findViewById(R.id.imgbtn_info);
        infoImBtn.setOnClickListener(this);
        if (!isLogin) {
            infoImBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_info:
                Intent intent = new Intent(context, ChartActivity.class);
                intent.putExtra(Config.Key_Product, productBean);
                intent.putExtra(Config.Key_Is_Admin, isAdmin);
                context.startActivity(intent);
                break;
            case R.id.imgbtn_navigate:
                Uri mUri = Uri.parse("geo:" + productBean.getLocationLatitude() + "," + productBean.getLocationLongitude() + "?q=" + productBean.getLocationName());
                Log.v("dtw", "la:" + productBean.getLocationLatitude() + "  ln:" + productBean.getLocationLongitude());
                Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
                if (mIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mIntent);
                } else {
                    Toast.makeText(context, "当前设备没有找到可用的导航软件", Toast.LENGTH_SHORT).show();
                }
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
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }
}

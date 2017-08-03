package com.dtw.fellinghouse.View.Main;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class DialogImageBrowserImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.drawable.nav_head)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context)
                .load(new QiNiuModel().getPublicUrl((String) path))
                .apply(options)
                .into(imageView);
    }
}

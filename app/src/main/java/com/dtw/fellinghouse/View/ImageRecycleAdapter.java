package com.dtw.fellinghouse.View;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.Utils.UriUtil;
import com.mob.tools.gui.BitmapProcessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class ImageRecycleAdapter extends RecyclerView.Adapter<ImageRecycleAdapter.ImageViewHolder> {
    private SimpleOnRecycleItemClickListener simpleOnRecycleItemClickListener;
    private Context context;
    private List<String> uriList;
    private int footResourceID = -1;

    public ImageRecycleAdapter(Context context, List<String> uriList) {
        this.context = context;
        this.uriList = uriList;
    }

    public void setSimpleOnRecycleItemClickListener(SimpleOnRecycleItemClickListener simpleOnRecycleItemClickListener) {
        this.simpleOnRecycleItemClickListener = simpleOnRecycleItemClickListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_image_recycle, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        if (position < uriList.size()) {
            if(uriList.get(position).startsWith("http")) {
                RequestOptions glideOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(context)
                        .load(uriList.get(position))
                        .apply(glideOptions)
                        .into(holder.imageView);
            }else{
                InputStream input = null;
                try {
                    input = context.getContentResolver().openInputStream(Uri.parse(uriList.get(position)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, null, bitmapOptions);
                int w, h;
                if (bitmapOptions.outWidth > 540) {
                    w = 540;
                    h = bitmapOptions.outHeight * 540 / bitmapOptions.outWidth;
                } else {
                    w = bitmapOptions.outWidth;
                    h = bitmapOptions.outHeight;
                }
                Log.v("dtw", "w:" + w + " h:" + h);
                RequestOptions glideOptions = new RequestOptions()
                        .override(w, h)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                Glide.with(context)
                        .load(uriList.get(position))
                        .apply(glideOptions)
                        .into(holder.imageView);
            }
        } else if (footResourceID != -1) {
            holder.imageView.setImageResource(footResourceID);
        }

    }

    @Override
    public int getItemCount() {
        if (footResourceID == -1) {
            return uriList.size();
        } else {
            return uriList.size() + 1;
        }
    }

    public void setFootImageResource(int resourceID) {
        this.footResourceID = resourceID;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_img);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (simpleOnRecycleItemClickListener != null) {
                        simpleOnRecycleItemClickListener.onRecycleItemClick(ImageRecycleAdapter.class.getName(), v, getAdapterPosition());
                    }
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (simpleOnRecycleItemClickListener != null) {
                        simpleOnRecycleItemClickListener.onRecycleItemLongClick(ImageRecycleAdapter.class.getName(), v, getAdapterPosition());
                    }
                    return false;
                }
            });
        }

    }
}

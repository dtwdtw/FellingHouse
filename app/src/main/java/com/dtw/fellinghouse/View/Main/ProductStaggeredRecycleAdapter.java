package com.dtw.fellinghouse.View.Main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.SimpleOnRecycleItemClickListener;

import java.util.List;

/**
 * Created by Administrator on 2017/7/15 0015.
 */

public class ProductStaggeredRecycleAdapter extends RecyclerView.Adapter<ProductStaggeredRecycleAdapter.ProductViewHolder> {
    private List<ProductBean> productBeanList;
    private Context context;
    private SimpleOnRecycleItemClickListener simpleOnRecycleItemClickListener;
    public ProductStaggeredRecycleAdapter(Context context, List<ProductBean> productBeanList){
        this.context=context;
        this.productBeanList = productBeanList;
    }
    public void setSimpleOnRecycleItemClickListener(SimpleOnRecycleItemClickListener simpleOnRecycleItemClickListener){
        this.simpleOnRecycleItemClickListener=simpleOnRecycleItemClickListener;
    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(context).inflate(R.layout.item_product_simple,parent,false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return productBeanList.size();
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductBean productBean = productBeanList.get(position);
        holder.locationText.setText(productBean.getLocationName());
        RequestOptions options = new RequestOptions()
                .error(R.drawable.nav_head)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context)
                .load(QiNiuModel.getInstance().getPublicUrl(productBean.getProductImgNameList().get(0)))
                .apply(options)
                .into(holder.productImage);
//        Log.v("dtw","imageURL:"+QiNiuModel.getInstance().getPrivateUrl(productBean.getProductImgNameList().get(0)));
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView locationText;

        public ProductViewHolder(final View itemView) {
            super(itemView);
            productImage= (ImageView) itemView.findViewById(R.id.img_product);
            locationText= (TextView) itemView.findViewById(R.id.text_location);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(simpleOnRecycleItemClickListener!=null){
                        simpleOnRecycleItemClickListener.onRecycleItemClick(ProductStaggeredRecycleAdapter.class.getName(),itemView,getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(simpleOnRecycleItemClickListener!=null){
                        simpleOnRecycleItemClickListener.onRecycleItemLongClick(ProductStaggeredRecycleAdapter.class.getName(),itemView,getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }
}

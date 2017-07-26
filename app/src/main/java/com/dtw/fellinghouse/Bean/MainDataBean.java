package com.dtw.fellinghouse.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class MainDataBean implements Parcelable{

    /**
     * lastID : 0
     * productList : [{"id":0,"name":"测试","descripe":"奢华测试","locationName":"天津","locationLatitude":36.30556423523153,"locationLongitude":104.48060937499996,"priceDay":0,"priceWeek":0,"priceMonth":0,"priceOriginal":0,"priceDecoration":0,"createTime":"2017-05-23","updateTime":"2017-06-25","state":"out","onerName":"王五","onerPhone":"123","onerID":"142225","onerDescripe":"好","tenantName":"李四","tenantPhone":"1234","tenantID":"145588","tenantPrice":12,"tenantType":"Day","tenantLength":2,"tenantDescripe":"好","productImgNameList":["FhieYtYGtSKnhmoFPItl-OqLdFLZ","Fqo6eh2C8fTLxDQoW3","factory-2389587_960_720.png","illustration-1546834_960_720.png"]}]
     */

    private int lastID;
    private List<ProductBean> productList;

    protected MainDataBean(Parcel in) {
        lastID = in.readInt();
        productList = in.createTypedArrayList(ProductBean.CREATOR);
    }

    public static final Creator<MainDataBean> CREATOR = new Creator<MainDataBean>() {
        @Override
        public MainDataBean createFromParcel(Parcel in) {
            return new MainDataBean(in);
        }

        @Override
        public MainDataBean[] newArray(int size) {
            return new MainDataBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lastID);
        dest.writeTypedList(productList);
    }

    public int getLastID() {
        return lastID;
    }

    public void setLastID(int lastID) {
        this.lastID = lastID;
    }

    public List<ProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }
}

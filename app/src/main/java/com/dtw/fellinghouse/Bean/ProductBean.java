package com.dtw.fellinghouse.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public  class ProductBean implements Parcelable{
    /**
     * id : 0
     * name : 测试
     * descripe : 奢华测试
     * locationName : 天津
     * locationLatitude : 36.30556423523153
     * locationLongitude : 104.48060937499996
     * priceDay : 0
     * priceWeek : 0
     * priceMonth : 0
     * priceOriginal : 0
     * priceDecoration : 0
     * createTime : 2017-05-23
     * updateTime : 2017-06-25
     * state : out
     * onerName : 王五
     * onerPhone : 123
     * onerID : 142225
     * onerDescripe : 好
     * tenantName : 李四
     * tenantPhone : 1234
     * tenantID : 145588
     * tenantPrice : 12
     * tenantType : Day
     * tenantLength : 2
     * tenantDescripe : 好
     * productImgNameList : ["FhieYtYGtSKnhmoFPItl-OqLdFLZ","Fqo6eh2C8fTLxDQoW3","factory-2389587_960_720.png","illustration-1546834_960_720.png"]
     */

    private int id;
    private String name;
    private String descripe;
    private String locationName;
    private double locationLatitude;
    private double locationLongitude;
    private long priceDay;
    private long priceWeek;
    private long priceMonth;
    private long priceOriginal;
    private long priceDecoration;
    private String createTime;
    private String updateTime;
    private String state;
    private String onerName;
    private String onerPhone;
    private String onerID;
    private String onerDescripe;
    private String originalStartTime;
    private String originalEndTime;
    private String tenantName;
    private String tenantPhone;
    private String tenantID;
    private long tenantPrice;
    private String tenantType;
    private int tenantLength;
    private String tenantDescripe;
    private List<String> productImgNameList;

    public ProductBean(){}

    protected ProductBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        descripe = in.readString();
        locationName = in.readString();
        locationLatitude = in.readDouble();
        locationLongitude = in.readDouble();
        priceDay = in.readLong();
        priceWeek = in.readLong();
        priceMonth = in.readLong();
        priceOriginal = in.readLong();
        priceDecoration = in.readLong();
        createTime = in.readString();
        updateTime = in.readString();
        state = in.readString();
        onerName = in.readString();
        onerPhone = in.readString();
        onerID = in.readString();
        onerDescripe = in.readString();
        originalStartTime = in.readString();
        originalEndTime = in.readString();
        tenantName = in.readString();
        tenantPhone = in.readString();
        tenantID = in.readString();
        tenantPrice = in.readLong();
        tenantType = in.readString();
        tenantLength = in.readInt();
        tenantDescripe = in.readString();
        productImgNameList = in.createStringArrayList();
    }

    public static final Creator<ProductBean> CREATOR = new Creator<ProductBean>() {
        @Override
        public ProductBean createFromParcel(Parcel in) {
            return new ProductBean(in);
        }

        @Override
        public ProductBean[] newArray(int size) {
            return new ProductBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(descripe);
        dest.writeString(locationName);
        dest.writeDouble(locationLatitude);
        dest.writeDouble(locationLongitude);
        dest.writeLong(priceDay);
        dest.writeLong(priceWeek);
        dest.writeLong(priceMonth);
        dest.writeLong(priceOriginal);
        dest.writeLong(priceDecoration);
        dest.writeString(createTime);
        dest.writeString(updateTime);
        dest.writeString(state);
        dest.writeString(onerName);
        dest.writeString(onerPhone);
        dest.writeString(onerID);
        dest.writeString(onerDescripe);
        dest.writeString(originalStartTime);
        dest.writeString(originalEndTime);
        dest.writeString(tenantName);
        dest.writeString(tenantPhone);
        dest.writeString(tenantID);
        dest.writeLong(tenantPrice);
        dest.writeString(tenantType);
        dest.writeInt(tenantLength);
        dest.writeString(tenantDescripe);
        dest.writeStringList(productImgNameList);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripe() {
        return descripe;
    }

    public void setDescripe(String descripe) {
        this.descripe = descripe;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public long getPriceDay() {
        return priceDay;
    }

    public void setPriceDay(long priceDay) {
        this.priceDay = priceDay;
    }

    public long getPriceWeek() {
        return priceWeek;
    }

    public void setPriceWeek(long priceWeek) {
        this.priceWeek = priceWeek;
    }

    public long getPriceMonth() {
        return priceMonth;
    }

    public void setPriceMonth(long priceMonth) {
        this.priceMonth = priceMonth;
    }

    public long getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(long priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public long getPriceDecoration() {
        return priceDecoration;
    }

    public void setPriceDecoration(long priceDecoration) {
        this.priceDecoration = priceDecoration;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOnerName() {
        return onerName;
    }

    public void setOnerName(String onerName) {
        this.onerName = onerName;
    }

    public String getOnerPhone() {
        return onerPhone;
    }

    public void setOnerPhone(String onerPhone) {
        this.onerPhone = onerPhone;
    }

    public String getOnerID() {
        return onerID;
    }

    public void setOnerID(String onerID) {
        this.onerID = onerID;
    }

    public String getOnerDescripe() {
        return onerDescripe;
    }

    public void setOnerDescripe(String onerDescripe) {
        this.onerDescripe = onerDescripe;
    }

    public String getOriginalStartTime() {
        return originalStartTime;
    }

    public void setOriginalStartTime(String originalStartTime) {
        this.originalStartTime = originalStartTime;
    }

    public String getOriginalEndTime() {
        return originalEndTime;
    }

    public void setOriginalEndTime(String originalEndTime) {
        this.originalEndTime = originalEndTime;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantPhone() {
        return tenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        this.tenantPhone = tenantPhone;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public long getTenantPrice() {
        return tenantPrice;
    }

    public void setTenantPrice(long tenantPrice) {
        this.tenantPrice = tenantPrice;
    }

    public String getTenantType() {
        return tenantType;
    }

    public void setTenantType(String tenantType) {
        this.tenantType = tenantType;
    }

    public int getTenantLength() {
        return tenantLength;
    }

    public void setTenantLength(int tenantLength) {
        this.tenantLength = tenantLength;
    }

    public String getTenantDescripe() {
        return tenantDescripe;
    }

    public void setTenantDescripe(String tenantDescripe) {
        this.tenantDescripe = tenantDescripe;
    }

    public List<String> getProductImgNameList() {
        return productImgNameList;
    }

    public void setProductImgNameList(List<String> productImgNameList) {
        this.productImgNameList = productImgNameList;
    }
}
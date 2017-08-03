package com.dtw.fellinghouse.View.AddProduct;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.dtw.fellinghouse.Bean.LocationQQBean;
import com.dtw.fellinghouse.Bean.LocationsBean;
import com.dtw.fellinghouse.Bean.MainDataBean;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Presener.AddProductPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.Utils.UriUtil;
import com.dtw.fellinghouse.View.BaseActivity;
import com.dtw.fellinghouse.View.ImageRecycleAdapter;
import com.dtw.fellinghouse.View.Login.LoginView;
import com.dtw.fellinghouse.View.SimpleOnRecycleItemClickListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class AddProductActivity extends BaseActivity implements SimpleOnRecycleItemClickListener, AddProductView, LocationListener {
    private AddProductPresener addProductPresener;
    private RecyclerView productImageListRecycle;
    private ImageRecycleAdapter imageRecycleAdapter;
    private List<String> uriList = new ArrayList<>();
    private EditText productName, productDescripe, onerName, onerPhone, onerID, originalStartTime, originalEndTime, onerDescripe, originalPrice;
    private MainDataBean mainDataBean;
    private LocationManager locationManager;
    private Location location = null;
    private LocationsBean locationsBean=new LocationsBean();
    private LocationQQBean locationQQBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addProductPresener = new AddProductPresener(this, this);

        productName = (EditText) findViewById(R.id.edittext_name);
        productDescripe = (EditText) findViewById(R.id.edittext_descripe);
        originalStartTime = (EditText) findViewById(R.id.edittext_original_starttime);
        originalEndTime = (EditText) findViewById(R.id.edittext_original_endtime);
        onerName = (EditText) findViewById(R.id.edittext_oner_name);
        onerPhone = (EditText) findViewById(R.id.edittext_oner_phone);
        onerID = (EditText) findViewById(R.id.edittext_oner_id);
        onerDescripe = (EditText) findViewById(R.id.edittext_oner_descripe);
        originalPrice = (EditText) findViewById(R.id.edittext_original_price);

        productImageListRecycle = (RecyclerView) findViewById(R.id.recycle_product_img_list);
        productImageListRecycle.setLayoutManager(new StaggeredGridLayoutManager(4, RecyclerView.VERTICAL));
        imageRecycleAdapter = new ImageRecycleAdapter(this, uriList);
        imageRecycleAdapter.setFootImageResource(R.drawable.icon_add_pic);
        imageRecycleAdapter.setSimpleOnRecycleItemClickListener(this);
        productImageListRecycle.setAdapter(imageRecycleAdapter);

        mainDataBean = getIntent().getParcelableExtra(Config.Key_Main_Product);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
            getLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.Request_Code_Request_Image) {
            if (resultCode == RESULT_OK) {
                uriList.add(data.getData().toString());
                imageRecycleAdapter.notifyItemInserted(uriList.size() - 1);
                productImageListRecycle.scrollToPosition(uriList.size());
                if(locationsBean.getLat()==0&&locationsBean.getLng()==0) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        try {
                            ExifInterface exifInterface = new ExifInterface(getContentResolver().openInputStream(data.getData()));
                            float[] latlong=new float[2];
                            exifInterface.getLatLong(latlong);
                            Log.v("dtw","size:"+latlong.length);
                            locationsBean.setLat(latlong[0]);
                            locationsBean.setLng(latlong[1]);
                            Log.v("dtw","pic location - la:"+locationsBean.getLat()+"  ln:"+locationsBean.getLng());
                            if(locationsBean.getLat()==0&&locationsBean.getLng()==0){
                                getLocation();
                            }else{
                                addProductPresener.getLocationDescripe(locationsBean);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.v("dtw", "uri:" + data.getData());
            }
        }
        Log.v("dtw", "resultCode:" + resultCode + " ok:" + RESULT_OK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Config.Request_Code_Permission_Location:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                break;
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Config.Request_Code_Permission_Location);
        } else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            String locationProvider = null;
            //获取当前可用的位置控制器
            List<String> locationProviderList = locationManager.getProviders(true);
            if (locationProviderList.contains(LocationManager.GPS_PROVIDER)) {
                //是否为GPS位置控制器
                locationProvider = LocationManager.GPS_PROVIDER;
            } else if (locationProviderList.contains(LocationManager.NETWORK_PROVIDER)) {
                //是否为网络位置控制器
                locationProvider = LocationManager.NETWORK_PROVIDER;

            } else {
                Toast.makeText(this, "请检查网络或GPS是否打开",
                        Toast.LENGTH_LONG).show();
                return;
            }
            location = locationManager.getLastKnownLocation(locationProvider);
            if (location == null) {
                //绑定定位事件，监听位置是否改变
                //第一个参数为控制器类型第二个参数为监听位置变化的时间间隔（单位：毫秒）
                //第三个参数为位置变化的间隔（单位：米）第四个参数为位置监听器
                locationManager.requestLocationUpdates(locationProvider, 2000, 0, this);
            } else {
                Log.v("dtw", location.toString());
                LocationsBean locationsBean=new LocationsBean();
                locationsBean.setLat(location.getLatitude());
                locationsBean.setLng(location.getLongitude());
                addProductPresener.getLocationDescripe(locationsBean);
            }
        }
    }

    //<editor-fold desc="定位回调接口">
    @Override
    public void onLocationChanged(Location location) {
        getLocation();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    //</editor-fold>

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationBean(LocationQQBean locationQQBean) {
        this.locationQQBean = locationQQBean;
        Log.v("dtw","Location:"+locationQQBean.getResult().getAddress());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edittext_original_starttime:
                DatePicker datePickerStart = new DatePicker(this);
                final AlertDialog.Builder builderStart = new AlertDialog.Builder(this);
                builderStart.setView(datePickerStart);
                final AlertDialog alertDialogStart = builderStart.show();
                Calendar calendarStart = Calendar.getInstance();
                if (!TextUtils.isEmpty(originalStartTime.getText())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = simpleDateFormat.parse(originalStartTime.getText().toString());
                        calendarStart.setTime(date);
                        calendarStart.set(Calendar.MONTH, calendarStart.get(Calendar.MONTH) - 1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                datePickerStart.init(calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH) + 1, calendarStart.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        originalStartTime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        alertDialogStart.dismiss();
                        Log.v("dtw", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                });
                break;
            case R.id.edittext_original_endtime:
                DatePicker datePickerEnd = new DatePicker(this);
                final AlertDialog.Builder builderEnd = new AlertDialog.Builder(this);
                builderEnd.setView(datePickerEnd);
                final AlertDialog alertDialogEnd = builderEnd.show();
                Calendar calendarEnd = Calendar.getInstance();
                if (!TextUtils.isEmpty(originalEndTime.getText())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = simpleDateFormat.parse(originalEndTime.getText().toString());
                        calendarEnd.setTime(date);
                        calendarEnd.set(Calendar.MONTH, calendarEnd.get(Calendar.MONTH) - 1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                datePickerEnd.init(calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH) + 1, calendarEnd.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        originalEndTime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        alertDialogEnd.dismiss();
                        Log.v("dtw", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                });
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_down:
                Log.v("dtw", "menu down click");
                ProductBean productBeanWithOutImageList = new ProductBean();
                mainDataBean.setLastID(mainDataBean.getLastID() + 1);
                productBeanWithOutImageList.setId(mainDataBean.getLastID());
                productBeanWithOutImageList.setName(productName.getText().toString());
                productBeanWithOutImageList.setDescripe(productDescripe.getText().toString());
                productBeanWithOutImageList.setOriginalStartTime(originalStartTime.getText().toString());
                productBeanWithOutImageList.setOriginalEndTime(originalEndTime.getText().toString());
                productBeanWithOutImageList.setOnerName(onerName.getText().toString());
                productBeanWithOutImageList.setOnerPhone(onerPhone.getText().toString());
                productBeanWithOutImageList.setOnerID(onerID.getText().toString());
                productBeanWithOutImageList.setOnerDescripe(onerDescripe.getText().toString());
                productBeanWithOutImageList.setPriceOriginal(Long.valueOf(TextUtils.isEmpty(originalPrice.getText().toString()) ? "0" : originalPrice.getText().toString()));
                productBeanWithOutImageList.setState(true);
                productBeanWithOutImageList.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                if (location != null) {
                    productBeanWithOutImageList.setLocationLatitude(location.getLatitude());
                    productBeanWithOutImageList.setLocationLongitude(location.getLongitude());
                    Log.v("dtw", "location" + location.toString());
                }else{
                    productBeanWithOutImageList.setLocationLatitude(locationsBean.getLat());
                    productBeanWithOutImageList.setLocationLongitude(locationsBean.getLng());
                }

                if (locationQQBean != null) {
                    productBeanWithOutImageList.setLocationName(locationQQBean.getResult().getAddress());
                }
                addProductPresener.insertProduct(mainDataBean, productBeanWithOutImageList, uriList);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecycleItemClick(String adapterClassName, View v, int position) {
        if (position == uriList.size()) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, Config.Request_Code_Request_Image);
        }
    }

    @Override
    public void onRecycleItemLongClick(String adapterClassName, View v, int position) {
        if (position < uriList.size()) {
            uriList.remove(position);
            imageRecycleAdapter.notifyItemRemoved(position);
        }
    }
}

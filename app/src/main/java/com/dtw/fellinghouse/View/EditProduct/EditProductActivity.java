package com.dtw.fellinghouse.View.EditProduct;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import com.dtw.fellinghouse.Bean.MainDataBean;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.Presener.EditProductPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.*;
import com.dtw.fellinghouse.View.ImageRecycleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class EditProductActivity extends BaseActivity implements EditProductView, SimpleOnRecycleItemClickListener {
    private EditProductPresener editProductPresener;
    private RecyclerView productImageListRecycle;
    private com.dtw.fellinghouse.View.ImageRecycleAdapter imageRecycleAdapter;
    private List<String> imgNameList=new ArrayList<>();
    private List<String> uriList = new ArrayList<>();
    private List<String> mixImgList=new ArrayList<>();
    private EditText productName, productDescripe, onerName, onerPhone, onerID, originalStartTime, originalEndTime, originalPrice,onerDescripe,priceDay,priceWeek,priceMonth,priceDecoration;
    private MainDataBean mainDataBean;
    private ProductBean editProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editProductPresener = new EditProductPresener(this, this);

        productName = (EditText) findViewById(R.id.edittext_name);
        productDescripe = (EditText) findViewById(R.id.edittext_descripe);
        originalStartTime = (EditText) findViewById(R.id.edittext_original_starttime);
        originalEndTime = (EditText) findViewById(R.id.edittext_original_endtime);
        originalPrice= (EditText) findViewById(R.id.edittext_original_price);
        onerName = (EditText) findViewById(R.id.edittext_oner_name);
        onerPhone = (EditText) findViewById(R.id.edittext_oner_phone);
        onerID = (EditText) findViewById(R.id.edittext_oner_id);
        onerDescripe = (EditText) findViewById(R.id.edittext_oner_descripe);
        priceDay= (EditText) findViewById(R.id.edittext_price_day);
        priceWeek= (EditText) findViewById(R.id.edittext_price_week);
        priceMonth= (EditText) findViewById(R.id.edittext_price_month);
        priceDecoration= (EditText) findViewById(R.id.edittext_decoration_price);

        productImageListRecycle = (RecyclerView) findViewById(R.id.recycle_product_img_list);
        productImageListRecycle.setLayoutManager(new StaggeredGridLayoutManager(4, RecyclerView.VERTICAL));
        imageRecycleAdapter = new ImageRecycleAdapter(this, mixImgList);
        imageRecycleAdapter.setFootImageResource(R.drawable.icon_add_pic);
        imageRecycleAdapter.setSimpleOnRecycleItemClickListener(this);
        productImageListRecycle.setAdapter(imageRecycleAdapter);

        mainDataBean = getIntent().getParcelableExtra(Config.Key_Main_Product);
        editProduct=getIntent().getParcelableExtra(Config.Key_Product);
        setTitle(editProduct.getName());

        productName.setText(editProduct.getName());
        productDescripe.setText(editProduct.getDescripe());
        originalPrice.setText(String.valueOf(editProduct.getPriceOriginal()));
        originalStartTime.setText(editProduct.getOriginalStartTime());
        originalEndTime.setText(editProduct.getOriginalEndTime());
        onerName.setText(editProduct.getOnerName());
        onerPhone.setText(editProduct.getOnerPhone());
        onerID.setText(editProduct.getOnerID());
        onerDescripe.setText(editProduct.getOnerDescripe());
        priceDay.setText(String.valueOf(editProduct.getPriceDay()));
        priceWeek.setText(String.valueOf(editProduct.getPriceWeek()));
        priceMonth.setText(String.valueOf(editProduct.getPriceMonth()));
        priceDecoration.setText(String.valueOf(editProduct.getPriceDecoration()));
        imgNameList=editProduct.getProductImgNameList();
        for(int i=0;i<imgNameList.size();i++){
            mixImgList.add(new QiNiuModel().getPublicUrl(imgNameList.get(i)));
        }
        imageRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.Request_Code_Request_Image) {
            if (resultCode == RESULT_OK) {
                uriList.add(data.getData().toString());
                mixImgList.add(data.getData().toString());
                imageRecycleAdapter.notifyItemInserted(mixImgList.size() - 1);
                productImageListRecycle.scrollToPosition(mixImgList.size());
                Log.v("dtw", "uri:" + data.getData());
            }
        }
        Log.v("dtw", "resultCode:" + resultCode + " ok:" + RESULT_OK);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
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
        getMenuInflater().inflate(R.menu.edit_product_menu, menu);
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
                productBeanWithOutImageList.setId(editProduct.getId());
                productBeanWithOutImageList.setName(productName.getText().toString());
                productBeanWithOutImageList.setDescripe(productDescripe.getText().toString());
                productBeanWithOutImageList.setPriceOriginal(Long.parseLong(originalPrice.getText().toString()));
                productBeanWithOutImageList.setOriginalStartTime(originalStartTime.getText().toString());
                productBeanWithOutImageList.setOriginalEndTime(originalEndTime.getText().toString());
                productBeanWithOutImageList.setOnerName(onerName.getText().toString());
                productBeanWithOutImageList.setOnerPhone(onerPhone.getText().toString());
                productBeanWithOutImageList.setOnerID(onerID.getText().toString());
                productBeanWithOutImageList.setOnerDescripe(onerDescripe.getText().toString());
                productBeanWithOutImageList.setPriceDay(Long.parseLong(priceDay.getText().toString()));
                productBeanWithOutImageList.setPriceWeek(Long.parseLong(priceWeek.getText().toString()));
                productBeanWithOutImageList.setPriceMonth(Long.parseLong(priceMonth.getText().toString()));
                productBeanWithOutImageList.setPriceDecoration(Long.parseLong(priceDecoration.getText().toString()));
                productBeanWithOutImageList.setLocationName(editProduct.getLocationName());
                productBeanWithOutImageList.setState(editProduct.getState());
                productBeanWithOutImageList.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm;ss").format(new Date()));
                productBeanWithOutImageList.setProductImgNameList(imgNameList);
                for(int i=0;i<mainDataBean.getProductList().size();i++){
                    if(mainDataBean.getProductList().get(i).getId()==editProduct.getId()){
                        mainDataBean.getProductList().remove(i);
                        break;
                    }
                }
                editProductPresener.insertProduct(mainDataBean, productBeanWithOutImageList, uriList);
                break;
            case R.id.menu_delete:
                editProductPresener.deleteProduct(mainDataBean,editProduct);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecycleItemClick(String adapterClassName, View v, int position) {
        if (position == mixImgList.size()) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, Config.Request_Code_Request_Image);
        }
    }

    @Override
    public void onRecycleItemLongClick(String adapterClassName, View v, int position) {
        if (position < mixImgList.size()) {
            if(position<imgNameList.size()){
                imgNameList.remove(position);
            }else if(position<uriList.size()){
                uriList.remove(position-imgNameList.size());
            }
            mixImgList.remove(position);
            imageRecycleAdapter.notifyItemRemoved(position);
        }
    }
}

package com.dtw.fellinghouse.View.AddProduct;

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
import com.dtw.fellinghouse.Presener.AddProductPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.BaseActivity;
import com.dtw.fellinghouse.View.ImageRecycleAdapter;
import com.dtw.fellinghouse.View.SimpleOnRecycleItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class AddProductActivity extends BaseActivity implements SimpleOnRecycleItemClickListener, AddProductView {
    private AddProductPresener addProductPresener;
    private RecyclerView productImageListRecycle;
    private ImageRecycleAdapter imageRecycleAdapter;
    private List<String> uriList = new ArrayList<>();
    private EditText productName, productDescripe, onerName, onerPhone, onerID, originalStartTime, originalEndTime, onerDescripe,originalPrice;
    private MainDataBean mainDataBean;

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
        originalPrice= (EditText) findViewById(R.id.edittext_original_price);

        productImageListRecycle = (RecyclerView) findViewById(R.id.recycle_product_img_list);
        productImageListRecycle.setLayoutManager(new StaggeredGridLayoutManager(4, RecyclerView.VERTICAL));
        imageRecycleAdapter = new ImageRecycleAdapter(this, uriList);
        imageRecycleAdapter.setFootImageResource(R.drawable.icon_add_pic);
        imageRecycleAdapter.setSimpleOnRecycleItemClickListener(this);
        productImageListRecycle.setAdapter(imageRecycleAdapter);

        mainDataBean = getIntent().getParcelableExtra(Config.Key_Main_Product);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.Request_Code_Request_Image) {
            if (resultCode == RESULT_OK) {
                uriList.add(data.getData().toString());
                imageRecycleAdapter.notifyItemInserted(uriList.size() - 1);
                productImageListRecycle.scrollToPosition(uriList.size());
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
                mainDataBean.setLastID(mainDataBean.getLastID()+1);
                productBeanWithOutImageList.setId(mainDataBean.getLastID());
                productBeanWithOutImageList.setName(productName.getText().toString());
                productBeanWithOutImageList.setDescripe(productDescripe.getText().toString());
                productBeanWithOutImageList.setOriginalStartTime(originalStartTime.getText().toString());
                productBeanWithOutImageList.setOriginalEndTime(originalEndTime.getText().toString());
                productBeanWithOutImageList.setOnerName(onerName.getText().toString());
                productBeanWithOutImageList.setOnerPhone(onerPhone.getText().toString());
                productBeanWithOutImageList.setOnerID(onerID.getText().toString());
                productBeanWithOutImageList.setOnerDescripe(onerDescripe.getText().toString());
                productBeanWithOutImageList.setPriceOriginal(Long.valueOf(TextUtils.isEmpty(originalPrice.getText().toString())?"0":originalPrice.getText().toString()));
                productBeanWithOutImageList.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
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

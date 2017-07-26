package com.dtw.fellinghouse.View.Tenant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dtw.fellinghouse.Bean.MainDataBean;
import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.QiNiuModel;
import com.dtw.fellinghouse.Presener.TenantPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.BaseActivity;
import com.dtw.fellinghouse.View.ImageRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24 0024.
 */

public class TenantActivity extends BaseActivity implements TenantView{
    private TenantPresener tenantPresener;
    private MainDataBean mainDataBean;
    private ProductBean editProductBean;
    private RecyclerView imageRecycleView;
    private TextView tenantName,tenantPhone,tenantID,tenantLength,tenantPrice;
    private RadioGroup tenantLengthType;
    private List<String> imagURLList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tenantPresener=new TenantPresener(this,this);

        tenantName= (TextView) findViewById(R.id.edit_tenant_name);
        tenantPhone= (TextView) findViewById(R.id.edit_tenant_phone);
        tenantID= (TextView) findViewById(R.id.edit_tenant_id);
        tenantLength= (TextView) findViewById(R.id.edit_tenant_length);
        tenantPrice= (TextView) findViewById(R.id.edit_tenant_price);
        tenantLengthType= (RadioGroup) findViewById(R.id.radiogroup_length_type);

        mainDataBean=getIntent().getParcelableExtra(Config.Key_Main_Product);
        editProductBean=getIntent().getParcelableExtra(Config.Key_Product);
        setTitle(editProductBean.getName());

        tenantName.setText(editProductBean.getTenantName());
        tenantPhone.setText(editProductBean.getTenantPhone());
        tenantID.setText(editProductBean.getTenantID());
        tenantLength.setText(String.valueOf(editProductBean.getTenantLength()));
        tenantPrice.setText(String.valueOf(editProductBean.getTenantPrice()));
        if(Config.TYPE_DAY.equals(editProductBean.getTenantType())){
            tenantLengthType.check(R.id.radio_day);
        }else if(Config.TYPE_WEEK.equals(editProductBean.getTenantType())){
            tenantLengthType.check(R.id.radio_week);
        }else if(Config.TYPE_MONTH.equals(editProductBean.getTenantType())){
            tenantLengthType.check(R.id.radio_month);
        }

        imageRecycleView= (RecyclerView) findViewById(R.id.recycle_product_img_list);
        imageRecycleView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        for(int i=0;i<editProductBean.getProductImgNameList().size();i++){
            imagURLList.add(QiNiuModel.getInstance().getPublicUrl(editProductBean.getProductImgNameList().get(i)));
        }
        ImageRecycleAdapter imageRecycleAdapter=new ImageRecycleAdapter(this,imagURLList);
        imageRecycleView.setAdapter(imageRecycleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                editProductBean.setTenantName(tenantName.getText().toString());
                editProductBean.setTenantPhone(tenantPhone.getText().toString());
                editProductBean.setTenantID(tenantID.getText().toString());
                editProductBean.setTenantLength(Integer.parseInt(tenantLength.getText().toString()));
                editProductBean.setTenantPrice(Long.parseLong(tenantPrice.getText().toString()));
                switch (tenantLengthType.getCheckedRadioButtonId()){
                    case R.id.radio_day:
                        editProductBean.setTenantType(Config.TYPE_DAY);
                        break;
                    case R.id.radio_week:
                        editProductBean.setTenantType(Config.TYPE_WEEK);
                        break;
                    case R.id.radio_month:
                        editProductBean.setTenantType(Config.TYPE_MONTH);
                        break;
                }
                for(int i=0;i<mainDataBean.getProductList().size();i++){
                    if(mainDataBean.getProductList().get(i).getId()==editProductBean.getId()){
                        mainDataBean.getProductList().remove(i);
                        break;
                    }
                }
                tenantPresener.insertProduct(mainDataBean,editProductBean);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}

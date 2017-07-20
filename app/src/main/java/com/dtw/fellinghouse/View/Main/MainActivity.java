package com.dtw.fellinghouse.View.Main;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Bean.ProductListBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Presener.MainPresener;
import com.dtw.fellinghouse.Presener.WXSharePresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.AddProduct.AddProductActivity;
import com.dtw.fellinghouse.View.BaseActivity;
import com.dtw.fellinghouse.View.Chart.ChartActivity;
import com.dtw.fellinghouse.View.Login.LoginActivity;
import com.dtw.fellinghouse.View.Setting.SettingActivity;
import com.dtw.fellinghouse.View.SimpleOnRecycleItemClickListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainView,NavigationView.OnNavigationItemSelectedListener ,SimpleOnRecycleItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private WXSharePresener wxSharePresener;
    private RecyclerView mainRecycle;
    private SwipeRefreshLayout mainSwipeRefresh;
    private ProductStaggeredRecycleAdapter productStaggeredRecycleAdapter;
    private List<ProductBean> productBeanList =new ArrayList<>();
    private MainPresener mainPresener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainPresener=new MainPresener(this,this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainSwipeRefresh= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_main);
        mainSwipeRefresh.setOnRefreshListener(this);
        mainSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.swipeRefreshColor0),
                getResources().getColor(R.color.swipeRefreshColor1),
                getResources().getColor(R.color.swipeRefreshColor2),
                getResources().getColor(R.color.swipeRefreshColor3));

        mainRecycle= (RecyclerView) findViewById(R.id.recycle_main);
        mainRecycle.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        productStaggeredRecycleAdapter=new ProductStaggeredRecycleAdapter(this, productBeanList);
        productStaggeredRecycleAdapter.setSimpleOnRecycleItemClickListener(this);
        mainRecycle.setAdapter(productStaggeredRecycleAdapter);
        mainPresener.getSimpleProductList(true,ProductListBean.class);

        MobclickAgent.setCheckDevice(false);
        wxSharePresener=new WXSharePresener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i= new Intent(Intent.ACTION_MAIN);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//         //Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav_login:
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                break;
            case R.id.nav_add:
                Intent add=new Intent(this, AddProductActivity.class);
                startActivity(add);
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_chart:
                Intent chart = new Intent(this, ChartActivity.class);
                startActivity(chart);
                break;
            case R.id.nav_share:
//                wxSharePresener.sendWebMsg(Config.WXSceneSession,"market://details?id=" + getPackageName());
//                wxSharePresener.sendTextMsg(Config.WXSceneSession,"hello","love");
                break;
            case R.id.nav_setting:
                Intent setting = new Intent(this, SettingActivity.class);
                startActivity(setting);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public <T> void onData(T data) {
        mainSwipeRefresh.setRefreshing(false);
        if (data instanceof ProductListBean) {
            ProductListBean productListBean = (ProductListBean) data;
            productBeanList.clear();
            productBeanList.addAll(productListBean.getProductList());
            productStaggeredRecycleAdapter.notifyDataSetChanged();
            Log.v("dtw","productMain:"+productListBean.getProductList().get(0).getName());
            Log.v("dtw","productMain:"+productListBean.getProductList().get(0).getLocationName());
            Log.v("dtw","productMain:"+productListBean.getProductList().get(0).getDescripe());
            Log.v("dtw","productMain:"+productListBean.getProductList().get(0).getProductImgNameList().get(0));
        }
    }

    @Override
    public void endRefreshing(){
        mainSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mainPresener.getSimpleProductList(false,ProductListBean.class);
    }

    @Override
    public void onRecycleItemClick(String adapterClassName, View v, int position) {
        if (adapterClassName.equals(ProductStaggeredRecycleAdapter.class.getName())) {
            Log.v("dtw","click position:"+position);
            ProductBrowserDialog productBrowserDialog=new ProductBrowserDialog(productBeanList.get(position),this,R.style.dialog);
            productBrowserDialog.show();
        }
    }

    @Override
    public void onRecycleItemLongClick(String adapterClassName, View v, int position) {
        if(ProductStaggeredRecycleAdapter.class.getName().equals(adapterClassName)){

        }
    }
}

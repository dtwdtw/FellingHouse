package com.dtw.fellinghouse.View.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.dtw.fellinghouse.Bean.ProductBean;
import com.dtw.fellinghouse.Bean.MainDataBean;
import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Presener.LoginPresener;
import com.dtw.fellinghouse.Presener.MainPresener;
import com.dtw.fellinghouse.Presener.WXSharePresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.Utils.SPUtil;
import com.dtw.fellinghouse.View.AddProduct.AddProductActivity;
import com.dtw.fellinghouse.View.BaseActivity;
import com.dtw.fellinghouse.View.Chart.ChartActivity;
import com.dtw.fellinghouse.View.EditProduct.EditProductActivity;
import com.dtw.fellinghouse.View.Login.LoginActivity;
import com.dtw.fellinghouse.View.Login.LoginView;
import com.dtw.fellinghouse.View.Setting.SettingActivity;
import com.dtw.fellinghouse.View.SimpleOnRecycleItemClickListener;
import com.dtw.fellinghouse.View.Tenant.TenantActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import cn.jpush.im.android.api.model.UserInfo;

public class MainActivity extends BaseActivity implements MainView, NavigationView.OnNavigationItemSelectedListener, SimpleOnRecycleItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private WXSharePresener wxSharePresener;
    private RecyclerView mainRecycle;
    private SwipeRefreshLayout mainSwipeRefresh;
    private MenuItem menuAdd;
    private MenuItem menuLogin;
    private MenuItem menuLogout;
    private ProductStaggeredRecycleAdapter productStaggeredRecycleAdapter;
    private List<ProductBean> productBeanList = new ArrayList<>();
    private MainDataBean mainDataBean;
    private MainPresener mainPresener;
    private boolean isAdmin = false;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainPresener = new MainPresener(this, this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menuAdd = navigationView.getMenu().findItem(R.id.nav_add);
        menuLogin = navigationView.getMenu().findItem(R.id.nav_login);
        menuLogout=navigationView.getMenu().findItem(R.id.nav_logout);
        menuAdd.setVisible(false);

        mainSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_main);
        mainSwipeRefresh.setOnRefreshListener(this);
        mainSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.swipeRefreshColor0),
                getResources().getColor(R.color.swipeRefreshColor1),
                getResources().getColor(R.color.swipeRefreshColor2),
                getResources().getColor(R.color.swipeRefreshColor3));

        mainRecycle = (RecyclerView) findViewById(R.id.recycle_main);
        mainRecycle.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        productStaggeredRecycleAdapter = new ProductStaggeredRecycleAdapter(this, productBeanList);
        productStaggeredRecycleAdapter.setSimpleOnRecycleItemClickListener(this);
        mainRecycle.setAdapter(productStaggeredRecycleAdapter);
        mainPresener.getSimpleProductList(true, MainDataBean.class);

        mainPresener.login(new SPUtil(this).getUserName(),new SPUtil(this).getUserPassword());
        MobclickAgent.setCheckDevice(false);
        wxSharePresener = new WXSharePresener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainSwipeRefresh.setRefreshing(true);
        mainPresener.getSimpleProductList(false, MainDataBean.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(Intent.ACTION_MAIN);
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
        switch (item.getItemId()) {
            case R.id.nav_login:
                Intent login = new Intent(this, LoginActivity.class);
                startActivityForResult(login, Config.Request_Code_Login);
                break;
            case R.id.nav_add:
                Intent add = new Intent(this, AddProductActivity.class);
                add.putExtra(Config.Key_Main_Product, mainDataBean);
                startActivity(add);
                break;
            case R.id.nav_logout:
                mainPresener.logout();
                menuAdd.setVisible(false);
                menuLogin.setVisible(true);
                menuLogout.setVisible(false);
                isLogin=false;
                isAdmin=false;
                break;
            case R.id.nav_chart:
                if (isLogin) {
                    Intent chart = new Intent(this, ChartActivity.class);
                    chart.putExtra(Config.Key_Is_Admin, isAdmin);
                    startActivity(chart);
                } else {
                    showToast("请先登录");
                }
                break;
            case R.id.nav_share:
                wxSharePresener.sendWebMsg(Config.WXSceneSession, Config.Share_Link);
//                wxSharePresener.sendTextMsg(Config.WXSceneSession, "资克", "FellingHouse");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.Request_Code_Login:
                if (resultCode == RESULT_OK) {
                    isLogin = true;
                    menuLogin.setVisible(false);
                    menuLogout.setVisible(true);
                    isAdmin = data.getBooleanExtra(Config.Key_Admin, false);
                    if (isAdmin) {
                        menuAdd.setVisible(true);
                    }
                }
                break;
        }
    }

    @Override
    public <T> void onData(T data) {
        mainSwipeRefresh.setRefreshing(false);
        if (data instanceof MainDataBean) {
            mainDataBean = (MainDataBean) data;
            productBeanList.clear();
            productBeanList.addAll(mainDataBean.getProductList());
            productStaggeredRecycleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void endRefreshing() {
        mainSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mainPresener.getSimpleProductList(false, MainDataBean.class);
    }

    @Override
    public void onRecycleItemClick(String adapterClassName, View v, int position) {
        if (adapterClassName.equals(ProductStaggeredRecycleAdapter.class.getName())) {
            Log.v("dtw", "click position:" + position);
            ProductBrowserDialog productBrowserDialog = new ProductBrowserDialog(productBeanList.get(position), this, isLogin,isAdmin, R.style.dialog);
            productBrowserDialog.show();
        }
    }

    @Override
    public void onRecycleItemLongClick(String adapterClassName, View v, int position) {
        if (ProductStaggeredRecycleAdapter.class.getName().equals(adapterClassName)) {
            if (isAdmin) {
                final ProductBean productBean = productBeanList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(productBean.getName());
                builder.setMessage("描述：" + productBean.getDescripe() + "\r\n位置：" + productBean.getLocationName());
                builder.setNegativeButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent edit = new Intent(MainActivity.this, EditProductActivity.class);
                        edit.putExtra(Config.Key_Main_Product, mainDataBean);
                        edit.putExtra(Config.Key_Product, productBean);
                        startActivity(edit);
                    }
                });
                builder.setNeutralButton("租客", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent tenant = new Intent(MainActivity.this, TenantActivity.class);
                        tenant.putExtra(Config.Key_Main_Product, mainDataBean);
                        tenant.putExtra(Config.Key_Product, productBean);
                        startActivity(tenant);
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();
            }
        }
    }
}

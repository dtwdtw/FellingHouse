package com.dtw.fellinghouse.View.Login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.LogPrinter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dtw.fellinghouse.Presener.LoginPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.BaseActivity;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class LoginActivity extends BaseActivity implements LoginView{
    private EditText phoneNum;
    private EditText password;
    private Button loginBtn;

    private LoginPresener loginPresener=new LoginPresener(this,this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNum= (EditText) findViewById(R.id.edit_phone_num);
        password= (EditText) findViewById(R.id.edit_password);
        loginBtn= (Button) findViewById(R.id.btn_login);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                loginPresener.login(phoneNum.getText().toString(),password.getText().toString());
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFail(int code, String msg) {

    }

    @Override
    public void onSuccess(int code, String msg) {

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}

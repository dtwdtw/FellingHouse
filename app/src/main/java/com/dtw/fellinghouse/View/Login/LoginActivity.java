package com.dtw.fellinghouse.View.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Presener.LoginPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.Utils.SPUtil;
import com.dtw.fellinghouse.View.SmsCodeLogin.SmsCodeLoginActivity;
import com.dtw.fellinghouse.View.BaseActivity;
import com.dtw.fellinghouse.View.Regist.RegistActivity;

import java.security.MessageDigest;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class LoginActivity extends BaseActivity implements LoginView{
    private EditText phoneNum;
    private EditText password;
    private Button loginBtn;

    private LoginPresener loginPresener;
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

        phoneNum.setText(new SPUtil(this).getUserName());
        password.setText(new SPUtil(this).getUserPassword());

        loginPresener=new LoginPresener(this,this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                loginPresener.login(phoneNum.getText().toString(),password.getText().toString());
                break;
            case R.id.text_regist:
                Intent regist=new Intent(this, RegistActivity.class);
                regist.putExtra(Config.Key_PhnoeNum,phoneNum.getText().toString());
                startActivityForResult(regist,Config.Request_Code_Regist);
                break;
            case R.id.btn_smscode_login:
                Intent smscodeLogin=new Intent(this, SmsCodeLoginActivity.class);
                smscodeLogin.putExtra(Config.Key_PhnoeNum,phoneNum.getText().toString());
                startActivityForResult(smscodeLogin,Config.Request_Code_SmsCodeLogin);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case Config.Request_Code_Regist:
                if(resultCode==RESULT_OK){
                    Log.v("Dtw","login");
                    String phone=data.getStringExtra(Config.Key_PhnoeNum);
                    String pwd=data.getStringExtra(Config.Key_Password);
                    phoneNum.setText(phone);
                    password.setText(pwd);
                    showToast("正在自动登录...");
                    loginPresener.login(phone,pwd);
                }
                break;
            case Config.Request_Code_SmsCodeLogin:
                if(resultCode==RESULT_OK){

                }
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
    protected void onDestroy() {
        super.onDestroy();
        loginPresener.onViewDestory();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goBack(UserInfo userInfo) {
        new SPUtil(this).setUserName(phoneNum.getText().toString());
        new SPUtil(this).setUserPassword(password.getText().toString());

        Intent intent = new Intent();
        if(Config.Key_Admin.equals(userInfo.getSignature())) {
            intent.putExtra(Config.Key_Admin,true);
        }else{
            intent.putExtra(Config.Key_Admin,false);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}

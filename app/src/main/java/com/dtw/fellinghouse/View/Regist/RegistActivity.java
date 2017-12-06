package com.dtw.fellinghouse.View.Regist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Presener.RegistPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.View.BaseActivity;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class RegistActivity extends BaseActivity implements RegistView {
    private RegistPresener registPresener;
    private Button getCodeBtn;
    private Button registBtn;
    private EditText phoneNum, code, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getCodeBtn = (Button) findViewById(R.id.btn_code);
        registBtn = (Button) findViewById(R.id.btn_regist);
        phoneNum = (EditText) findViewById(R.id.edit_phone_num);
        code = (EditText) findViewById(R.id.edit_code);
        password = (EditText) findViewById(R.id.edit_password);
        phoneNum.setText(getIntent().getStringExtra(Config.Key_PhnoeNum));

        registPresener = new RegistPresener(this, this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code:
                registPresener.getCode(phoneNum.getText().toString());
                break;
            case R.id.text_voice_verify:
                registPresener.getVoiceCode(phoneNum.getText().toString());
                break;
            case R.id.btn_regist:
                if (password.getText().toString().length() > 5) {
                    registPresener.verifyCode(phoneNum.getText().toString(), password.getText().toString(), code.getText().toString());
                }else{
                    showToast("请输入大于六位字符的密码");
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registPresener.onViewDetory();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goBackWidthData() {
        Intent intent = new Intent();
        intent.putExtra(Config.Key_PhnoeNum, phoneNum.getText().toString());
        intent.putExtra(Config.Key_Password, password.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setCodeBtnText(String text) {
        getCodeBtn.setText(text);
    }

    @Override
    public void setEnableCodeBtn(boolean enable) {
        getCodeBtn.setEnabled(enable);
    }

    @Override
    public void setEnableRegistBtn(boolean enable) {
        registBtn.setEnabled(enable);
    }

}
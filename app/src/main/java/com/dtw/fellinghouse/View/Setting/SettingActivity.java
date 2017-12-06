package com.dtw.fellinghouse.View.Setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.Model.JMessageModel;
import com.dtw.fellinghouse.Presener.SettingPresener;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.Utils.ScreenUtil;
import com.dtw.fellinghouse.View.BaseActivity;

import org.w3c.dom.Attr;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class SettingActivity extends BaseActivity implements SettingView, CompoundButton.OnCheckedChangeListener {
    private SettingPresener settingPresener;
    private Switch switchNotifyDisenable, switchNotifySilence;
    private TextView setUserAdminText;
    private boolean isAdmin;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isAdmin = getIntent().getBooleanExtra(Config.Key_Is_Admin, false);

        settingPresener = new SettingPresener(this, this);

        switchNotifyDisenable = (Switch) findViewById(R.id.switch_notify_disenable);
        switchNotifySilence = (Switch) findViewById(R.id.switch_notify_silence);
        setUserAdminText = (TextView) findViewById(R.id.text_setuser_admin);

        switchNotifyDisenable.setOnCheckedChangeListener(this);
        switchNotifySilence.setOnCheckedChangeListener(this);

        switch (settingPresener.getNotifyFlag()) {
            case Config.NotifyDisEnable:
                switchNotifyDisenable.setEnabled(false);
                break;
            case Config.NotifySilence:
                switchNotifySilence.setEnabled(false);
                break;
        }

        if (isAdmin) {
            setUserAdminText.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_setuser_admin:
                final View view = LayoutInflater.from(this).inflate(R.layout.edittext_dialog, null, false);
                final EditText editText = (EditText) view.findViewById(R.id.edittext);
                editText.setHint(R.string.admin_phone);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.input_admin_phone)
                        .setView(view)
                        .setNegativeButton(R.string.cancle, null)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                settingPresener.setUserAdmin(editText.getText().toString());
                            }
                        }).show();
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_notify_silence:
                if (isChecked) {
                    settingPresener.setNotifyFlag(Config.NotifyDefault);
                    switchNotifySilence.setText(R.string.notify_sound_play);
                } else {
                    settingPresener.setNotifyFlag(Config.NotifySilence);
                    switchNotifySilence.setText(R.string.notify_sound_no_play);
                }
                break;
            case R.id.switch_notify_disenable:
                if (isChecked) {
                    settingPresener.setNotifyFlag(Config.NotifyDefault);
                    switchNotifySilence.setEnabled(true);
                    switchNotifyDisenable.setText(R.string.notify_message_pop);
                } else {
                    settingPresener.setNotifyFlag(Config.NotifyDisEnable);
                    switchNotifySilence.setEnabled(false);
                    switchNotifyDisenable.setText(R.string.notify_message_no_pop);
                }
                break;
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

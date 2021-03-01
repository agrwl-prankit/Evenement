package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import io.realm.Realm;
import io.realm.mongodb.User;

public class SettingActivity extends AppCompatActivity {

    AppInfo appInfo;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.settingToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");

        Realm.init(this);
        appInfo = new AppInfo();
        user = appInfo.getApp().currentUser();
    }
}
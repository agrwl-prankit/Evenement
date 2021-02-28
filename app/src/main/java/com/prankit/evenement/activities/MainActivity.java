package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.prankit.evenement.R;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainActivity extends AppCompatActivity {

    String APP_ID = "evenement-msvpz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        App app = new App(new AppConfiguration.Builder(APP_ID).build());

    }
}
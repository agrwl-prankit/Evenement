package com.prankit.evenement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.prankit.evenement.R;
import com.prankit.evenement.TabAdapter;
import com.prankit.evenement.Utils.AppInfo;

import io.realm.Realm;
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {

    AppInfo appInfo;
    User user;
    private static final int INTERNET_PERMISSION_CODE = 100;
    private static final int CALL_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.TabLayout);
        ViewPager viewPager = findViewById(R.id.ViewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("My Events"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final TabAdapter adapter = new TabAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        Realm.init(this);
        appInfo = new AppInfo();
        user = appInfo.getApp().currentUser();

        ImageView logOut = findViewById(R.id.logOut);
        ImageView gotoProfile = findViewById(R.id.gotoProfile);
        FloatingActionButton fab = findViewById(R.id.addEventFab);

        fab.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddEventActivity.class));
            finish();
        });
        gotoProfile.setOnClickListener(view -> {
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        });
        logOut.setOnClickListener(view -> logout());
    }

    @Override
    protected void onStart() {
        super.onStart();
        internetPermission();
        callPermission();
    }

    void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Log Out");
        dialog.setMessage("Do you want to Log out?");
        dialog.setPositiveButton("Yes", (dialogInterface, d) -> {
            ProgressDialog loadingBar = new ProgressDialog(this);
            loadingBar.setMessage("Signing out..");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            user.logOutAsync(result -> {
                if (result.isSuccess()) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    Toast.makeText(this, "Sign out successfully", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Error in signing out")
                            .setMessage(result.getError().getErrorMessage())
                            .setPositiveButton("Ok", null)
                            .show();
                }
                loadingBar.dismiss();
            });
        })
                .setNegativeButton("No", (dialogInterface, d) -> { });
        dialog.create();
        dialog.show();
    }

    public void internetPermission(){
        checkPermission(Manifest.permission.INTERNET, INTERNET_PERMISSION_CODE);
    }

    public void callPermission(){
        checkPermission(Manifest.permission.CALL_PHONE, CALL_PERMISSION_CODE);
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INTERNET_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "Allow the internet permission from setting", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
        if (requestCode == CALL_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "Allow the call permission from setting", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }
}
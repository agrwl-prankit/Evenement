package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.TabLayout);
        ViewPager viewPager = findViewById(R.id.ViewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("My Events"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final TabAdapter adapter = new TabAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
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


    void logout(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Log Out");
        dialog.setMessage("Do you want to Log out?");
        dialog.setPositiveButton("Yes", (dialogInterface, d) -> {
            user.logOutAsync(result -> {
                if(result.isSuccess()){
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
            });
        })
                .setNegativeButton("No", (dialogInterface, d) -> {
                });
        dialog.create();
        dialog.show();
    }
}
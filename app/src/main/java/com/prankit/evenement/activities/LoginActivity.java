package com.prankit.evenement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import io.realm.Realm;
import io.realm.mongodb.Credentials;

public class LoginActivity extends AppCompatActivity {

    AppInfo appInfo;
    private ProgressDialog loadingBar;
    private static final int INTERNET_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        internetPermission();

        Realm.init(this);
        appInfo = new AppInfo();
        loadingBar = new ProgressDialog(this);

        TextInputEditText email = findViewById(R.id.inputLoginEmail);
        TextInputEditText password = findViewById(R.id.inputLoginPassword);
        Button loginButton = findViewById(R.id.loginButton);
        TextView signUp = findViewById(R.id.signUpButton);
        TextView forgetPassword = findViewById(R.id.forgetPassword);

        forgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(this, SendResetLinkActivity.class));
        });
        signUp.setOnClickListener(view -> startActivity(new Intent(this, SignUpActivity.class)));
        loginButton.setOnClickListener(view -> login(email.getText().toString(), password.getText().toString()));
    }

    private void login(String mail, String pass) {
        if (mail.equals("") || pass.equals("")){
            Toast.makeText(this, "Enter all credentials", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingBar.setTitle("LogIn");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        Credentials credentials = Credentials.emailPassword(mail, pass);
        appInfo.getApp().loginAsync(credentials, result -> {
            if (result.isSuccess()){
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                new AlertDialog.Builder(LoginActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Error in login")
                    .setMessage(result.getError().getErrorMessage())
                    .setPositiveButton("Ok", null)
                    .show();
            }
            loadingBar.dismiss();
        });
    }

    public void internetPermission(){
        checkPermission(Manifest.permission.INTERNET, INTERNET_PERMISSION_CODE);
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
    }
}
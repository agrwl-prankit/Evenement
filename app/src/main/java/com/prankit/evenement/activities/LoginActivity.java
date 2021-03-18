package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class LoginActivity extends AppCompatActivity {

    AppInfo appInfo;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Realm.init(this);
        appInfo = new AppInfo();
        loadingBar = new ProgressDialog(this);

        TextInputEditText email = findViewById(R.id.inputLoginEmail);
        TextInputEditText password = findViewById(R.id.inputLoginPassword);
        Button loginButton = findViewById(R.id.loginButton);
        TextView signUp = findViewById(R.id.signUpButton);
        TextView forgetPassword = findViewById(R.id.forgetPassword);

        //forgetPassword.setOnClickListener(view -> {});
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
}
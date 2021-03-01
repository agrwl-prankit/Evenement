package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;

public class SignUpActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;
    AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Realm.init(this);
        appInfo = new AppInfo();
        loadingBar = new ProgressDialog(this);

        TextInputEditText email = findViewById(R.id.inputSignInEmail);
        TextInputEditText password = findViewById(R.id.inputSignInPassword);
        Button signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(view -> signIn(email.getText().toString(), password.getText().toString()));

    }

    private void signIn(String mail, String pass) {
        if (mail.equals("") || pass.equals("")){
            Toast.makeText(this, "Enter all credentials", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingBar.setTitle("SignIn");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        Credentials credentials = Credentials.emailPassword(mail, pass);
        appInfo.getApp().getEmailPassword().registerUserAsync(mail, pass, result -> {
            if (result.isSuccess()){
                appInfo.getApp().loginAsync(credentials, result1 -> {
                    Toast.makeText(SignUpActivity.this, "SignIn Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                });
            } else {
                new AlertDialog.Builder(SignUpActivity.this)
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
package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import io.realm.Realm;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText password, confirmPassword;
    private Button resetButton;
    private Intent intent;
    private String data1, data2;
    private Uri data;
    private AppInfo appInfo;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        intent = getIntent();
        data = intent.getData();
        data1 = data.getQueryParameter("token");
        data2 = data.getQueryParameter("tokenId");

        Realm.init(this);
        appInfo = new AppInfo();
        loadingBar = new ProgressDialog(this);

        password = findViewById(R.id.inputResetPassword);
        confirmPassword = findViewById(R.id.inputConfirmPassword);
        resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener(v -> resetPassword() );
    }

    private void resetPassword() {
        if (password.getText().toString().equals("")){
            password.setError("Required");
            return;
        }
        if (confirmPassword.getText().toString().equals("")){
            confirmPassword.setError("Required");
            return;
        }
        if (!password.getText().toString().equals(confirmPassword.getText().toString())){
            confirmPassword.setError("Password not match");
            return;
        }
        loadingBar.setTitle("Resetting Password");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        appInfo.getApp().getEmailPassword().resetPasswordAsync(data1, data2, password.getText().toString(), result -> {
            if (result.isSuccess()){
                Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                new AlertDialog.Builder(ResetPasswordActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Error in resetting password")
                        .setMessage(result.getError().getErrorMessage())
                        .setPositiveButton("Ok", null)
                        .show();
            }
            loadingBar.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
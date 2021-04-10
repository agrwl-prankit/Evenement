package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import io.realm.Realm;

public class ResetPasswordActivity extends AppCompatActivity {

    private AppInfo appInfo;
    private ProgressDialog loadingBar;
    private TextInputEditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Realm.init(this);
        appInfo = new AppInfo();
        loadingBar = new ProgressDialog(this);

        email = findViewById(R.id.inputResetEmail);
        Button reset = findViewById(R.id.resetButton);

        reset.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        if (email.getText().toString().equals("")) email.setError("Required");
        else {
            loadingBar.setTitle("Sending reset password link");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            appInfo.getApp().getEmailPassword().sendResetPasswordEmailAsync(email.getText().toString(), result -> {
                if (result.isSuccess()){
                    email.setText("");
                    Toast.makeText(this, "link send successfully", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Error in sending resetting link")
                            .setMessage(result.getError().getErrorMessage())
                            .setPositiveButton("Ok", null)
                            .show();
                }
                loadingBar.dismiss();
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
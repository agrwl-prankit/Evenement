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

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class SignUpActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;
    AppInfo appInfo;
    User user;
    private MongoDatabase db;
    private MongoClient client;
    private MongoCollection<Document> collection;
    private TextInputEditText inputName, inputNumber, inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Realm.init(this);
        appInfo = new AppInfo();
        loadingBar = new ProgressDialog(this);

        inputEmail = findViewById(R.id.inputSignInEmail);
        inputPassword = findViewById(R.id.inputSignInPassword);
        inputName = findViewById(R.id.inputSignInName);
        inputNumber = findViewById(R.id.inputSignInNumber);
        Button signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(view ->
                signIn(inputName.getText().toString(), inputNumber.getText().toString(), inputEmail.getText().toString(), inputPassword.getText().toString()));

    }

    private void signIn(String name, String number, String mail, String pass) {
        if (mail.equals("") || pass.equals("") || name.equals("") || number.equals("")) {
            Toast.makeText(this, "Enter all credentials", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingBar.setTitle("SignIn");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        Credentials credentials = Credentials.emailPassword(mail, pass);
        appInfo.getApp().getEmailPassword().registerUserAsync(mail, pass, result -> {
            if (result.isSuccess()) {
                appInfo.getApp().loginAsync(credentials, result1 -> {
                    addProfile(result1.get().getId());
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                    Toast.makeText(SignUpActivity.this, "SignIn Successful", Toast.LENGTH_SHORT).show();
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

    private void addProfile(String id) {

        user = appInfo.getApp().currentUser();
        if (user != null) {
            client = user.getMongoClient("mongodb-atlas");
        }
        db = client.getDatabase("Event");
        collection = db.getCollection("User_Info");

        loadingBar.setTitle("Adding");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        collection.insertOne(new Document("userId", id).append("name", inputName.getText().toString())
                .append("number", inputNumber.getText().toString()).append("email", inputEmail.getText().toString())
                .append("password", inputPassword.getText().toString()))
                .getAsync(result1 -> {
                    if (!result1.isSuccess()) {
                        new AlertDialog.Builder(SignUpActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Error in adding profile")
                                .setMessage(result1.getError().getErrorMessage())
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                });
        loadingBar.dismiss();
    }
}
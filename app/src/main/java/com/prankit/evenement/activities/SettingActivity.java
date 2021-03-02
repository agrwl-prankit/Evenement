package com.prankit.evenement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class SettingActivity extends AppCompatActivity {

    AppInfo appInfo;
    User user;
    private TextInputEditText inputName, inputNumber, inputEmail;
    private String updateName = null, updateNumber = null, updateEmail = null;
    private ProgressDialog loadingBar;
    private Button addButton;
    private MongoDatabase db;
    private MongoClient client;
    private MongoCollection<Document> collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.settingToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");

        loadingBar = new ProgressDialog(this);
        Realm.init(this);
        appInfo = new AppInfo();
        user = appInfo.getApp().currentUser();
        client = user.getMongoClient("mongodb-atlas");
        db = client.getDatabase("Event");
        collection = db.getCollection("User_Info");
        retrieveInfo();

        //CircleImageView profileImage = findViewById(R.id.inputProfileImage);
        inputName = findViewById(R.id.inputProfileName);
        inputNumber = findViewById(R.id.inputProfileNumber);
        inputEmail = findViewById(R.id.inputProfileEmail);
        addButton = findViewById(R.id.addProfileButton);

        addButton.setOnClickListener(view -> addProfile());
    }

    public  void  retrieveInfo(){
        loadingBar.setTitle("Checking Profile");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        Document findQuery = new Document().append("userId", user.getId());
        collection.findOne(findQuery).getAsync(result -> {
            if (result.isSuccess() && !result.get().equals("")){
                updateName = result.get().getString("name");
                updateEmail = result.get().getString("email");
                updateNumber = result.get().getString("number");
                inputName.setText(updateName);
                inputEmail.setText(updateEmail);
                inputNumber.setText(updateNumber);
                addButton.setText("Update Profile");
            }
        });
        loadingBar.dismiss();
    }

    private void addProfile() {
        Document findQuery = new Document().append("userId", user.getId());
        collection.findOne(findQuery).getAsync(result -> {
            if (result.isSuccess() && !result.get().equals("")){
                updateInfo();
                return;
            }
        });

        if (inputName.getText().toString().equals("") || inputNumber.getText().toString().equals("") ||
            inputEmail.getText().toString().equals("")){
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingBar.setTitle("Adding");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        collection.insertOne(new Document("userId", user.getId()).append("name", inputName.getText().toString())
        .append("number", inputNumber.getText().toString()).append("email", inputEmail.getText().toString()))
                .getAsync(result -> {
            if (result.isSuccess()){
                Toast.makeText(SettingActivity.this, "Profile added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                new AlertDialog.Builder(SettingActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Error in adding profile")
                        .setMessage(result.getError().getErrorMessage())
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });
        loadingBar.dismiss();
    }

    private void updateInfo() {
        loadingBar.setTitle("Updating profile");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        loadingBar.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.closeOption){
            finish();
        }
        return true;
    }
}
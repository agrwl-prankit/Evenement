package com.prankit.evenement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.result.UpdateResult;

public class SettingActivity extends AppCompatActivity {

    AppInfo appInfo;
    User user;
    private TextInputEditText inputName, inputNumber, inputEmail;
    private String updateName = null, updateNumber = null, updateEmail = null;
    private ProgressDialog loadingBar;
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
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase db = client.getDatabase("Event");
        collection = db.getCollection("User_Info");
        retrieveInfo();

        //CircleImageView profileImage = findViewById(R.id.inputProfileImage);
        inputName = findViewById(R.id.inputProfileName);
        inputNumber = findViewById(R.id.inputProfileNumber);
        inputEmail = findViewById(R.id.inputProfileEmail);
        Button addButton = findViewById(R.id.addProfileButton);

        addButton.setOnClickListener(view -> updateInfo());
    }

    public  void  retrieveInfo(){
        loadingBar.setTitle("Checking Profile");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        Document findQuery = new Document("userId", user.getId());
        collection.findOne(findQuery).getAsync((App.Result<Document> result) -> {
            if (result.isSuccess() && !result.get().getString("name").equals("")){
                updateName = result.get().getString("name");
                updateEmail = result.get().getString("email");
                updateNumber = result.get().getString("number");
                inputName.setText(updateName);
                inputEmail.setText(updateEmail);
                inputNumber.setText(updateNumber);
            }
        });
        loadingBar.dismiss();
    }

    private void updateInfo() {
        loadingBar.setTitle("Updating profile");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        Document findQuery = new Document().append("userId", user.getId());
        collection.updateOne(findQuery, new Document("userId", user.getId()).append("name", inputName.getText().toString())
                .append("number", inputNumber.getText().toString())).getAsync(result -> {
                    if (result.isSuccess()){
                        long count = result.get().getModifiedCount();
                        if (count == 1) {
                            Toast.makeText(SettingActivity.this, "Update profile successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        new AlertDialog.Builder(SettingActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Error in updating profile")
                                .setMessage(result.getError().getErrorMessage())
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                });
        loadingBar.dismiss();
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
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
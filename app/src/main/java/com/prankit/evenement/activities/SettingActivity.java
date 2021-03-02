package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;
import com.prankit.evenement.models.UserInfo;

import org.bson.Document;
import org.bson.types.ObjectId;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.result.InsertOneResult;

public class SettingActivity extends AppCompatActivity {

    AppInfo appInfo;
    User user;
    private TextInputEditText name, number, email;
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

        Realm.init(this);
        appInfo = new AppInfo();
        user = appInfo.getApp().currentUser();
        client = user.getMongoClient("mongodb-atlas");
        db = client.getDatabase("Event");
        collection = db.getCollection("User_Info");

        //CircleImageView profileImage = findViewById(R.id.inputProfileImage);
        name = findViewById(R.id.inputProfileName);
        number = findViewById(R.id.inputProfileNumber);
        email = findViewById(R.id.inputProfileEmail);
        Button addButton = findViewById(R.id.addProfileButton);

        addButton.setOnClickListener(view -> addProfile());
    }

    private void addProfile() {
        if (name.getText().toString().equals("") || number.getText().toString().equals("") ||
            email.getText().toString().equals("")){
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            return;
        }
        collection.insertOne(new Document("userId", user.getId()).append("name", name.getText().toString())
        .append("number", number.getText().toString()).append("email", email.getText().toString()))
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
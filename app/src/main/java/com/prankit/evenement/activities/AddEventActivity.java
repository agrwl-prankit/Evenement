package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class AddEventActivity extends AppCompatActivity {

    private String fromIntent = "No", eventId;
    private ProgressDialog loadingBar;
    Calendar myCalendar;
    private int mDay, mMonth, mYear;
    private TextInputEditText createrName, eventName, start, end, fee, email, number;
    AppInfo appInfo;
    private String userId, selectDate = "";
    private MongoCollection<Document> postCollection;
    private TextView createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initializeField();
        initializeDb();
        getFromIntent();

        myCalendar = Calendar.getInstance();
        ImageView closeEvent = findViewById(R.id.closeCreateEvent);

        closeEvent.setOnClickListener(view -> backToMainActivity());
        createBtn.setOnClickListener(view -> {
            if (fromIntent.equals("No")) createEvent();
            else updateEvent();
        });
        start.setOnClickListener(view -> {
            selectDate = "Start";
            showDatePicker();
        });
        end.setOnClickListener(view -> {
            selectDate = "End";
            showDatePicker();
        });
    }

    private void updateEvent() {
        loadingBar.setTitle("Updating Event");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        Document findQuery = new Document().append("userId", userId);
        postCollection.updateOne(findQuery, new Document("userId", userId).append("createrName", createrName.getText().toString())
                .append("eventName", eventName.getText().toString()).append("startDate", start.getText().toString())
                .append("endDate", end.getText().toString()).append("fee", fee.getText().toString())
                .append("email", email.getText().toString()).append("number", number.getText().toString())
                .append("type", "all")).getAsync(result -> {
            if (result.isSuccess()) {
                backToMainActivity();
                Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(AddEventActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Error in updating event")
                        .setMessage(result.getError().getErrorMessage())
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });
        loadingBar.dismiss();
    }

    private void getFromIntent() {
        fromIntent = getIntent().getStringExtra("FromIntent");
        if (fromIntent.equals("yes")){
            createrName.setText(getIntent().getStringExtra("uname"));
            eventName.setText(getIntent().getStringExtra("ename"));
            start.setText(getIntent().getStringExtra("sd"));
            end.setText(getIntent().getStringExtra("ed"));
            fee.setText(getIntent().getStringExtra("fee"));
            email.setText(getIntent().getStringExtra("mail"));
            number.setText(getIntent().getStringExtra("num"));
            eventId = getIntent().getStringExtra("eid");
            createBtn.setText("Update Event");
        }
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        this.mYear = c.get(Calendar.YEAR);
        this.mMonth = c.get(Calendar.MONTH);
        this.mDay = c.get(Calendar.DAY_OF_MONTH);
        showDialog(111);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 111) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, mYear, mMonth, mDay);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0);
            Date newDate = calendar.getTime();
            datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));
            return datePickerDialog;
        }
        return null;
    }

    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (selectDate.equals("Start"))
                start.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            else if (selectDate.equals("End"))
                end.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };

    private void createEvent() {
        if (eventName.getText().toString().equals("") || start.getText().toString().equals("") || end.getText().toString().equals("")
                || fee.getText().toString().equals("") || email.getText().toString().equals("") || number.getText().toString().equals("")
                || createrName.getText().toString().equals("")) {
            if (eventName.getText().toString().equals("")) eventName.setError("Required Field");
            if (start.getText().toString().equals("")) start.setError("Required Field");
            if (end.getText().toString().equals("")) end.setError("Required Field");
            if (fee.getText().toString().equals("")) fee.setError("Required Field");
            if (email.getText().toString().equals("")) email.setError("Required Field");
            if (number.getText().toString().equals("")) number.setError("Required Field");
            if (createrName.getText().toString().equals("")) createrName.setError("Required Field");
        } else {
            loadingBar.setTitle("Creating event");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            postCollection.insertOne(new Document("userId", userId).append("createrName", createrName.getText().toString())
                    .append("eventName", eventName.getText().toString()).append("startDate", start.getText().toString())
                    .append("endDate", end.getText().toString()).append("fee", fee.getText().toString())
                    .append("email", email.getText().toString()).append("number", number.getText().toString())
                    .append("type", "all")).getAsync(result -> {
                if (result.isSuccess()) {
                    backToMainActivity();
                    Toast.makeText(this, "Event create successfully", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(AddEventActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Error in creating event")
                            .setMessage(result.getError().getErrorMessage())
                            .setPositiveButton("Ok", null)
                            .show();
                }
            });
            loadingBar.dismiss();
        }
    }

    private void initializeField() {
        createrName = findViewById(R.id.inputEventCreateName);
        eventName = findViewById(R.id.inputEventName);
        start = findViewById(R.id.inputEventSDate);
        end = findViewById(R.id.inputEventEDate);
        fee = findViewById(R.id.inputEventFee);
        email = findViewById(R.id.inputEventEmail);
        number = findViewById(R.id.inputEventNumber);
        createBtn = findViewById(R.id.createEventBtn);
        loadingBar = new ProgressDialog(this);
    }

    private void initializeDb() {
        Realm.init(this);
        appInfo = new AppInfo();
        User user = appInfo.getApp().currentUser();
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase db = client.getDatabase("Event");
        postCollection = db.getCollection("Post_Info");
        userId = user.getId();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMainActivity();
    }

    private void backToMainActivity() {
        startActivity(new Intent(AddEventActivity.this, MainActivity.class));
        finish();
    }
}
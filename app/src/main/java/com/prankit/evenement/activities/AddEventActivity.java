package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.prankit.evenement.R;

public class AddEventActivity extends AppCompatActivity {

    private TextInputEditText name, start, end, fee, email, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initializeField();

        TextView createBtn = findViewById(R.id.createEventBtn);
        ImageView closeEvent = findViewById(R.id.closeCreateEvent);

        closeEvent.setOnClickListener(view -> finish());
        createBtn.setOnClickListener(view -> createEvent());
    }

    private void createEvent() {
        if (name.getText().toString().equals("") || start.getText().toString().equals("") || end.getText().toString().equals("")
        || fee.getText().toString().equals("") || email.getText().toString().equals("") || number.getText().toString().equals("")) {
            if (name.getText().toString().equals("")) {
                name.setError("Required Field");
            }
            if (start.getText().toString().equals("")) {
                start.setError("Required Field");
            }
            if (end.getText().toString().equals("")) {
                end.setError("Required Field");
            }
            if (fee.getText().toString().equals("")) {
                fee.setError("Required Field");
            }
            if (email.getText().toString().equals("")) {
                email.setError("Required Field");
            }
            if (number.getText().toString().equals("")) {
                number.setError("Required Field");
            }
            return;
        } else {

        }
    }

    private void initializeField() {
        name = findViewById(R.id.inputEventName);
        start = findViewById(R.id.inputEventSDate);
        end = findViewById(R.id.inputEventEDate);
        fee = findViewById(R.id.inputEventFee);
        email = findViewById(R.id.inputEventEmail);
        number = findViewById(R.id.inputEventNumber);
    }
}
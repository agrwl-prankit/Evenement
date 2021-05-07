package com.prankit.evenement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;
import com.prankit.evenement.adapter.EventsAdapter;
import com.prankit.evenement.adapter.ShowParticipantAdapter;
import com.prankit.evenement.models.ParticipantInfo;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ShowParticipantsActivity extends AppCompatActivity {

    private String eventId;
    private User user;
    private MongoCollection<Document> userCollection, applyCollection;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    ArrayList<ParticipantInfo> participants = new ArrayList<>();
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_participants);

        eventId = getIntent().getStringExtra("eventId");
        String eventName = getIntent().getStringExtra("eventName");

        TextView toolBar = findViewById(R.id.showParticipantToolBar);
        toolBar.setText(" Event name : " + eventName);

        ImageView closeActivity = findViewById(R.id.closeParticipantActivity);
        closeActivity.setOnClickListener(view -> {
            finish();
        });

        initializeDb();

        loadingBar = new ProgressDialog(this);
        recyclerView = findViewById(R.id.participantRecycleList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        retrieveInfo();
    }

    private void retrieveInfo() {
        Log.i("eid1", eventId);
        loadingBar.setTitle("Collecting Participants");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        participants.clear();
        Document findQuery = new Document("eventId", eventId);
        RealmResultTask<MongoCursor<Document>> findTask = applyCollection.find(findQuery).iterator();
        findTask.getAsync(task -> {
            MongoCursor<Document> result = task.get();
            while (result.hasNext()) {
                Document currentDoc = result.next();
                Log.i("eiddoc", currentDoc.toJson());
                String userId = currentDoc.getString("appliedUserId");
                Log.i("eiduser", userId);
                Document userQuery = new Document("userId", userId);
                userCollection.findOne(userQuery).getAsync(result1 -> {
                    String name = result1.get().getString("name");
                    String number = result1.get().getString("number");
                    String email = result1.get().getString("email");
                    ParticipantInfo participantInfo = new ParticipantInfo(name, number, email);
                    participants.add(participantInfo);
                    adapter = new ShowParticipantAdapter(participants, ShowParticipantsActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);
                });
            }
            loadingBar.dismiss();
            Log.i("partList", "" + participants.size());
        });
    }

    private void initializeDb() {
        Realm.init(this);
        AppInfo appInfo = new AppInfo();
        user = appInfo.getApp().currentUser();
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase db = client.getDatabase("Event");
        userCollection = db.getCollection("User_Info");
        applyCollection = db.getCollection("Apply_User");
    }
}
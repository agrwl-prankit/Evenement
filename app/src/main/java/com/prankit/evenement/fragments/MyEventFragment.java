package com.prankit.evenement.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;
import com.prankit.evenement.adapter.MyEventAdapter;
import com.prankit.evenement.models.EventInfo;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MyEventFragment extends Fragment {

    private View view;
    AppInfo appInfo;
    private User user;
    private MongoCollection<Document> postCollection;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    ArrayList<EventInfo> events = new ArrayList<>();

    public MyEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_event, container, false);
        initializeDb();

        recyclerView = view.findViewById(R.id.myEventRecycleList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        retrieveInfo();

        return view;
    }

    public  void  retrieveInfo() {
        events.clear();
        Document findQuery = new Document().append("userId", user.getId());
        RealmResultTask<MongoCursor<Document>> findTask = postCollection.find(findQuery).iterator();
        findTask.getAsync(task ->{
            if (task.isSuccess()){
                MongoCursor<Document> result = task.get();
                while (result.hasNext()){
                    Document currentDoc = result.next();
                    if (!currentDoc.getString("eventName").equals("")){
                        String userId = currentDoc.getString("userId");
                        String createrName = currentDoc.getString("createrName");
                        String eventName = currentDoc.getString("eventName");
                        String startDate = currentDoc.getString("startDate");
                        String endDate = currentDoc.getString("endDate");
                        String fee = currentDoc.getString("fee");
                        String email = currentDoc.getString("email");
                        String number = currentDoc.getString("number");
                        ObjectId _id = currentDoc.getObjectId("_id");
                        EventInfo eventInfo = new EventInfo(_id.toString(), userId, createrName, eventName,
                                startDate, endDate, fee, email, number);
                        events.add(eventInfo);
                        adapter = new MyEventAdapter(events, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(layoutManager);
                    }
                }
            }
        });
    }

    private void initializeDb() {
        Realm.init(getContext());
        appInfo = new AppInfo();
        user = appInfo.getApp().currentUser();
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase db = client.getDatabase("Event");
        postCollection = db.getCollection("Post_Info");
    }
}
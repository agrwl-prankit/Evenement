package com.prankit.evenement.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;
import com.prankit.evenement.activities.AddEventActivity;
import com.prankit.evenement.activities.LoginActivity;
import com.prankit.evenement.activities.MainActivity;
import com.prankit.evenement.activities.ShowParticipantsActivity;
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

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.ViewHolder> {

    private ProgressDialog loadingBar;
    private ArrayList<EventInfo> eventList;
    private Context context;
    AppInfo appInfo;
    User user;
    private MongoCollection<Document> postCollection, applyCollection;

    public MyEventAdapter(ArrayList<EventInfo> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_event_view, parent, false);
        initializeDb();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(eventList.get(position).getEventName());
        holder.sdate.setText(eventList.get(position).getStartDate());
        holder.edate.setText(eventList.get(position).getEndDate());
        holder.fee.setText(eventList.get(position).getFee());
        holder.number.setText(eventList.get(position).getNumber());
        holder.cName.setText(eventList.get(position).getCreaterName());

        holder.participant.setVisibility(View.VISIBLE);
        holder.l1.setVisibility(View.VISIBLE);
        holder.edit.setVisibility(View.VISIBLE);
        holder.l2.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.VISIBLE);

        holder.participant.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowParticipantsActivity.class);
            intent.putExtra("eventId", eventList.get(position).get_id());
            intent.putExtra("eventName", eventList.get(position).getEventName());
            context.startActivity(intent);
        });

        holder.edit.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddEventActivity.class);
            intent.putExtra("FromIntent", "yes");
            intent.putExtra("eid", eventList.get(position).get_id());
            intent.putExtra("ename", eventList.get(position).getEventName());
            intent.putExtra("uid", eventList.get(position).getUserId());
            intent.putExtra("uname", eventList.get(position).getCreaterName());
            intent.putExtra("num", eventList.get(position).getNumber());
            intent.putExtra("mail", eventList.get(position).getEmail());
            intent.putExtra("fee", eventList.get(position).getFee());
            intent.putExtra("sd", eventList.get(position).getStartDate());
            intent.putExtra("ed", eventList.get(position).getEndDate());
            context.startActivity(intent);
        });

        holder.delete.setOnClickListener(view -> {
            Log.i("del", "delete clicked");
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Delete Event");
            dialog.setMessage("Do you want to delete this event?");
            dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                loadingBar.setTitle("Deleting Event");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();

                Document postQuery = new Document("_id", new ObjectId(eventList.get(position).get_id()));
                postCollection.deleteOne(postQuery).getAsync(result -> {
                    if (result.isSuccess()){
                        Document applyQuery = new Document("eventId", eventList.get(position).get_id());
                        applyCollection.deleteMany(applyQuery).getAsync(result1 -> {
                            if (result1.isSuccess()){
                                context.startActivity(new Intent(context, MainActivity.class));
                                Toast.makeText(context, "event deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                            else { new AlertDialog.Builder(context)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Error in deleting event")
                                    .setMessage(result.getError().getErrorMessage())
                                    .setPositiveButton("Ok", null)
                                    .show();
                            }
                        });
                    }
                    else { new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Error in deleting event")
                            .setMessage(result.getError().getErrorMessage())
                            .setPositiveButton("Ok", null)
                            .show();
                    }
                    loadingBar.dismiss();
                });
            }).setNegativeButton("No", (dialogInterface, i) -> {});
            dialog.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, sdate, edate, fee, number, cName;
        LinearLayout participant, l1, edit, l2, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.eventName);
            cName = itemView.findViewById(R.id.eventCreaterName);
            sdate = itemView.findViewById(R.id.eventSDate);
            edate = itemView.findViewById(R.id.eventEDate);
            fee = itemView.findViewById(R.id.eventfee);
            participant = itemView.findViewById(R.id.eventParticipant);
            l1 = itemView.findViewById(R.id.eventL1);
            edit = itemView.findViewById(R.id.eventEdit);
            l2 = itemView.findViewById(R.id.eventL2);
            delete = itemView.findViewById(R.id.eventDelete);
            number = itemView.findViewById(R.id.eventCreaterNumber);
        }
    }

    private void initializeDb() {
        Realm.init(context);
        appInfo = new AppInfo();
        user = appInfo.getApp().currentUser();
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase db = client.getDatabase("Event");
        applyCollection = db.getCollection("Apply_User");
        postCollection = db.getCollection("Post_Info");
        loadingBar = new ProgressDialog(context);
    }
}

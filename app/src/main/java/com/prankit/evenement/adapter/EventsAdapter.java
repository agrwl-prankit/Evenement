package com.prankit.evenement.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.prankit.evenement.R;
import com.prankit.evenement.Utils.AppInfo;
import com.prankit.evenement.models.EventInfo;
import org.bson.Document;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private ProgressDialog loadingBar;
    private final ArrayList<EventInfo> eventList;
    private final Context context;
    AppInfo appInfo;
    User user;
    private MongoCollection<Document> collection;

    public EventsAdapter(ArrayList<EventInfo> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_event_view, parent, false);

        initializeDb();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(eventList.get(position).getEventName());
        holder.sdate.setText(eventList.get(position).getStartDate());
        holder.edate.setText(eventList.get(position).getEndDate());
        holder.fee.setText(eventList.get(position).getFee());
        holder.createrName.setText(eventList.get(position).getCreaterName());
        holder.number.setText(eventList.get(position).getNumber());
        holder.applyText.setText("Apply");
        holder.callText.setText("Call");
        holder.emailText.setText("Email");

        Document findQuery = new Document("appliedUserId", user.getId());
        RealmResultTask<MongoCursor<Document>> findTask = collection.find(findQuery).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> result = task.get();
                while (result.hasNext()) {
                    Document currentDoc = result.next();
                    if (currentDoc.getString("eventId").equals(eventList.get(position).get_id())) {
                        holder.applyText.setText("Applied");
                        holder.applyText.setTextColor(Color.BLACK);
                    }
                }
            }
        });

        holder.call.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + eventList.get(position).getNumber()));
            context.startActivity(callIntent);
        });

        holder.email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {eventList.get(position).getEmail()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Apply in " + eventList.get(position).getEventName());
            Intent mailer = Intent.createChooser(intent, null);
            context.startActivity(mailer);
        });

        holder.apply.setOnClickListener(view -> {
            if (holder.applyText.getText().equals("Apply")) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Apply in event");
                dialog.setMessage("Do you want to apply in this event?");
                dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                    loadingBar.setTitle("Applying");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                    collection.insertOne(new Document("appliedUserId", user.getId())
                            .append("createrId", eventList.get(position).getUserId())
                            .append("eventId", eventList.get(position).get_id())).getAsync(result -> {
                        if (!result.isSuccess()) {
                            new AlertDialog.Builder(context)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Error in adding profile")
                                    .setMessage(result.getError().getErrorMessage())
                                    .setPositiveButton("Ok", null)
                                    .show();
                        } else {
                            holder.applyText.setText("Applied");
                            holder.applyText.setTextColor(Color.BLACK);
                            Toast.makeText(context, "Applied Successfully", Toast.LENGTH_SHORT).show();
                        }
                        loadingBar.dismiss();
                    });
                }).setNegativeButton("No", (dialogInterface, i) -> {});
                dialog.create(); dialog.show();
            } else {
                Toast.makeText(context, "You have already applied in this event", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, sdate, edate, fee, createrName, applyText, number, callText, emailText;
        LinearLayout apply, l1, call, l2, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.eventName);
            sdate = itemView.findViewById(R.id.eventSDate);
            edate = itemView.findViewById(R.id.eventEDate);
            fee = itemView.findViewById(R.id.eventfee);
            createrName = itemView.findViewById(R.id.eventCreaterName);
            applyText = itemView.findViewById(R.id.eventParticipantText);
            apply = itemView.findViewById(R.id.eventParticipant);
            l1 = itemView.findViewById(R.id.eventL1);
            call = itemView.findViewById(R.id.eventEdit);
            callText = itemView.findViewById(R.id.eventEditText);
            l2 = itemView.findViewById(R.id.eventL2);
            email = itemView.findViewById(R.id.eventDelete);
            emailText = itemView.findViewById(R.id.eventDeleteText);
            number = itemView.findViewById(R.id.eventCreaterNumber);
        }
    }

    private void initializeDb() {
        Realm.init(context);
        appInfo = new AppInfo();
        user = appInfo.getApp().currentUser();
        MongoClient client = user.getMongoClient("mongodb-atlas");
        MongoDatabase db = client.getDatabase("Event");
        collection = db.getCollection("Apply_User");
        loadingBar = new ProgressDialog(context);
    }
}

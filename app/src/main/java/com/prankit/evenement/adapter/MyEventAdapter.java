package com.prankit.evenement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prankit.evenement.R;
import com.prankit.evenement.activities.AddEventActivity;
import com.prankit.evenement.models.EventInfo;

import java.util.ArrayList;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.ViewHolder> {

    private ArrayList<EventInfo> eventList;
    private Context context;

    public MyEventAdapter(ArrayList<EventInfo> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_event_view, parent, false);
        return new MyEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(eventList.get(position).getEventName());
        holder.sdate.setText(eventList.get(position).getStartDate());
        holder.edate.setText(eventList.get(position).getEndDate());
        holder.fee.setText(eventList.get(position).getFee());
        holder.number.setText(eventList.get(position).getNumber());

        holder.participant.setVisibility(View.VISIBLE);
        holder.l1.setVisibility(View.VISIBLE);
        holder.edit.setVisibility(View.VISIBLE);
        holder.l2.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.VISIBLE);

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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, sdate, edate, fee, participantText, number;
        LinearLayout participant, l1, edit, l2, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.eventName);
            sdate = itemView.findViewById(R.id.eventSDate);
            edate = itemView.findViewById(R.id.eventEDate);
            fee = itemView.findViewById(R.id.eventfee);
            participantText = itemView.findViewById(R.id.eventParticipantText);
            participant = itemView.findViewById(R.id.eventParticipant);
            l1 = itemView.findViewById(R.id.eventL1);
            edit = itemView.findViewById(R.id.eventEdit);
            l2 = itemView.findViewById(R.id.eventL2);
            delete = itemView.findViewById(R.id.eventDelete);
            number = itemView.findViewById(R.id.eventCreaterNumber);
        }
    }
}

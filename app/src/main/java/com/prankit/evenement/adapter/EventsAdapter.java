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
import com.prankit.evenement.activities.ApplyEventActivity;
import com.prankit.evenement.models.EventInfo;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

    private ArrayList<EventInfo> eventList;
    private Context context;

    public EventsAdapter(ArrayList<EventInfo> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_event_view, parent, false);
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(eventList.get(position).getEventName());
        holder.sdate.setText(eventList.get(position).getStartDate());
        holder.edate.setText(eventList.get(position).getEndDate());
        holder.fee.setText(eventList.get(position).getFee());

        holder.participant.setVisibility(View.VISIBLE);
        holder.participantText.setText("Apply");

        holder.participant.setOnClickListener(view -> {
            Intent intent = new Intent(context, ApplyEventActivity.class);
            intent.putExtra("eventId", eventList.get(position).get_id());
            intent.putExtra("createrId", eventList.get(position).getUserId());
            intent.putExtra("eventName", eventList.get(position).getEventName());
            intent.putExtra("createrName", eventList.get(position).getCreaterName());
            intent.putExtra("sDate", eventList.get(position).getStartDate());
            intent.putExtra("eDate", eventList.get(position).getEndDate());
            intent.putExtra("fee", eventList.get(position).getFee());
            intent.putExtra("email", eventList.get(position).getEmail());
            intent.putExtra("number", eventList.get(position).getNumber());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, sdate, edate, fee, participantText;
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
        }
    }
}

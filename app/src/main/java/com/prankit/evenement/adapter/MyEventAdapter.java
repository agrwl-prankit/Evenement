package com.prankit.evenement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prankit.evenement.R;
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
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, sdate, edate, fee;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.eventName);
            sdate = itemView.findViewById(R.id.eventSDate);
            edate = itemView.findViewById(R.id.eventEDate);
            fee = itemView.findViewById(R.id.eventFee);
        }
    }
}

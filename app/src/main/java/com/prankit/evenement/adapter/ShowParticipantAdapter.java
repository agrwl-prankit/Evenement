package com.prankit.evenement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.prankit.evenement.R;
import com.prankit.evenement.models.ParticipantInfo;
import java.util.ArrayList;

public class ShowParticipantAdapter extends RecyclerView.Adapter<ShowParticipantAdapter.ViewHolder> {

    private ArrayList<ParticipantInfo> participantList;
    private Context context;

    public ShowParticipantAdapter(ArrayList<ParticipantInfo> participantList, Context context) {
        this.participantList = participantList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_participant_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(participantList.get(position).getpName());
        holder.email.setText(participantList.get(position).getpEmail());
        holder.number.setText(participantList.get(position).getpNumber());
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pName);
            email = itemView.findViewById(R.id.pEmail);
            number = itemView.findViewById(R.id.pNumber);
        }
    }
}

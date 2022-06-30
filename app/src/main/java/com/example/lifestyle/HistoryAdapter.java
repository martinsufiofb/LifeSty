package com.example.lifestyle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<History> historyList;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.bind(history);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView nameOfExercise;
        public TextView user;
        public TextView count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tvDate);
            nameOfExercise = itemView.findViewById(R.id.tvnameOfExerciseH);
            user = itemView.findViewById(R.id.tvUserH);
            count = itemView.findViewById(R.id.tvCountH);
        }

        public void bind(History history) {
            user.setText(history.getUser().getUsername());
            date.setText(history.getCreatedAt().toString().substring(0,10));
            nameOfExercise.setText(history.getNameOfExercise());
            count.setText(history.getCount());
        }
    }
}

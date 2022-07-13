package com.example.lifestyle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<ParseUser> users;

    public SearchAdapter(Context context, List<ParseUser> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        ParseUser user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvPushupsNo;
        private TextView tvSitupsNo;
        private TextView tvSquatsNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSearchUsername);
            tvPushupsNo = itemView.findViewById(R.id.tvSearchPushupsNo);
            tvSitupsNo = itemView.findViewById(R.id.tvSearchSitupsNo);
            tvSquatsNo = itemView.findViewById(R.id.tvSearchSquatsN0);
        }

        public void bind(ParseUser user) {
            tvTitle.setText(user.getUsername());
            tvPushupsNo.setText(Integer.toString(user.getInt("pushUps")));
            tvSitupsNo.setText(Integer.toString(user.getInt("sitUps")));
            tvSquatsNo.setText(Integer.toString(user.getInt("squats")));
        }
    }
}

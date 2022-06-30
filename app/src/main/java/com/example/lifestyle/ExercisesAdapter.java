package com.example.lifestyle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> exercises;

    public ExercisesAdapter(Context context, List<Exercise> exercises){
        this.context = context;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_exercise,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitleExercise);
            tvDescription = itemView.findViewById(R.id.tvDescriptionExercise);
            ivImage = itemView.findViewById(R.id.ivImageExercise);
            itemView.setOnClickListener(this);
        }

        public void bind(Exercise exercise) {
            tvDescription.setText(exercise.getDescription());
            tvTitle.setText(exercise.getTitle());
            Glide.with(context).load(exercise.getImage().getUrl()).into(ivImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                Intent intent = new Intent(context, ExercisesCounter.class);
                intent.putExtra("page",position);
                context.startActivity(intent);
            }
        }
    }
}

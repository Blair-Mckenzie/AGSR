package com.example.agsr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class WalkingActivity extends RecyclerView.Adapter<WalkingActivity.ViewHolder> {

    private ArrayList<Walks> mData;
    private LayoutInflater mInflater;
    ProgressBar progressBar;
    TextView currentSteps;
    TextView displayPercentage;


    // data is passed into the constructor
    WalkingActivity(Context context, ArrayList<Walks> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.walking_activity, parent, false);
        progressBar = view.findViewById(R.id.progress_bar);
        currentSteps = view.findViewById(R.id.text_view_steps_progress);
        displayPercentage = view.findViewById(R.id.text_view_progress_percentage);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Walks walk = mData.get(position);
        holder.title.setText(walk.getTitle());
        holder.numSteps.setText(MessageFormat.format("{0} Steps",String.valueOf(walk.getNumSteps())));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView numSteps;
        Button deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.activity_name);
            numSteps = itemView.findViewById(R.id.num_steps);
            deleteButton = itemView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment_home.setNumSteps(fragment_home.getNumSteps()- mData.get(getAdapterPosition()).getNumSteps()); ;
                    fragment_home.setProg(((double) fragment_home.getNumSteps() / (double)fragment_home.getGoal() * 100));
                    fragment_home.updateProgressBar();
                    removeAt(getAdapterPosition());
                }
            });
        }
    }
}

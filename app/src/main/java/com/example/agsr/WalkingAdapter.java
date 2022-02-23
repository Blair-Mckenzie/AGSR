package com.example.agsr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class WalkingAdapter extends ListAdapter<Walk,WalkingAdapter.WalkViewHolder> {

    private WalkingAdapter.OnItemClickListener listener;
    WalkingAdapter(@NonNull DiffUtil.ItemCallback<Walk> diffCallback){
        super(diffCallback);
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(WalkingAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    ProgressBar progressBar;
    TextView currentSteps;
    TextView displayPercentage;


    @NonNull
    @Override
    public WalkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.walking_item, parent, false);
        WalkViewHolder wvh = new WalkViewHolder(view,listener);
        return wvh;
    }

    @Override
    public void onBindViewHolder(@NonNull WalkViewHolder holder, int position) {
        Walk walk = getItem(position);
        holder.walkTitleView.setText(walk.getTitle());
        holder.walkNumStepsView.setText(MessageFormat.format("{0} Steps",String.valueOf(walk.getNumSteps())));

    }

    public class WalkViewHolder extends RecyclerView.ViewHolder {
        TextView walkTitleView;
        TextView walkNumStepsView;
        Button addWalkButton;
        Button deleteButton;

        WalkViewHolder(View itemView, final WalkingAdapter.OnItemClickListener listener) {
            super(itemView);
            walkTitleView = itemView.findViewById(R.id.activity_name);
            walkNumStepsView = itemView.findViewById(R.id.walk_num_steps);
            addWalkButton = itemView.findViewById(R.id.add_steps_button);
            deleteButton = itemView.findViewById(R.id.walk_delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    listener.onDeleteClick(position);
                }
            });
        }
    }
    static class TargetDiff extends DiffUtil.ItemCallback<Walk> {

        @Override
        public boolean areItemsTheSame(@NonNull Walk oldItem, @NonNull Walk newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Walk oldItem, @NonNull Walk newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}

package com.example.agsr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class TargetAdapter extends ListAdapter<Target,TargetAdapter.TargetViewHolder> {

    private OnItemClickListener listener;
    TargetAdapter(@NonNull DiffUtil.ItemCallback<Target> diffCallback){
        super(diffCallback);
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public TargetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.target_item, parent, false);
        TargetViewHolder tvh = new TargetViewHolder(view,listener);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TargetViewHolder holder, int position) {
        Target target = getItem(position);
        holder.targetTitleView.setText(target.getTitle());
        holder.targetNumStepsView.setText(MessageFormat.format("{0} Steps",String.valueOf(target.getNumSteps())));
    }

    public class TargetViewHolder extends RecyclerView.ViewHolder {
         TextView targetTitleView;
         TextView targetNumStepsView;
         Button addTargetButton;
         Button deleteButton;

        TargetViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            targetTitleView = itemView.findViewById(R.id.target_title);
            targetNumStepsView = itemView.findViewById(R.id.target_steps);
            addTargetButton = itemView.findViewById(R.id.fab);
            deleteButton = itemView.findViewById(R.id.target_delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    listener.onDeleteClick(position);
                }
            });
        }
    }

    static class WordDiff extends DiffUtil.ItemCallback<Target> {

        @Override
        public boolean areItemsTheSame(@NonNull Target oldItem, @NonNull Target newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Target oldItem, @NonNull Target newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}

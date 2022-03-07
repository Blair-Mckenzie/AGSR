package com.example.agsr;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;


public class TargetAdapter extends ListAdapter<Target, TargetAdapter.TargetViewHolder> {

    private OnItemClickListener listener;
    Context context;

    TargetAdapter(@NonNull DiffUtil.ItemCallback<Target> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);

        void onEditClick(int position);
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TargetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.target_item, parent, false);
        return new TargetViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TargetViewHolder holder, int position) {
        Target target= getItem(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("AGSR", Context.MODE_PRIVATE);
        Boolean isGoalsToggled = sharedPreferences.getBoolean("goalsToggle", false);
        holder.targetTitleView.setText(target.getTitle());
        holder.targetNumStepsView.setText(MessageFormat.format("{0} Steps", String.valueOf(target.getNumSteps())));
        if(!isGoalsToggled){
            holder.editButton.setVisibility(View.INVISIBLE);
        }else {
            holder.editButton.setVisibility(View.VISIBLE);
        }
    }

    public class TargetViewHolder extends RecyclerView.ViewHolder {
        LinearLayout targetLayout;
        TextView targetTitleView;
        TextView targetNumStepsView;
        Button addTargetButton;
        Button deleteButton;
        Button editButton;

        TargetViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            targetLayout = itemView.findViewById(R.id.target_layout);
            targetTitleView = itemView.findViewById(R.id.target_title);
            targetNumStepsView = itemView.findViewById(R.id.target_steps);
            addTargetButton = itemView.findViewById(R.id.fab);
            deleteButton = itemView.findViewById(R.id.target_delete_button);
            editButton = itemView.findViewById(R.id.target_edit_button);

            deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                listener.onDeleteClick(position);
            });
            editButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                listener.onEditClick(position);
            });
            targetLayout.setOnClickListener(view -> {
                int position = getAdapterPosition();
                listener.onClick(position);
            });

        }
    }

    static class TargetDiff extends DiffUtil.ItemCallback<Target> {

        @Override
        public boolean areItemsTheSame(@NonNull Target oldItem, @NonNull Target newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Target oldItem, @NonNull Target newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getNumSteps() == newItem.getNumSteps() &&
                    oldItem.isActive() == newItem.isActive();
        }
    }
}

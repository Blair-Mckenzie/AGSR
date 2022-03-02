package com.example.agsr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;


public class HistoryAdapter extends ListAdapter<History, HistoryAdapter.HistoryViewHolder> {

    HistoryAdapter(@NonNull DiffUtil.ItemCallback<History> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = getItem(position);
        double prog = (((double) history.getCurrentSteps() / (double) history.getGoalSteps()) * 100);
        holder.historyTargetTitle.setText(MessageFormat.format("Goal Name - {0}",history.getGoalTitle()));
        holder.historyStepsWalked.setText(MessageFormat.format("Steps Walked - {0}",String.valueOf(history.getCurrentSteps())));
        holder.historyTargetSteps.setText(MessageFormat.format("Target Steps - {0}",String.valueOf(history.getGoalSteps())));
        holder.historyGoalCompleted.setText(MessageFormat.format("Goal Completed - {0} %", String.valueOf(Math.round(prog))));
//        holder.targetNumStepsView.setText(MessageFormat.format("{0} Steps", String.valueOf(target.getNumSteps())));
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout targetLayout;
        TextView historyTargetTitle;
        TextView historyStepsWalked;
        TextView historyTargetSteps;
        TextView historyGoalCompleted;

        HistoryViewHolder(View itemView) {
            super(itemView);
//            targetLayout = itemView.findViewById(R.id.target_layout);
            historyTargetTitle = itemView.findViewById(R.id.history_target_title);
            historyStepsWalked = itemView.findViewById(R.id.history_completed_steps);
            historyTargetSteps = itemView.findViewById(R.id.history_target_steps);
            historyGoalCompleted = itemView.findViewById(R.id.history_goal_completed);

        }
    }

    static class TargetDiff extends DiffUtil.ItemCallback<History> {
        @Override
        public boolean areItemsTheSame(@NonNull History oldItem, @NonNull History newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull History oldItem, @NonNull History newItem) {
            return oldItem.getGoalTitle().equals(newItem.getGoalTitle()) &&
                    oldItem.getGoalSteps() == newItem.getGoalSteps()&&
                    oldItem.getCurrentSteps() == newItem.getCurrentSteps();
        }
    }
}

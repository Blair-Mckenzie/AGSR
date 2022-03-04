package com.example.agsr;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home extends Fragment {

    private RecyclerView recyclerView;
    public static WalkViewModel walkViewModel;
    WalkingAdapter adapter;

    public static int numSteps;
    private static int goal = 10000;

    Target activeTarget;
    private ProgressBar progressBar;
    private TextView displayCurrentSteps;
    private TextView displayPercentage;
    private TextView goalName;

    private String date;

    public fragment_home() {
        // Required empty public constructor
    }

    public static fragment_home newInstance(int prog, int goal) {
        fragment_home fragment = new fragment_home();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("AGSR", Context.MODE_PRIVATE);
        String activeTitle = sharedPreferences.getString("activeGoalTitle", "Default");
        goalName.setText(activeTitle);
        goal = sharedPreferences.getInt("activeGoalSteps", 10000);
        recyclerView.postDelayed(() -> {
            if (adapter.getCurrentList().size() != 0) {
                numSteps = adapter.getCurrentList().get(adapter.getCurrentList().size() - 1).getCurrentSteps();
            } else {
                numSteps = 0;
            }
            updateProgressBar();
        }, 100);
    }

    @Override
    public void onPause() {
        super.onPause();
        HistoryViewModel historyViewModel = new ViewModelProvider(getActivity()).get(HistoryViewModel.class);
        historyViewModel.insert(new History(date, goalName.getText().toString(), goal, numSteps));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(receiver, new IntentFilter("sendData"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.walking_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));

        adapter = new WalkingAdapter(new WalkingAdapter.TargetDiff());
        recyclerView.setAdapter(adapter);
        walkViewModel = new ViewModelProvider(this).get(WalkViewModel.class);
        walkViewModel.getAllWalks().observe(getViewLifecycleOwner(), walks -> adapter.submitList(walks));

        EditText addStepsTextview = view.findViewById(R.id.steps_input);
        displayCurrentSteps = view.findViewById(R.id.text_view_steps_progress);
        displayPercentage = view.findViewById(R.id.text_view_progress_percentage);
        MaterialButton addStepsButton = view.findViewById(R.id.add_steps_button);
        progressBar = view.findViewById(R.id.progress_bar);
        goalName = view.findViewById(R.id.current_goal_textview);

        TextView dateTimeDisplay = view.findViewById(R.id.date_view);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        addStepsButton.setOnClickListener(view1 -> {
            if (isEmptyInput(addStepsTextview)) {
                addStepsTextview.setError("Required");
            } else {
                numSteps += Integer.parseInt((addStepsTextview.getText().toString()));
                updateProgressBar();
                walkViewModel.insert(new Walk(Integer.parseInt(addStepsTextview.getText().toString()), numSteps));
            }
        });

        adapter.setOnItemClickListener(position -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.confirm_delete_popup, (ViewGroup) getView(), false);
            TextView confrimDelete = viewInflated.findViewById(R.id.delete_popup);
            builder.setView(viewInflated);
            String message = "Are you sure you wish to delete these steps";
            confrimDelete.setText(message);
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                dialog.dismiss();
                numSteps -= adapter.getCurrentList().get(position).getNumSteps();
                updateProgressBar();
                walkViewModel.delete(adapter.getCurrentList().get(position));
            });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        });

        return view;
    }

    public void updateProgressBar() {
        double prog = (((double) numSteps / (double) goal) * 100);
//        progressBar.setProgress((int) prog, true);
        ObjectAnimator.ofInt(progressBar, "progress", (int) prog).setDuration(350).start();
        displayCurrentSteps.setText(MessageFormat.format("{0} / {1}", String.valueOf(numSteps), goal));
        displayPercentage.setText(MessageFormat.format("{0} %", String.valueOf(Math.round(prog))));
    }

    private boolean isEmptyInput(EditText editText) {
        return editText.getText().toString().length() == 0;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            activeTarget = (Target) intent.getSerializableExtra("target");
            if (activeTarget != null) {
                goalName.setText(activeTarget.getTitle());
                goal = activeTarget.getNumSteps();
                updateProgressBar();
            }
        }
    };

}
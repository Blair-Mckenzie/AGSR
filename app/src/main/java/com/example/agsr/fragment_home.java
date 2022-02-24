package com.example.agsr;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home extends Fragment {

    private RecyclerView recyclerView;
    private WalkViewModel walkViewModel;
    WalkingAdapter adapter;

    public static int numSteps;
    private static int goal = 10000;

    private ProgressBar progressBar;
    private TextView displayCurrentSteps;
    private TextView displayPercentage;

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
        if (adapter.getCurrentList().size() != 0) {
            numSteps = adapter.getCurrentList().get(adapter.getCurrentList().size() - 1).getCurrentSteps();
        }
        else{
            numSteps = 0;
        }
        updateProgressBar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter.getCurrentList().size() != 0) {
            numSteps = adapter.getCurrentList().get(adapter.getCurrentList().size() - 1).getCurrentSteps();
        }
        else{
            numSteps = 0;
        }
        updateProgressBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter.getCurrentList().size() != 0) {
            numSteps = adapter.getCurrentList().get(adapter.getCurrentList().size() - 1).getCurrentSteps();
        }
        else{
            numSteps = 0;
        }
        updateProgressBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.walking_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new WalkingAdapter(new WalkingAdapter.TargetDiff());
        recyclerView.setAdapter(adapter);
        walkViewModel = new ViewModelProvider(this).get(WalkViewModel.class);
        walkViewModel.getAllWalks().observe(getViewLifecycleOwner(), walks -> adapter.submitList(walks));

        EditText addStepsTextview = view.findViewById(R.id.steps_input);
        EditText addActivityName = view.findViewById(R.id.activity_name_input);
        displayCurrentSteps = view.findViewById(R.id.text_view_steps_progress);
        displayPercentage = view.findViewById(R.id.text_view_progress_percentage);
        MaterialButton addStepsButton = view.findViewById(R.id.add_steps_button);
        progressBar = view.findViewById(R.id.progress_bar);
        TextView goalName = view.findViewById(R.id.current_goal_textview);


        if (adapter.getCurrentList().size() != 0) {
            numSteps = adapter.getCurrentList().get(adapter.getCurrentList().size() - 1).getCurrentSteps();
        }else{
            numSteps = 0;
        }
        updateProgressBar();

        addStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmptyInput(addStepsTextview) && isEmptyInput(addActivityName)) {
                    addStepsTextview.setError("Required");
                    addActivityName.setError("Required");
                } else if (isEmptyInput(addStepsTextview)) {
                    addStepsTextview.setError("Required");
                } else if (isEmptyInput(addActivityName)) {
                    addActivityName.setError("Required");
                } else {
                    numSteps += Integer.parseInt((addStepsTextview.getText().toString()));
                    updateProgressBar();
                    walkViewModel.insert(new Walk(addActivityName.getText().toString(), Integer.parseInt(addStepsTextview.getText().toString()), numSteps));
                }
            }
        });

        adapter.setOnItemClickListener(new WalkingAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                numSteps -= adapter.getCurrentList().get(position).getNumSteps();
                updateProgressBar();
                walkViewModel.delete(adapter.getCurrentList().get(position));
            }
        });
        return view;
    }

    public void updateProgressBar() {
        double prog = (((double) numSteps / (double) goal) * 100);
        progressBar.setProgress((int) prog);
        displayCurrentSteps.setText(MessageFormat.format("{0} / {1}", String.valueOf(numSteps), goal));
        displayPercentage.setText(MessageFormat.format("{0} %", String.valueOf(Math.round(prog))));
    }

    private boolean isEmptyInput(EditText editText) {
        return editText.getText().toString().length() == 0;
    }

}
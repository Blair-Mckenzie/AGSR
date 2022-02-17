package com.example.agsr;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class fragment_targets extends Fragment {

    private RecyclerView recyclerView;
    private TargetViewModel targetViewModel;
    TargetAdapter adapter;

    public fragment_targets() {
        // Required empty public constructor
    }

    public static fragment_targets newInstance(String param1, String param2) {
        fragment_targets fragment = new fragment_targets();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_targets, container, false);

        recyclerView = view.findViewById(R.id.targets_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new TargetAdapter(new TargetAdapter.WordDiff());
        recyclerView.setAdapter(adapter);
        targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);
        targetViewModel.getAllTargets().observe(getViewLifecycleOwner(), targets -> {
            adapter.submitList(targets);
        });

        FloatingActionButton addGoalButton = view.findViewById(R.id.fab);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add New Goal");
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_target_popup, (ViewGroup) getView(), false);
                EditText titleInput = viewInflated.findViewById(R.id.target_title_input);
                EditText stepsInput = viewInflated.findViewById(R.id.target_steps_input);

                builder.setView(viewInflated);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int numSteps = Integer.parseInt(stepsInput.getText().toString());
                        targetViewModel.insert(new Target(titleInput.getText().toString(), numSteps, false));
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        adapter.setOnItemClickListener(new TargetAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                targetViewModel.delete(adapter.getCurrentList().get(position));
            }
            @Override
            public void onEditClick(int position){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Goal");
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_target_popup, (ViewGroup) getView(), false);
                EditText titleInput = viewInflated.findViewById(R.id.target_title_input);
                EditText stepsInput = viewInflated.findViewById(R.id.target_steps_input);

                TextView currentTitle = view.findViewById(R.id.target_title);
                TextView currentSteps = view.findViewById(R.id.target_steps);

                titleInput.setText(currentTitle.getText());
                String currentStepsDisplay = currentSteps.getText().toString();
                stepsInput.setText(currentStepsDisplay.substring(0,currentStepsDisplay.length()-6));
                builder.setView(viewInflated);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Target updatedTarget = adapter.getCurrentList().get(position);
                        updatedTarget.setTitle(titleInput.getText().toString());
                        updatedTarget.setNumSteps(Integer.parseInt(stepsInput.getText().toString()));
                        targetViewModel.update(updatedTarget);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        return view;
    }

}
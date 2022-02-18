package com.example.agsr;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

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
        View view = setUpLayout(inflater, container);

        FloatingActionButton addGoalButton = view.findViewById(R.id.fab);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupDialog("Add New Goal", 'a', view, 0);
            }
        });

        adapter.setOnItemClickListener(new TargetAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                targetViewModel.delete(adapter.getCurrentList().get(position));
            }

            @Override
            public void onEditClick(int position) {
                showPopupDialog("Edit Goal", 'e', view, position);
            }

            @Override
            public void onLongClick(int position) {
//                View v = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView;
//                LinearLayout goalLayout= v.findViewById(R.id.target_layout);
//                goalLayout.setBackgroundColor(Color.parseColor("#4359A9"));
//                Target currentTarget = adapter.getCurrentList().get(position);
//                currentTarget.setActive(true);
//                targetViewModel.update(currentTarget);
            }
        });
        return view;

    }

    private boolean checkDialogEmpty(EditText title, EditText numSteps) {
        boolean err = false;
        if (title.getText().toString().length() == 0 && numSteps.getText().toString().length() == 0) {
            title.setError("Required");
            numSteps.setError("Required");
            err = true;
        } else if (title.getText().toString().length() == 0) {
            title.setError("Required");
            err = true;
        } else if (numSteps.getText().toString().length() == 0) {
            numSteps.setError("Required");
            err = true;
        }
        return err;
    }

    private void showPopupDialog(String title, char type, View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_target_popup, (ViewGroup) getView(), false);
        EditText titleInput = viewInflated.findViewById(R.id.target_title_input);
        EditText stepsInput = viewInflated.findViewById(R.id.target_steps_input);
        if (type == 'e') {
            View v = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView;
            TextView currentTitle = v.findViewById(R.id.target_title);
            TextView currentSteps = v.findViewById(R.id.target_steps);
            titleInput.setText(currentTitle.getText().toString());
            String currentStepsDisplay = currentSteps.getText().toString();
            stepsInput.setText(currentStepsDisplay.substring(0, currentStepsDisplay.length() - 6));
            builder.setView(viewInflated);
        }
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(checkDialogEmpty(titleInput, stepsInput))) {
                    dialog.dismiss();
                    if (isDuplicate(titleInput.getText().toString())) {
                        Toast.makeText(getContext(), "Goal Name Already Exists", Toast.LENGTH_LONG).show();
                    }else{
                        if (type == 'a') {
                            int numSteps = Integer.parseInt(stepsInput.getText().toString());
                            targetViewModel.insert(new Target(titleInput.getText().toString(), numSteps, false));
                        } else {
                            Target updatedTarget = adapter.getCurrentList().get(position);
                            updatedTarget.setTitle(titleInput.getText().toString());
                            updatedTarget.setNumSteps(Integer.parseInt(stepsInput.getText().toString()));
                            targetViewModel.update(updatedTarget);
                        }
                    }


                }
            }
        });
    }

    private boolean isDuplicate(String newTitle) {
        List<Target> currentList = adapter.getCurrentList();
        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getTitle().equals(newTitle)) {
                return true;
            }
        }
        return false;
    }

    private View setUpLayout(LayoutInflater inflater, ViewGroup container){
        View view = inflater.inflate(R.layout.fragment_targets, container, false);

        recyclerView = view.findViewById(R.id.targets_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new TargetAdapter(new TargetAdapter.TargetDiff());
        recyclerView.setAdapter(adapter);
        targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);
        targetViewModel.getAllTargets().observe(getViewLifecycleOwner(), targets -> {
            adapter.submitList(targets);
        });
        return view;
    }

}
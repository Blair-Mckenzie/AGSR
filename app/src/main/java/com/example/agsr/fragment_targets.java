package com.example.agsr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class fragment_targets extends Fragment {

    private RecyclerView recyclerView;
    public static TargetViewModel targetViewModel;
    public TargetAdapter adapter;

    private int activeTarget = -1;
    private int selectedPosition = -1;

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
        if (savedInstanceState != null) {
            activeTarget = savedInstanceState.getInt("position");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        List<Target> currentList = adapter.getCurrentList();
        if (currentList.size() != 0) {
            for (int i = 0; i < currentList.size(); i++) {
                if (currentList.get(i).isActive()) {
                    activeTarget = i;
                }
            }
        }
        if(activeTarget != -1){
            sendData(currentList.get(activeTarget));
            outState.putInt("position",activeTarget);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.postDelayed(() -> {
            List<Target> currentList = adapter.getCurrentList();
            if (currentList.size() != 0) {
              for(int i =0; i<currentList.size();i++){
                  if(currentList.get(i).isActive()){
                      changeTargetBackground(i,'s');
                  }
              }
//                if(currentList.get(0).getTitle().equals("Default")){
////                    highlightTarget(0);
//                }
            }
        }, 100);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = setUpLayout(inflater, container);

        FloatingActionButton addGoalButton = view.findViewById(R.id.fab);
        addGoalButton.setOnClickListener(view1 -> showPopupDialog("Add New Goal", 'a', view1, 0));

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
            public void onClick(int position){
                List<Target> currentList = adapter.getCurrentList();
                for(int i =0; i<currentList.size();i++){
                    changeTargetBackground(i,'u');
                    currentList.get(i).setActive(false);
                    targetViewModel.update(currentList.get(i));
                }
                changeTargetBackground(position,'s');
                currentList.get(position).setActive(true);
                targetViewModel.update(currentList.get(position));
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
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!(checkDialogEmpty(titleInput, stepsInput))) {
                dialog.dismiss();
                String newTitle = titleInput.getText().toString().trim();
                if (type == 'e') {
                    Target updatedTarget = adapter.getCurrentList().get(position);
                    if (newTitle.equals(updatedTarget.getTitle())) {
                        int id = updatedTarget.getId();
                        Target target = new Target(titleInput.getText().toString().trim(),Integer.parseInt(stepsInput.getText().toString()),false);
                        target.setId(id);
                        targetViewModel.update(target);
                    } else if (isDuplicate(newTitle)) {
                        Toast.makeText(getContext(), "Goal Name Already Exists", Toast.LENGTH_LONG).show();
                    } else {
                        int id = updatedTarget.getId();
                        Target target = new Target(titleInput.getText().toString().trim(),Integer.parseInt(stepsInput.getText().toString()),false);
                        target.setId(id);
                        targetViewModel.update(target);
                    }
                } else if (isDuplicate(newTitle)) {
                    Toast.makeText(getContext(), "Goal Name Already Exists", Toast.LENGTH_LONG).show();
                } else {
                    int numSteps = Integer.parseInt(stepsInput.getText().toString());
                    targetViewModel.insert(new Target(newTitle, numSteps, false));
                }
            }
        });
    }

    private boolean isDuplicate(String newTitle) {
        List<Target> currentList = adapter.getCurrentList();
        for (int i = 0; i < currentList.size(); i++) {
            if (currentList.get(i).getTitle().equalsIgnoreCase(newTitle.trim())) {
                return true;
            }
        }
        return false;
    }

    private View setUpLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_targets, container, false);

        recyclerView = view.findViewById(R.id.targets_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new TargetAdapter(new TargetAdapter.TargetDiff());
        recyclerView.setAdapter(adapter);
        targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);
        targetViewModel.getAllTargets().observe(getViewLifecycleOwner(), targets -> adapter.submitList(targets));
        return view;
    }

    private void sendData(Target target){
        Intent intent = new Intent("sendData");
        intent.putExtra("target", target);
        LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
    }

    private void changeTargetBackground(int position, char selected){
        View v = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView;
        LinearLayout goalLayout = v.findViewById(R.id.target_layout);
        Button deleteButton = v.findViewById(R.id.target_delete_button);
        Button editButton = v.findViewById(R.id.target_edit_button);
        if(selected == 's'){
            deleteButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.INVISIBLE);
            goalLayout.setBackgroundColor(Color.parseColor("#DBE1FF"));
        }else{
            goalLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
        }

    }

}
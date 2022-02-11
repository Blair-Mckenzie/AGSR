package com.example.agsr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home extends Fragment {
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle bundleRecyclerViewState;
    private RecyclerView recyclerView;

    private static int numSteps = 0;
    private static double prog = 0.0;
    private static int goal = 10000;

    private static ProgressBar  progressBar;
    private static TextView displayCurrentSteps;
    private static TextView displayPercentage;

    WalkingAdapter adapter;
    Parcelable state;



    public fragment_home() {
        // Required empty public constructor
    }
    public static fragment_home newInstance(int prog, int goal) {
        fragment_home fragment = new fragment_home();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        bundleRecyclerViewState = new Bundle();
        state = recyclerView.getLayoutManager().onSaveInstanceState();
        bundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, state);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundleRecyclerViewState != null){
            state = bundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(state);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<Walk> walks = new ArrayList<>();
        recyclerView = view.findViewById(R.id.walking_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new WalkingAdapter(this.getContext(), walks);
        recyclerView.setAdapter(adapter);

        EditText addStepsTextview = view.findViewById(R.id.steps_input);
        EditText addActivityName = view.findViewById(R.id.activity_name_input);
        displayCurrentSteps = view.findViewById(R.id.text_view_steps_progress);
        displayPercentage = view.findViewById(R.id.text_view_progress_percentage);
        MaterialButton addStepsButton = view.findViewById(R.id.add_steps_button);
        MaterialButton deleteButton = view.findViewById(R.id.delete_button);
        progressBar = view.findViewById(R.id.progress_bar);

        displayCurrentSteps.setText(MessageFormat.format("{0} / {1}",numSteps, goal ));
        double progress = (prog / (double)goal) * 100;
        displayPercentage.setText(MessageFormat.format("{0} %",String.valueOf(Math.round(progress))));

        addStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmptyInput(addStepsTextview) && isEmptyInput(addActivityName)){
                    addStepsTextview.setError("Required");
                    addActivityName.setError("Required");
                } else if(isEmptyInput(addStepsTextview)){
                    addStepsTextview.setError("Required");
                }else if(isEmptyInput(addActivityName)){
                    addActivityName.setError("Required");
                } else {
                    numSteps += Integer.parseInt((addStepsTextview.getText().toString()));
                    updateProgressBar();
                    walks.add(0,new Walk(addActivityName.getText().toString(),Integer.parseInt(addStepsTextview.getText().toString())));
                    adapter.notifyItemInserted(0);
                }
            }
        });
        return view;
    }

    public static void updateProgressBar(){
        prog = (((double) numSteps / (double)goal) * 100);
        progressBar.setProgress((int)prog,true);
        displayCurrentSteps.setText(MessageFormat.format("{0} / {1}",String.valueOf(numSteps), goal ));
        displayPercentage.setText(MessageFormat.format("{0} %",String.valueOf(Math.round(prog))));
    }
    private boolean isEmptyInput (EditText editText){
        return editText.getText().toString().length() == 0;
    }
    public static int getNumSteps() {
        return numSteps;
    }
    public static void setNumSteps(int numSteps) {
        fragment_home.numSteps = numSteps;
    }
    public static void setProg(double prog) {
        fragment_home.prog = prog;
    }
    public static int getGoal() {
        return goal;
    }

}
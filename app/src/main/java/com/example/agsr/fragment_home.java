package com.example.agsr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    WalkingActivity adapter;
    private int prog = 0;
    private int goal = 10000;
//    private MessageFormat stepsTemplate;
    private String mParam2;

    public fragment_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment fragment_home.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<Walks> walks = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.walking_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new WalkingActivity(this.getContext(), walks);
        recyclerView.setAdapter(adapter);

        EditText addStepsTextview = view.findViewById(R.id.steps_input);
        EditText addActivityName = view.findViewById(R.id.activity_name_input);
        TextView displayCurrentSteps = view.findViewById(R.id.text_view_steps_progress);
        TextView displayPercentage = view.findViewById(R.id.text_view_progress_percentage);
        MaterialButton addStepsButton = view.findViewById(R.id.add_steps_button);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        displayCurrentSteps.setText(MessageFormat.format("{0} / {1}",String.valueOf(prog), goal ));
        double progress = ((double) prog / (double)goal) * 100;
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
                    prog += Integer.parseInt((addStepsTextview.getText().toString()));
                    displayCurrentSteps.setText(MessageFormat.format("{0} / {1}",String.valueOf(prog), goal ));
                    double progress = ((double) prog / (double)goal) * 100;
                    progressBar.setProgress((int)progress);
                    displayPercentage.setText(MessageFormat.format("{0} %",String.valueOf(Math.round(progress))));
                    walks.add(0,new Walks(addActivityName.getText().toString(),Integer.parseInt(addStepsTextview.getText().toString())));
                    adapter.notifyItemInserted(0);
                }
            }
        });
        return view;
    }

    private boolean isEmptyInput (EditText editText){
        return editText.getText().toString().length() == 0;
    }

}
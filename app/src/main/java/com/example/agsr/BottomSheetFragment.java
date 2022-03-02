package com.example.agsr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    SharedPreferences.Editor prefs;
    SwitchMaterial toggleHistory;
    SwitchMaterial toggleGoals;

    public BottomSheetFragment() {
        // Required empty public constructor
    }
    public static BottomSheetFragment newInstance(String param1, String param2) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("AGSR", Context.MODE_PRIVATE);
        prefs = sharedPreferences.edit();
        prefs.putBoolean("historyToggle",toggleHistory.isChecked());
        prefs.putBoolean("goalsToggle", toggleGoals.isChecked());
        prefs.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("AGSR", Context.MODE_PRIVATE);
        toggleHistory.setChecked(sharedPreferences.getBoolean("historyToggle",false));
        toggleGoals.setChecked(sharedPreferences.getBoolean("goalsToggle",false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        toggleHistory = view.findViewById(R.id.toggle_history);
        toggleGoals = view.findViewById(R.id.toggle_goals);

//        toggleHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Button editHistory = view.findViewById(R.id.edit_history_button);
//                if(b){
//                    editHistory.setVisibility(View.VISIBLE);
//                }else{
//                    editHistory.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        return view;
    }
}
package com.example.agsr;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class fragment_calendar extends Fragment {

    public static HistoryViewModel historyViewModel;
    private RecyclerView recyclerView;
    HistoryAdapter adapter;
    String currentDate;

    public fragment_calendar() {
        // Required empty public constructor
    }

    public static fragment_calendar newInstance(String param1, String param2) {
        fragment_calendar fragment = new fragment_calendar();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);
        recyclerView = view.findViewById(R.id.history_recycler_view);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        TextView historyTargetTitle = view.findViewById(R.id.history_target_title);
        TextView historyTargetSteps = view.findViewById(R.id.history_target_steps);
        TextView historyGoalCompleted = view.findViewById(R.id.history_goal_completed);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = dateFormat.format(calendarView.getDate());
        System.out.println(calendarView.getDate());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter(new HistoryAdapter.TargetDiff());
        recyclerView.setAdapter(adapter);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        historyViewModel.getHistory().observe(getViewLifecycleOwner(), histories -> adapter.submitList(histories));

//        calendarView.set

        return view;
    }
}
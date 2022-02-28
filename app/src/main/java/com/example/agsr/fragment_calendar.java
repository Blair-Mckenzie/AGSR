package com.example.agsr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class fragment_calendar extends Fragment {

    public static HistoryViewModel historyViewModel;
    private RecyclerView recyclerView;
    static HistoryAdapter adapter;
    String currentDate;
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


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
        List<History> currentList = adapter.getCurrentList();
        if(currentList.size() >1){
            for(int i =0;i<currentList.size()-1;i++){
                historyViewModel.delete(currentList.get(i));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);
        recyclerView = view.findViewById(R.id.history_recycler_view);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        TextView historyTargetTitle = view.findViewById(R.id.history_target_title);
        TextView historyTargetSteps = view.findViewById(R.id.history_target_steps);
        TextView historyGoalCompleted = view.findViewById(R.id.history_goal_completed);
        Button deleteHistory = view.findViewById(R.id.delete_history_button);
        Button editHistory = view.findViewById(R.id.edit_history_button);

        currentDate = dateFormat.format(calendarView.getDate());
        System.out.println(calendarView.getDate());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter(new HistoryAdapter.TargetDiff());
        recyclerView.setAdapter(adapter);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        historyViewModel.getTodayHistory(currentDate).observe(getViewLifecycleOwner(),histories -> adapter.submitList(histories));

//        historyViewModel.getHistory().observe(getViewLifecycleOwner(), histories -> adapter.submitList(histories));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                historyViewModel.getTodayHistory(currentDate).removeObservers(getViewLifecycleOwner());
                month = month+1 ;
                String newDate;
                if(month <10){
                    newDate = (String.valueOf(day)+"/"+ "0"+String.valueOf(month)+"/"+String.valueOf(year));
                }else{
                    newDate = (String.valueOf(day)+"/"+ String.valueOf(month)+"/"+String.valueOf(year));
                }
                historyViewModel.getTodayHistory(newDate).observe(getViewLifecycleOwner(),histories -> adapter.submitList(histories));
            }
        });

        deleteHistory.setOnClickListener(view1 -> {
            historyViewModel.deleteAll();
        });

        editHistory.setOnClickListener(view1->{
            
        });
        return view;
    }

    public List<History> getHistoryList(){
        return adapter.getCurrentList();
    }
}
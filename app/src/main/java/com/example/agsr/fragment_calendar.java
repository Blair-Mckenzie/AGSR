package com.example.agsr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class fragment_calendar extends Fragment {

    public static HistoryViewModel historyViewModel;
    private RecyclerView recyclerView;
    static HistoryAdapter adapter;
    String currentDate;
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    boolean isHistoryToggled;
    SharedPreferences sharedPreferences;
    ArrayList<String> currentGoals;
    HashMap<String, Integer> mapOfTargets;
    String selectedTarget = "";
    Button editHistory;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

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
        if (currentList.size() > 1) {
            for (int i = 0; i < currentList.size() - 1; i++) {
                historyViewModel.delete(currentList.get(i));
            }
        }
        sharedPreferences = this.getActivity().getSharedPreferences("AGSR", Context.MODE_PRIVATE);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // Implementation
                isHistoryToggled = sharedPreferences.getBoolean("historyToggle", false);
                if (!isHistoryToggled) {
                    editHistory.setVisibility(View.INVISIBLE);
                } else {
                    editHistory.setVisibility(View.VISIBLE);
                }
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        Set<String> set = sharedPreferences.getStringSet("listOfTargets", null);
        currentGoals = new ArrayList<>(set);
        mapOfTargets = new HashMap<>();
        try {
            String jsonString = sharedPreferences.getString("mapOfTargets", (new JSONObject()).toString());
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String k = keysItr.next();
                Integer v = (Integer) jsonObject.get(k);
                mapOfTargets.put(k, v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        recyclerView = view.findViewById(R.id.history_recycler_view);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Button deleteHistory = view.findViewById(R.id.delete_history_button);
        editHistory = view.findViewById(R.id.edit_history_button);
        sharedPreferences = this.getActivity().getSharedPreferences("AGSR", Context.MODE_PRIVATE);
        isHistoryToggled = sharedPreferences.getBoolean("historyToggle", false);
//        if (isHistoryToggled) {
//            editHistory.setVisibility(View.VISIBLE);
//        } else {
        editHistory.setVisibility(View.INVISIBLE);
//        }
        currentDate = dateFormat.format(calendarView.getDate());
        System.out.println(calendarView.getDate());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter(new HistoryAdapter.TargetDiff());
        recyclerView.setAdapter(adapter);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        historyViewModel.getTodayHistory(currentDate).observe(getViewLifecycleOwner(), histories -> adapter.submitList(histories));

        calendarView.setMaxDate(calendarView.getDate());

        calendarView.setOnDateChangeListener((calendarView1, year, month, day) -> {
            historyViewModel.getTodayHistory(currentDate).removeObservers(getViewLifecycleOwner());
            isHistoryToggled = sharedPreferences.getBoolean("historyToggle", false);

            month += 1;
            String newDate;
            if (day < 10 && month < 10) {
                newDate = "0" + day + "/" + "0" + month + "/" + year;
            } else if (day < 10) {
                newDate = "0" + day + "/" + month + "/" + year;
            } else if (month < 10) {
                newDate = day + "/" + "0" + month + "/" + year;
            } else {
                newDate = day + "/" + month + "/" + year;
            }
            if (!isHistoryToggled || newDate.equals(dateFormat.format(calendarView1.getDate()))) {
                editHistory.setVisibility(View.INVISIBLE);
            } else {
                editHistory.setVisibility(View.VISIBLE);
            }
            currentDate = newDate;
            historyViewModel.getTodayHistory(newDate).observe(getViewLifecycleOwner(), histories -> adapter.submitList(histories));
        });

        deleteHistory.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.confirm_delete_popup, (ViewGroup) getView(), false);
            TextView confrimDelete = viewInflated.findViewById(R.id.delete_popup);
            builder.setView(viewInflated);
            String message = "Are you sure you wish to delete all history (including today)";
            confrimDelete.setText(message.toString());
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    historyViewModel.deleteAll();
                    fragment_home.walkViewModel.deleteAll();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        });
        editHistory.setOnClickListener(view1 -> {
            if (adapter.getCurrentList().size() == 0) {
//                historyViewModel.insert(new History(currentDate,"Default",3000,8000));
                showPopupDialog("Add Activity", 'a', view1, 0);
            } else {
                showPopupDialog("Edit Activity", 'e', view1, 0);
            }
        });
        return view;
    }

    /**
     * @param title    title of the popup dialog box
     * @param type     type of action to be displayed in the popup
     * @param view     view to be inflated
     * @param position position of the adapter
     */
    private void showPopupDialog(String title, char type, View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_history_popup, (ViewGroup) getView(), false);
        Spinner targetList = viewInflated.findViewById(R.id.targets_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, currentGoals);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetList.setAdapter(arrayAdapter);
        targetList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTarget = currentGoals.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        EditText historyStepsInput = viewInflated.findViewById(R.id.history_steps_input);
        TextView historyTargetTitle = viewInflated.findViewById(R.id.history_target_title);

        if (type == 'e') {
            View v = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(0)).itemView;
            TextView historyTargetSteps = v.findViewById(R.id.history_completed_steps);
            String currentStepsDisplay = historyTargetSteps.getText().toString();
            historyStepsInput.setText(currentStepsDisplay.substring(15));
            builder.setView(viewInflated);
        }
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!(checkDialogEmpty(historyStepsInput))) {
                dialog.dismiss();
//                String newTitle = titleInput.getText().toString().trim();
                if (type == 'e') {
                    History history = adapter.getCurrentList().get(0);
                    int id = history.getId();
                    History updatedHistory = new History(currentDate, selectedTarget, mapOfTargets.get(selectedTarget), Integer.parseInt(historyStepsInput.getText().toString()));
                    updatedHistory.setId(id);
                    historyViewModel.update(updatedHistory);
                } else {
                    historyViewModel.insert(new History(currentDate, selectedTarget, mapOfTargets.get(selectedTarget), Integer.parseInt(historyStepsInput.getText().toString())));
                }
            }
        });
    }
//    }

    private boolean checkDialogEmpty(EditText numSteps) {
        if (numSteps.getText().toString().length() == 0) {
            numSteps.setError("Required");
            return true;
        }
        return false;
    }

}
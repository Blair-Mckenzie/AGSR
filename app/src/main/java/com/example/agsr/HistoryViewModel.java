package com.example.agsr;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private AGSRRepository repository;
    private final LiveData<List<History>> allHistory;
    private final LiveData<List<History>> todayHistory;

    public HistoryViewModel(Application application) {
        super(application);
        repository = new AGSRRepository(application);
        allHistory = repository.getAllHistory();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        todayHistory = repository.getTodayHistory(date);
    }

    LiveData<List<History>> getHistory(){
        return allHistory;
    }

    LiveData<List<History>> getTodayHistory(String date){
        return repository.getTodayHistory(date);
    }
    public void deleteAll(){
        repository.deleteAll();
    }
    public void insert(History history){
        repository.insert(history);
    }
    public void delete(History history){
        repository.delete(history);
    }
    public void update(History history){
        repository.update(history);
    }
}

package com.example.agsr;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private AGSRRepository repository;
    private final LiveData<List<History>> allHistory;

    public HistoryViewModel(Application application) {
        super(application);
        repository = new AGSRRepository(application);
        allHistory = repository.getAllHistory();
    }

    LiveData<List<History>> getHistory(){
        return allHistory;
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

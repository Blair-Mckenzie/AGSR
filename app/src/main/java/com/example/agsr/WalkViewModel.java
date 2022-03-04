package com.example.agsr;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WalkViewModel extends AndroidViewModel {
    private AGSRRepository repository;
    private final LiveData<List<Walk>> allWalks;
    public WalkViewModel(@NonNull Application application) {
        super(application);
        repository = new AGSRRepository(application);
        allWalks = repository.getAllWalks();
    }

    LiveData<List<Walk>> getAllWalks(){
        return allWalks;
    }

    public void insert(Walk walk){
        repository.insert(walk);
    }

    public void delete(Walk walk){
        repository.delete(walk);
    }

    public void deleteAll(){repository.deleteAllWalks();}

}

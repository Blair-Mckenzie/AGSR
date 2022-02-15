package com.example.agsr;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TargetViewModel extends AndroidViewModel {
    private AGSRRepository repository;
    private final LiveData<List<Target>> allTargets;

    public TargetViewModel(Application application) {
        super(application);
        repository = new AGSRRepository(application);
        allTargets = repository.getAllTargets();
    }

    LiveData<List<Target>> getAllTargets(){
        return allTargets;
    }

    public void insert(Target target){
        repository.insert(target);
    }

    public void delete(Target target){
        repository.delete(target);
    }

    public void update(Target target){
        repository.update(target);
    }
}

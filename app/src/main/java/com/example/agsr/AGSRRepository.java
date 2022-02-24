package com.example.agsr;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AGSRRepository {

    private TargetDao targetDao;
    private LiveData<List<Target>> allTargets;

    private WalkDao walkDao;
    private LiveData<List<Walk>> allWalks;

    AGSRRepository(Application application) {
        AGSRDatabase db = AGSRDatabase.getDatabase(application);
        walkDao = db.walkDao();
        allWalks = walkDao.getWalks();
        targetDao = db.targetDao();
        allTargets = targetDao.getTargets();
    }

    LiveData<List<Target>> getAllTargets() {
        return allTargets;
    }

    void insert(Target target) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> targetDao.insert(target));
    }
    void update(Target target) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> targetDao.update(target));
    }
    void delete(Target target) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> targetDao.delete(target));
    }

    LiveData<List<Walk>> getAllWalks(){
        return allWalks;
    }
    void insert(Walk walk) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> walkDao.insert(walk));
    }
    void delete(Walk walk) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> walkDao.delete(walk));
    }

}

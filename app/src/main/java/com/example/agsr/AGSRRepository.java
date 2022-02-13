package com.example.agsr;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AGSRRepository {

    private TargetDao targetDao;
    private LiveData<List<Target>> allTargets;

    AGSRRepository(Application application) {
        AGSRDatabase db = AGSRDatabase.getDatabase(application);
        targetDao = db.targetDao();
        allTargets = targetDao.getTargets();
    }

    LiveData<List<Target>> getAllTargets() {
        return allTargets;
    }

    void insert(Target target) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> {
            targetDao.insert(target);
        });
    }

    void delete(Target target) {
       new deleteTargetAsyncTask(targetDao).execute(target);
    }

    private static class deleteTargetAsyncTask extends AsyncTask<Target, Void, Void> {
        private TargetDao mAsyncTaskDao;

        deleteTargetAsyncTask(TargetDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Target... targets) {
            mAsyncTaskDao.delete(targets[0]);
            return null;
        }
    }
}

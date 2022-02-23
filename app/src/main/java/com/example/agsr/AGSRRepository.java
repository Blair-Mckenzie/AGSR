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
        AGSRDatabase.databaseWriteExecutor.execute(() -> {
            targetDao.insert(target);
        });
    }

    void delete(Target target) {
       new deleteTargetAsyncTask(targetDao).execute(target);
    }

    void update(Target target) {
        new updateTargetAsyncTask(targetDao).execute(target);
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

    private static class updateTargetAsyncTask extends AsyncTask<Target, Void, Void> {
        private TargetDao mAsyncTaskDao;

        updateTargetAsyncTask(TargetDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Target... targets) {
            mAsyncTaskDao.update(targets[0]);
            return null;
        }
    }


    LiveData<List<Walk>> getAllWalks(){
        return allWalks;
    }
    void insert(Walk walk) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> {
            walkDao.insert(walk);
        });
    }

    void delete(Walk walk) {
        new deleteWalkAsyncTask(walkDao).execute(walk);
    }

    void update(Walk walk) {
        new updateWalkAsyncTask(walkDao).execute(walk);
    }

    private static class deleteWalkAsyncTask extends AsyncTask<Walk, Void, Void> {
        private WalkDao mAsyncTaskDao;

        deleteWalkAsyncTask(WalkDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Walk... walks) {
            mAsyncTaskDao.delete(walks[0]);
            return null;
        }
    }

    private static class updateWalkAsyncTask extends AsyncTask<Walk, Void, Void> {
        private WalkDao mAsyncTaskDao;

        updateWalkAsyncTask(WalkDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Walk... walks) {
            mAsyncTaskDao.update(walks[0]);
            return null;
        }
    }
}

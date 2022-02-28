package com.example.agsr;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AGSRRepository {

    private final TargetDao targetDao;
    private final LiveData<List<Target>> allTargets;

    private final WalkDao walkDao;
    private final LiveData<List<Walk>> allWalks;

    private final HistoryDao historyDao;
    private final LiveData<List<History>> allHistory;
    private final LiveData<List<History>> todayHistory;
    private String date;

    AGSRRepository(Application application) {
        AGSRDatabase db = AGSRDatabase.getDatabase(application);
        walkDao = db.walkDao();
        allWalks = walkDao.getWalks();
        targetDao = db.targetDao();
        allTargets = targetDao.getTargets();
        historyDao = db.historyDao();
        allHistory = historyDao.getHistory();
        todayHistory = historyDao.getHistoryDate(date);
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

    LiveData<List<History>> getAllHistory() {
        return allHistory;
    }

    LiveData<List<History>> getTodayHistory(String date){
        return historyDao.getHistoryDate(date);
    }
    void insert(History history) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> historyDao.insert(history));
    }
    void update(History history) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> historyDao.update(history));
    }
    void delete(History history) {
        AGSRDatabase.databaseWriteExecutor.execute(() -> historyDao.delete(history));
    }
    void deleteAll(){
        AGSRDatabase.databaseWriteExecutor.execute(historyDao::deleteAll);
    }


}

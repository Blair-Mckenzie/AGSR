package com.example.agsr;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Target.class, Walk.class,History.class},version = 5,exportSchema = false)
public abstract class AGSRDatabase  extends RoomDatabase {
    public abstract TargetDao targetDao();
    public abstract WalkDao walkDao();
    public abstract HistoryDao historyDao();

    private static volatile AGSRDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 6;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AGSRDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (AGSRDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AGSRDatabase.class, "AGSR_database").addCallback(sRoomDatabaseCallback).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

        private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                    TargetDao dao = INSTANCE.targetDao();
                    Target [] allTargets = dao.getAllTargets();
                    if(allTargets.length == 0){
                        Target defaultTarget = new Target("Default",10000,true);
                        dao.insert(defaultTarget);
                    }
            });
        }
    };
}

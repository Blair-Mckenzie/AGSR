package com.example.agsr;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Target.class, Walk.class,History.class},version = 4,exportSchema = false)
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

////            // If you want to keep data through app restarts,
////            // comment out the following block
            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
                    TargetDao dao = INSTANCE.targetDao();
                    HistoryDao dao1 = INSTANCE.historyDao();
                    Target [] allTargets = dao.getAllTargets();
                    if(allTargets.length == 0){
                        Target defaultTarget = new Target("Default",10000,true);
                        dao.insert(defaultTarget);
                    }
//                    dao1.deleteAll();
//                    dao1.insert(new History("27/02/2022","Default",10000,1000));
//                    dao1.insert(new History("26/02/2022","Default",5000,1000));
//                    dao1.insert(new History("23/02/2022","Default",2000,1000));


////                dao.deleteAll();
////
////                Target target1 = new Target("Ambitious",10000,false);
////                dao.insert(target1);
////                Target target2 = new Target("Lazy as fuck",1000,false);
////                dao.insert(target2);
            });
        }
    };
}

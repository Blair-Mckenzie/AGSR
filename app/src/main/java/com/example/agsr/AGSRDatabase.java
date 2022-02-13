package com.example.agsr;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Target.class},version = 1,exportSchema = false)
public abstract class AGSRDatabase  extends RoomDatabase {
    public abstract TargetDao targetDao();

    private static volatile AGSRDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AGSRDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (AGSRDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AGSRDatabase.class, "target_database").build();
                }
            }
        }
        return INSTANCE;
    }

//    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//
//            // If you want to keep data through app restarts,
//            // comment out the following block
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                TargetDao dao = INSTANCE.targetDao();
//                dao.deleteAll();
//
//                Target target1 = new Target("Ambitious",10000,false);
//                dao.insert(target1);
//                Target target2 = new Target("Lazy as fuck",1000,false);
//                dao.insert(target2);
//            });
//        }
//    };
}

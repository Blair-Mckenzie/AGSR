package com.example.agsr;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Target.class, Walk.class},version = 2,exportSchema = false)
public abstract class AGSRDatabase  extends RoomDatabase {
    public abstract TargetDao targetDao();
    public abstract WalkDao walkDao();

    private static volatile AGSRDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AGSRDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (AGSRDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AGSRDatabase.class, "AGSR_database").build();
                }
            }
        }
        return INSTANCE;
    }
}

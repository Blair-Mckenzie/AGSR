package com.example.agsr;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TargetDao {

    @Insert
    void insert(Target target);

    @Delete
    void delete(Target target);

    @Update
    void update(Target target);

    @Query("SELECT * FROM target_table")
    LiveData<List<Target>> getTargets();
}

package com.example.agsr;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WalkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Walk walk);

    @Delete
    void delete(Walk walk);

    @Query("SELECT * FROM walk_table")
    LiveData<List<Walk>> getWalks();

}

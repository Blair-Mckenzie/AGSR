package com.example.agsr;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WalkDao {

    @Insert
    void insert(Walk walk);

    @Delete
    void delete(Walk walk);

    @Query("DELETE FROM walk_table")
    void deleteAll();

    @Query("SELECT * FROM walk_table")
    LiveData<List<Walk>> getWalks();

}

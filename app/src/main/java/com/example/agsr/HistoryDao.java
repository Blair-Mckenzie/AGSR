package com.example.agsr;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(History history);

    @Delete
    void delete(History history);

    @Update
    void update(History history);

    @Query("SELECT * FROM history_table")
    LiveData<List<History>> getHistory();

    @Query("DELETE FROM history_table")
    void deleteAll();

    @Query("SELECT * FROM history_table where date = :date")
    LiveData<List<History>> getHistoryDate(String date);

    @Query("SELECT * FROM history_table")
    public History[] getAllHistory();

}

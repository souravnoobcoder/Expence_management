package com.example.expence_management.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert
    void insert(DataItems items);

    @Delete
    void delete(DataItems items);

    @Query("SELECT * FROM EXPENSES ORDER BY DATE")
    LiveData<List<DataItems>> getAllAscending();

    @Query("SELECT *FROM EXPENSES ORDER BY DATE DESC")
    LiveData<List<DataItems>> getAllDescending();
}

package com.example.expence_management.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert(onConflict =OnConflictStrategy.REPLACE)
    void insert(DataItems items);

    @Delete
    void delete(DataItems items);

    @Update
    void update(DataItems item);

    @Query("UPDATE EXPENSES SET DATE =:uDate WHERE date =:date")
    void updateDate(long uDate,long date);

    @Query("SELECT date FROM EXPENSES ORDER BY DATE")
    List<Long> getAllDate();

    @Query("SELECT COUNT(date) FROM EXPENSES")
    Integer getCount();

    @Query("SELECT * FROM EXPENSES ORDER BY DATE")
    LiveData<List<DataItems>> getAllAscending();

    @Query("SELECT *FROM EXPENSES ORDER BY DATE DESC")
    LiveData<List<DataItems>> getAllDescending();

    @Query("select *from expenses where date=:date ")
    LiveData<DataItems> getRow(long date);

    @Query("select *from expenses where date=:date ")
    DataItems getRoww(long date);
}

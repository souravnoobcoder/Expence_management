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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DataItems items);

    @Delete
    void delete(DataItems items);

    @Update
    void update(DataItems item);

    @Query("UPDATE EXPENSES SET DATE =:uDate WHERE ID =:id")
    void updateDate(String uDate,int id);

    @Query("UPDATE EXPENSES SET MONEYGOT =:moneyList,GROSSMONEYGOT=:money WHERE ID =:id")
    void updateMoneyGotList(int id,List<Integer> moneyList,int money);

    @Query("UPDATE EXPENSES SET MONEYEXPENSE =:moneyList,GROSSMONEYEXPENSE =:money WHERE ID =:id")
    void updateMoneyExpenseList(int id,List<Integer> moneyList,int money);

    @Query("UPDATE EXPENSES SET MONEYGOTPURPOSES =:moneyDescriptionList WHERE ID =:id")
    void updateMoneyGotDescriptionList(int id,List<String> moneyDescriptionList);

    @Query("UPDATE EXPENSES SET MONEYEXPENSEPURPOSES =:moneyDescriptionList WHERE ID =:id")
    void updateMoneyExpenseDescriptionList(int id, List<String> moneyDescriptionList);

    @Query("SELECT * FROM EXPENSES ORDER BY DATE")
    LiveData<List<DataItems>> getAllAscending();

    @Query("SELECT *FROM EXPENSES ORDER BY DATE DESC")
    LiveData<List<DataItems>> getAllDescending();

    @Query("select *from expenses where  id=:id")
    LiveData<DataItems> getRow(int id);
}

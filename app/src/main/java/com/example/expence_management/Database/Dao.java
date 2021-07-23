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

    @Query("UPDATE EXPENSES SET DATE =:uDate WHERE DATE =:pDate")
    void updateDate(String uDate,String pDate);

    @Query("UPDATE EXPENSES SET MONEYGOT =:moneyList,GROSSMONEYGOT=:money WHERE DATE =:date")
    void updateMoneyGotList(String date,List<Integer> moneyList,int money);

    @Query("UPDATE EXPENSES SET MONEYEXPENSE =:moneyList,GROSSMONEYEXPENSE =:money WHERE DATE =:date")
    void updateMoneyExpenseList(String date,List<Integer> moneyList,int money);

    @Query("UPDATE EXPENSES SET MONEYGOTPURPOSES =:moneyDescriptionList WHERE DATE =:date")
    void updateMoneyGotDescriptionList(String date,List<String> moneyDescriptionList);

    @Query("UPDATE EXPENSES SET MONEYEXPENSEPURPOSES =:moneyDescriptionList WHERE DATE =:date")
    void updateMoneyExpenceDescriptionList(String date,List<String> moneyDescriptionList);

    @Query("SELECT * FROM EXPENSES ORDER BY DATE")
    LiveData<List<DataItems>> getAllAscending();

    @Query("SELECT *FROM EXPENSES ORDER BY DATE DESC")
    LiveData<List<DataItems>> getAllDescending();
}

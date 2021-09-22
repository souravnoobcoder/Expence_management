package com.example.expense_management.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: DataItems?)

    @Delete
    suspend fun delete(items: DataItems?)

    @Update
    suspend fun update(item: DataItems?)

    @Query("UPDATE EXPENSES SET DATE =:uDate WHERE date =:date")
    suspend fun updateDate(uDate: Long, date: Long)

    @Query("SELECT date FROM EXPENSES ORDER BY DATE")
    suspend fun  allDate(): List<Long>

    @Query("SELECT COUNT(date) FROM EXPENSES")
    fun  count(): Int?

    @Query("SELECT * FROM EXPENSES ORDER BY grossMoneyExpense")
    fun dataExpenseAscending() : LiveData<List<DataItems>>

    @Query("SELECT * FROM EXPENSES ORDER BY grossMoneyGot")
    fun dataGotAscending() : LiveData<List<DataItems>>

    @Query("SELECT * FROM EXPENSES ORDER BY grossMoneyExpense DESC")
    fun dataExpenseDescending() : LiveData<List<DataItems>>

    @Query("SELECT * FROM EXPENSES ORDER BY grossMoneyGot DESC")
    fun dataGotDescending() : LiveData<List<DataItems>>

    @Query("SELECT * FROM EXPENSES ORDER BY DATE DESC")
    fun dataDescending() : LiveData<List<DataItems>>


    @Query("SELECT * FROM EXPENSES ORDER BY DATE")
    fun dataAscending() : LiveData<List<DataItems>>

    @Query("select *from expenses where date=:date ")
    fun getRow(date: Long): LiveData<DataItems?>?

    @Query("select *from expenses where date=:date ")
    suspend fun getRoww(date: Long?): DataItems?

    @Query("SELECT Sum(grossMoneyExpense) FROM EXPENSES WHERE date BETWEEN :first And :last ")
    suspend fun getExpense(first: Long, last: Long): Int

    @Query("SELECT Sum(grossMoneyGot) FROM EXPENSES WHERE date BETWEEN :first And :last ")
    suspend fun getGain(first: Long, last: Long): Int
}
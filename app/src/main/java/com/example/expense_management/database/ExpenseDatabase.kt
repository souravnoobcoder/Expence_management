package com.example.expense_management.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DataItems::class], version = 1, exportSchema = false)
@TypeConverters(
    DataConverter::class
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun dao(): Dao?

    companion object {
        var INSTANCE: ExpenseDatabase? = null
        private const val DATABASE_NAME = "expense.database"
        fun getDbINSTANCE(context: Context?): ExpenseDatabase? {
            INSTANCE = Room.databaseBuilder(
                context!!.applicationContext,
                ExpenseDatabase::class.java,
                DATABASE_NAME
            )
                .build()
            return INSTANCE
        }

        fun deleteInstance() {
            if (INSTANCE != null) INSTANCE = null
        }
    }
}
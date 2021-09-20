package com.example.expense_management.database;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {DataItems.class},version = 1,exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class myDatabase extends RoomDatabase {
    public abstract Dao Dao();
    public static myDatabase INSTANCE;
    public static final String DATABASE_NAME="expense.database";
    public static myDatabase getDbINSTANCE(@Nullable Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),myDatabase.class,DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }
    public static void DELETE_INSTANCE(){
        if (INSTANCE!=null)
            INSTANCE=null;
    }
}

package com.example.expence_management.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class DataItems {
    @PrimaryKey(autoGenerate = true)
    private int id;


}

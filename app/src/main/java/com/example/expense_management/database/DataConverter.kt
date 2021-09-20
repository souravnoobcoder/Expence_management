package com.example.expense_management.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class DataConverter : Serializable {
    @TypeConverter
    fun formmoneyExpense(list: ArrayList<Int?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Int?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun tomoneyExpense(moneyExpenseString: String?): ArrayList<Int>? {
        if (moneyExpenseString == null) return null
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Int?>?>() {}.type
        return gson.fromJson(moneyExpenseString, type)
    }

    @TypeConverter
    fun formoneyExpensePurposes(purposeList: ArrayList<String?>?): String? {
        if (purposeList == null) return null
        val gson = Gson()
        val type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.toJson(purposeList, type)
    }

    @TypeConverter
    fun tomoneyExpensePurposes(string: String?): ArrayList<String>? {
        if (string == null) return null
        val gson = Gson()
        val type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(string, type)
    }
}
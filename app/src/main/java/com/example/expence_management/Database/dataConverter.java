package com.example.expence_management.Database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class dataConverter implements Serializable {
    @TypeConverter
    public String formmoneyExpense(List<Integer> list){
        if (list==null){
            return null;
        }
        Gson gson=new Gson();
        Type type=new TypeToken<List<Integer>>(){
        }.getType();
        return gson.toJson(list,type);
    }

    @TypeConverter
    public List<Integer> tomoneyExpense(String moneyExpenseString){
        if (moneyExpenseString==null)
            return null;
        Gson gson=new Gson();
        Type type=new TypeToken<List<Integer>>(){
        }.getType();
        return gson.fromJson(moneyExpenseString,type);
    }
    @TypeConverter
   public String formoneyExpensePurposes(List<String> purposeList){
        if (purposeList==null)
            return null;
        Gson gson=new Gson();
        Type type=new TypeToken<List<String>>(){
        }.getType();
        return gson.toJson(purposeList,type);
    }
    @TypeConverter
    public List<String> tomoneyExpensePurposes(String string){
        if (string==null)
            return null;
        Gson gson=new Gson();
        Type type=new TypeToken<List<String>>(){
        }.getType();
        return gson.fromJson(string,type);
    }
}

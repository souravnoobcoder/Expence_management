package com.example.expense_management.Database;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class DataViewModel extends AndroidViewModel {
    private Repo repository;
    private LiveData<List<DataItems>> liveDataList;

    public DataViewModel(@Nullable Application application) {
        super(application);
        this.repository=new Repo(application);
        this.liveDataList=this.repository.getDataAscending();
    }
    public LiveData<List<DataItems>> getAllDataAscending(){
        this.liveDataList=this.repository.getDataAscending();
        return liveDataList;
    }
    public LiveData<List<DataItems>> getAllDataDescending(){
        this.liveDataList=this.repository.getDataDescending();
        return liveDataList;
    }
    public LiveData<List<DataItems>> getAllExpenseDataDescending(){
        this.liveDataList=this.repository.getDataExpenseDescending();
        return liveDataList;
    }
    public LiveData<List<DataItems>> getAllGotDataDescending(){
        this.liveDataList=this.repository.getDataGotDescending();
        return liveDataList;
    }
    public LiveData<List<DataItems>> getAllExpenseDataAscending(){
        this.liveDataList=this.repository.getDataExpenseAscending();
        return liveDataList;
    }
    public LiveData<List<DataItems>> getAllGotDataAscending(){
        this.liveDataList=this.repository.getDataGotAscending();
        return liveDataList;
    }
    public void insertData(DataItems items){
        this.repository.insertData(items);
    }
    public void deleteData(DataItems items){
        this.repository.deleteData(items);
    }
    public void updateDate(long uDate,long date){
        this.repository.updateDate(uDate,date);
    }
    public void update(DataItems items){
        this.repository.update(items);
    }
    public LiveData<DataItems> getRow(long date){
       return this.repository.getRow(date);
    }
}

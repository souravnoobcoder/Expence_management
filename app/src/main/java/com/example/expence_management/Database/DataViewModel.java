package com.example.expence_management.Database;

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
    public void insertData(DataItems items){
        this.repository.insertData(items);
    }
    public void deleteData(DataItems items){
        this.repository.deleteData(items);
    }
    public void updateDate(long uDate,long date){
        this.repository.updateDate(uDate,date);
    }
//    public void updateMoneyGotList(int id,List<Integer> moneyList,int money){
//        this.repository.updateMoneyGotList(id,moneyList,money);
//    }
//    public void updateMoneyExpenseList(int id,List<Integer> moneyList,int money){
//        this.repository.updateMoneyExpenseList(id,moneyList,money);
//    }
//    public void updateMoneyGotDescriptionList(int id,List<String> moneyDescriptionList){
//        this.repository.updateMoneyGotDescriptionList(id,moneyDescriptionList);
//    }
//    public void updateMoneyExpenseDescriptionList(int id,List<String> moneyDescriptionList){
//        this.repository.updateMoneyExpenseDescriptionList(id,moneyDescriptionList);
//    }
    public void update(DataItems items){
        this.repository.update(items);
    }
    public LiveData<DataItems> getRow(long date){
       return this.repository.getRow(date);
    }
}

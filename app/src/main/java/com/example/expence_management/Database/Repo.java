package com.example.expence_management.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.expence_management.dataClasses.dataClassDates;
import com.example.expence_management.dataClasses.dataClassDescription;
import com.example.expence_management.dataClasses.dataClassMoney;

import java.util.List;

public class Repo {
    private Dao dao;
    private LiveData<List<DataItems>> liveData;
    public Repo(Application application){
        myDatabase database=myDatabase.getDbINSTANCE(application);
        this.dao=database.Dao();
        this.liveData=dao.getAllAscending();
    }
    public LiveData<List<DataItems>> getDataAscending(){
        return dao.getAllAscending();
    }
    public LiveData<List<DataItems>> getDataDescending(){
        return dao.getAllDescending();
    }
    public void insertData(DataItems dataItems){
        new insertAsyncTask(dao).execute(dataItems);
    }

    private class insertAsyncTask extends AsyncTask<DataItems,Void,Void> {
        private Dao AsyncTaskDao;

        private insertAsyncTask(Dao asyncTaskDao) {
            this.AsyncTaskDao = asyncTaskDao;
        }
        @Override
        protected Void doInBackground(DataItems... dataItems) {
            AsyncTaskDao.insert(dataItems[0]);
            return null;
        }
    }
    public void deleteData(DataItems dataItems){
        new deleteAsyncTask(dao).execute(dataItems);
    }

    private class deleteAsyncTask extends AsyncTask<DataItems,Void,Void> {
        private Dao AsyncTaskDao;

        private deleteAsyncTask(Dao asyncTaskDao) {
           this.AsyncTaskDao = asyncTaskDao;
        }
        @Override
        protected Void doInBackground(DataItems... dataItems) {
            AsyncTaskDao.delete(dataItems[0]);
            return null;
        }
    }
    public void updateDate(long date,long pDate){
        dataClassDates d=new dataClassDates(date,pDate);
        new updateDateAsyncTask(dao).execute(d);
    }

    private class updateDateAsyncTask extends AsyncTask<dataClassDates,Void,Void>{
        private Dao AsyncTaskDao;

        public updateDateAsyncTask(Dao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(dataClassDates... dataClasses) {
            AsyncTaskDao.updateDate(dataClasses[0].getuDate(),dataClasses[0].getpDate());
            return null;
        }
    }
    public void update(DataItems items){
      new update(dao).execute(items);
    }
    private class update extends AsyncTask<DataItems,Void,Void>{
        private Dao AsyncTaskDao;
        public update(Dao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }
        @Override
        protected Void doInBackground(DataItems... dataItems) {
            AsyncTaskDao.update(dataItems[0]);
            return null;
        }
    }
    public LiveData<DataItems> getRow(long date){
        return this.dao.getRow(date);
    }

}

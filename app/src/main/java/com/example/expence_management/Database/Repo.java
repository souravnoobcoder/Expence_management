package com.example.expence_management.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

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
}

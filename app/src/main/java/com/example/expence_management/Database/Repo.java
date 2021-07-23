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
    public void updateDate(String uDate,String date){
        dataClassDates d=new dataClassDates(uDate,date);
        new updateDateAsyncTask(dao).execute(d);
    }

    private class updateDateAsyncTask extends AsyncTask<dataClassDates,Void,Void>{
        private Dao AsyncTaskDao;

        public updateDateAsyncTask(Dao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(dataClassDates... dataClasses) {
            AsyncTaskDao.updateDate(dataClasses[0].getuDate(),dataClasses[0].getDate());
            return null;
        }
    }
    public void updateMoneyGotList(String date,List<Integer> moneyList,int money){
        dataClassMoney de=new dataClassMoney(date,moneyList,money);
        new updateMoneyGotAsyncTask(dao).execute(de);
    }

    private class updateMoneyGotAsyncTask extends AsyncTask<dataClassMoney,Void,Void>{
        private Dao AsyncTaskDao;

        public updateMoneyGotAsyncTask(Dao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(dataClassMoney... dataClass) {
            AsyncTaskDao.updateMoneyGotList(dataClass[0].getDate()
                    ,dataClass[0].getMoneyList(),dataClass[0].getMoney());
            return null;
        }
    }
    public void updateMoneyExpenseList(String date,List<Integer> moneyList,int money){
        dataClassMoney dataClassMoney=new dataClassMoney(date,moneyList,money);
        new updateMoneyExpenseListAsyncTask(dao).execute(dataClassMoney);
    }
    private class updateMoneyExpenseListAsyncTask extends AsyncTask<dataClassMoney,Void,Void>{
        private Dao AsyncTaskDao;

        public updateMoneyExpenseListAsyncTask(Dao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(dataClassMoney... dataClassMonies) {
            AsyncTaskDao.updateMoneyExpenseList(dataClassMonies[0].getDate()
                    ,dataClassMonies[0].getMoneyList(),dataClassMonies[0].getMoney());
            return null;
        }
    }
    public void updateMoneyGotDescriptionList(String date,List<String> moneyDescriptionList){
        dataClassDescription dataClassDescription=new dataClassDescription(date,moneyDescriptionList);
        new updateMoneyGotDescriptionListAsyncTask(dao).execute(dataClassDescription);
    }
    private class updateMoneyGotDescriptionListAsyncTask extends AsyncTask<dataClassDescription,Void,Void>{
        private Dao AsyncTaskDao;

        public updateMoneyGotDescriptionListAsyncTask(Dao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(dataClassDescription... dataClassDescriptions) {
            AsyncTaskDao.updateMoneyGotDescriptionList(dataClassDescriptions[0].getDate()
                    ,dataClassDescriptions[0].getMoneyDescriptionList());
            return null;
        }
    }
    public void updateMoneyExpenseDescriptionList(String date,List<String> moneyDescriptionList){
        dataClassDescription dataClassDescription=new dataClassDescription(date,moneyDescriptionList);
        new updateMoneyGotDescriptionListAsyncTask(dao).execute(dataClassDescription);
    }
    private class updateMoneyExpenseDescriptionListAsyncTask extends AsyncTask<dataClassDescription,Void,Void>{
        private Dao AsyncTaskDao;

        public updateMoneyExpenseDescriptionListAsyncTask(Dao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(dataClassDescription... dataClassDescriptions) {
            AsyncTaskDao.updateMoneyExpenceDescriptionList(dataClassDescriptions[0].getDate()
                    ,dataClassDescriptions[0].getMoneyDescriptionList());
            return null;
        }
    }
}

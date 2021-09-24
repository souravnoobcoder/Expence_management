package com.example.expense_management.database


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class DataViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val repository: Repo = Repo(application)
    private var liveDataList: LiveData<List<DataItems>>
    fun allDataAscending(): LiveData<List<DataItems>> {
        return repository.getDataAscending()
    }

    fun allDataDescending(): LiveData<List<DataItems>> {
        return repository.getDataDescending()
    }

    fun allExpenseDataDescending(): LiveData<List<DataItems>> {
        return repository.getDataExpenseDescending()
    }

    fun allGotDataDescending(): LiveData<List<DataItems>> {
        return repository.getDataGotDescending()
    }

    fun allExpenseDataAscending(): LiveData<List<DataItems>> {
        return repository.getDataExpenseAscending()
    }

    fun allGotDataAscending(): LiveData<List<DataItems>> {
        return repository.getDataGotAscending()
    }

    fun insertData(items: DataItems?) {
        repository.insertData(items!!)
    }

    fun deleteData(items: DataItems?) {
        repository.deleteData(items!!)
    }

    fun updateDate(uDate: Long, date: Long) {
        repository.updateDate(uDate, date)
    }

    fun update(items: DataItems?) {
        repository.update(items)
    }

    fun getRow(date: Long): LiveData<DataItems?>? {
        return repository.getRow(date)
    }

    init {
        liveDataList = repository.getDataAscending()
    }
}
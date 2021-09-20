package com.example.expense_management.database


import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class Repo(application: Application?) {
    private val dao: Dao
    private val liveData: LiveData<List<DataItems>>?

    fun getDataAscending(): LiveData<List<DataItems>> {
        return dao.dataAscending()
    }

    fun getDataDescending(): LiveData<List<DataItems>> {
        return dao.dataDescending()
    }

    fun getDataGotAscending(): LiveData<List<DataItems>> {
        return dao.dataGotAscending()
    }

    fun getDataExpenseAscending(): LiveData<List<DataItems>> {
        return dao.dataExpenseAscending()
    }

    fun getDataExpenseDescending(): LiveData<List<DataItems>> {
        return dao.dataExpenseDescending()
    }

    fun getDataGotDescending(): LiveData<List<DataItems>> {
        return dao.dataGotDescending()
    }

    fun insertData(dataItems: DataItems) {
        CoroutineScope(IO).launch { dao.insert(dataItems) }
    }

    fun deleteData(dataItems: DataItems) {
        CoroutineScope(IO).launch { dao.delete(dataItems) }
    }

    fun updateDate(date: Long, pDate: Long) {
        CoroutineScope(IO).launch { dao.updateDate(date, pDate) }
    }

    fun update(items: DataItems?) {
        CoroutineScope(IO).launch { dao.update(items) }

    }

    fun getRow(date: Long): LiveData<DataItems?>? {
        return dao.getRow(date)
    }

    init {
        val database = myDatabase.getDbINSTANCE(application)
        dao = database.Dao()
        liveData = dao.dataAscending()
    }
}
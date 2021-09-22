package com.example.expense_management.activities

import android.Manifest
import com.example.expense_management.dataClasses.psfs.checkDate
import com.example.expense_management.dataClasses.psfs.makeDate
import androidx.appcompat.app.AppCompatActivity
import com.example.expense_management.RecyclerViewAdapters.mainRecycleAdapter
import com.example.expense_management.database.DataItems
import com.google.android.material.datepicker.MaterialDatePicker
import com.example.expense_management.database.DataViewModel
import com.google.android.material.appbar.MaterialToolbar
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import com.example.expense_management.database.myDatabase
import android.widget.TextView
import android.widget.Toast
import android.content.DialogInterface
import android.widget.RadioButton
import android.os.Looper
import android.util.DisplayMetrics
import android.os.Environment
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Pair
import com.example.expense_management.R
import com.example.expense_management.activities.AddingToDatabase
import com.example.expense_management.activities.DetailedData
import com.example.expense_management.dataClasses.psfs.CHECK
import com.example.expense_management.dataClasses.psfs.DATA_ID
import com.example.expense_management.dataClasses.psfs.DATE_KEY
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.io.FileUtils
import java.io.File
import java.lang.Exception
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    var dialogBuilder: AlertDialog.Builder? = null
    var deleteDialogBuilder: AlertDialog.Builder? = null
    var sortAlertBuilder: AlertDialog.Builder? = null
    var dialog: AlertDialog? = null
    var deleteDialog: AlertDialog? = null
    var sortAlert: AlertDialog? = null
    private var adapter: mainRecycleAdapter? = null
    var items: DataItems? = null
    var doubleBackToExitPressedOnce = false
    private var materialDatePicker: MaterialDatePicker<Long>? = null
    private var datePickerForSearch: MaterialDatePicker<Long>? = null
    private var forMultiDates: MaterialDatePicker<Pair<Long, Long>>? = null
    var viewModel: DataViewModel? = null
    var expense = 0
    var got = 0
    lateinit var toolbar: MaterialToolbar
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.tool_bar)
        val mainListRecyclerView = findViewById<RecyclerView>(R.id.main_list_recycleView)
        mainListRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = mainRecycleAdapter()
        mainListRecyclerView.adapter = adapter
        val addingData = findViewById<FloatingActionButton>(R.id.adding_element)
        val layoutParams = CoordinatorLayout.LayoutParams(-2, -2)
        layoutParams.leftMargin = go().second - 200
        layoutParams.topMargin = go().first - 200
        addingData.layoutParams = layoutParams
        val builder = MaterialDatePicker.Builder.datePicker()
        materialDatePicker = builder.setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        viewModel!!.allDataDescending.observe(
            this,
            { dataItems: List<DataItems?>? -> setDataItemsList(dataItems) })
        datePickerForSearch = builder.setTitleText("Search by date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        val multiBuilder = MaterialDatePicker.Builder.dateRangePicker()
        forMultiDates = multiBuilder.setTitleText("Select multi dates")
            .build()
        addingData.setOnClickListener {
            materialDatePicker!!.show(
                this@MainActivity.supportFragmentManager,
                "Yes"
            )
        }
        materialDatePicker!!.addOnPositiveButtonClickListener { selection: Long ->
            val get = checkDate(selection, this@MainActivity)
            if (get < 0) {
                val intent = Intent(this@MainActivity, AddingToDatabase::class.java)
                intent.putExtra(DATE_KEY, selection)
                startActivity(intent)
            } else {
                makeAlertDailogbBox(selection)
                dialog!!.show()
            }
        }
        forMultiDates!!.addOnPositiveButtonClickListener { selection ->
            CoroutineScope(IO).launch { getSumDetails(selection) }
        }
        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
            val itemId = item.itemId
            when (itemId) {
                R.id.search_by_date -> {
                    datePickerForSearch!!.show(supportFragmentManager, "ust")
                }
                R.id.search_for_multi_days -> {
                    forMultiDates!!.show(supportFragmentManager, "get")
                }
                R.id.sort -> {
                    alertSort()
                }
                R.id.get_backup -> {
                    setBackupFirebase(R.id.get_backup)
                }
                R.id.set_backup -> {
                    setBackupFirebase(R.id.set_backup)
                }
            }
            false
        })
        adapter!!.setOnItemLongClickListener { dataItem: DataItems -> alertForDelete(dataItem) }
        adapter!!.setOnItemClickListener { data: DataItems ->
            val dataIntent = Intent(this@MainActivity, DetailedData::class.java)
            dataIntent.putExtra(DATA_ID, data.date)
            dataIntent.putExtra(CHECK, "yes")
            this@MainActivity.startActivity(dataIntent)
            finish()
        }
        datePickerForSearch!!.addOnPositiveButtonClickListener { selection: Long? ->
            CoroutineScope(IO).launch {
                    myDatabase.getDbINSTANCE(this@MainActivity).Dao().getRoww(selection)
                searchMain(selection)
            }
        }
    }
    private suspend fun searchMain(selection: Long?){
        CoroutineScope(Main).launch { search(selection) }
    }
    private suspend fun search(selection : Long?){
        if (items == null) {
            val layoutInflater = layoutInflater
            val layout =
                layoutInflater.inflate(R.layout.cusom_toast, findViewById(R.id.toast_custom))
            val textView = layout.findViewById<TextView>(R.id.tvtoast)
            textView.text = makeDate((selection)!!) + " Not found"
            textView.setTextColor(Color.rgb(0, 132, 219))
            textView.textSize = 20f
            val toast = Toast(applicationContext)
            toast.view = layout
            toast.duration = Toast.LENGTH_LONG
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } else {
            val intent = Intent(this@MainActivity, DetailedData::class.java)
            intent.putExtra(CHECK, "yes")
            intent.putExtra(DATA_ID, selection)
            startActivity(intent)
            finish()
        }
    }
    private suspend fun getSumDetails(selection : Pair<Long,Long>) {
        expense = myDatabase.getDbINSTANCE(this@MainActivity).Dao()
            .getExpense(selection.first, selection.second)
        got = myDatabase.getDbINSTANCE(this@MainActivity).Dao()
            .getGain(selection.first, selection.second)
        showSumAlertOnMainThread(expense,got,selection.first,selection.second)
    }

    private fun setDataItemsList(dataItems: List<DataItems?>?) {
        var dataItemsList: MutableList<DataItems?> = ArrayList()
        if (dataItemsList == null) dataItemsList = ArrayList()
        dataItemsList.clear()
        dataItemsList.addAll(dataItems!!)
        if (dataItemsList != null) adapter!!.setDataItemsList(dataItems)
        adapter!!.notifyDataSetChanged()
    }

    private fun makeAlertDailogbBox(longDate: Long) {
        dialogBuilder = AlertDialog.Builder(this@MainActivity)
        dialogBuilder!!.setTitle("Already in Database")
            .setPositiveButton("Update") { _: DialogInterface?, _: Int ->
                val intent = Intent(this@MainActivity, DetailedData::class.java)
                intent.putExtra(CHECK, "yes")
                intent.putExtra(DATA_ID, longDate)
                startActivity(intent)
                finish()
            }.setNegativeButton("Replace") { _: DialogInterface?, _: Int ->
                val intent = Intent(this@MainActivity, AddingToDatabase::class.java)
                intent.putExtra(DATE_KEY, longDate)
                startActivity(intent)
                finish()
            }
        dialog = dialogBuilder!!.create()
    }

   private fun alertForDelete(dataItem: DataItems) {
        deleteDialogBuilder = AlertDialog.Builder(this@MainActivity)
        deleteDialogBuilder!!.setTitle("Do you really want to delete ")
            .setMessage(
                """
${makeDate(dataItem.date)} date data"""
            )
            .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                viewModel!!.deleteData(dataItem)
                dateA()
            }
        deleteDialog = deleteDialogBuilder!!.create()
        deleteDialog!!.show()
    }

    private suspend fun showSumAlertOnMainThread(paid: Int, gain: Int, start: Long, last: Long){
        withContext(Main){
            showSumAlert(paid,gain,start,last)
        }
    }
    private fun showSumAlert(paid: Int, gain: Int, start: Long, last: Long) {
        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle(makeDate(start) + " to " + makeDate(last))
            .setMessage("Gross Gain = $gain\nGross Paid = $paid")
            .setPositiveButton("Ok", null).create()
        dialog.show()
    }

    private fun alertSort() {
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.sort_selection, null)
        val date = dialogView.findViewById<RadioButton>(R.id.by_date)
        val got = dialogView.findViewById<RadioButton>(R.id.by_gainMoney)
        val expense = dialogView.findViewById<RadioButton>(R.id.by_expense)
        val ascending = dialogView.findViewById<RadioButton>(R.id.ascending)
        val descending = dialogView.findViewById<RadioButton>(R.id.descending)
        sortAlertBuilder = AlertDialog.Builder(this@MainActivity)
        sortAlertBuilder!!.setView(dialogView)
            .setTitle("Sorting")
            .setPositiveButton("Ok")
            { _: DialogInterface?, _: Int -> if (date.isChecked && ascending.isChecked)
                dateA() else if (date.isChecked && descending.isChecked) dateD()
            else if (got.isChecked && ascending.isChecked) gotA()
                    else if (got.isChecked && descending.isChecked) gotD()
                        else if (expense.isChecked && ascending.isChecked) expenseA()
                            else expenseD() }
        sortAlert = sortAlertBuilder!!.create()
        sortAlert!!.show()
    }

    private fun gotA() {
        viewModel!!.allGotDataAscending.observe(
            this@MainActivity,
            { dataItems: List<DataItems?>? -> setDataItemsList(dataItems) })
    }

    private fun expenseA() {
        viewModel!!.allExpenseDataAscending.observe(
            this@MainActivity,
            { dataItems: List<DataItems?>? -> setDataItemsList(dataItems) })
    }

    private fun expenseD() {
        viewModel!!.allExpenseDataDescending.observe(
            this@MainActivity,
            { dataItems: List<DataItems?>? -> setDataItemsList(dataItems) })
    }

    private fun gotD() {
        viewModel!!.allGotDataDescending.observe(
            this@MainActivity,
            { dataItems: List<DataItems?>? -> setDataItemsList(dataItems) })
    }

    private fun dateA() {
        viewModel!!.allDataAscending.observe(
            this@MainActivity,
            { dataItems: List<DataItems?>? -> setDataItemsList(dataItems) })
    }

    private fun dateD() {
        viewModel!!.allDataDescending.observe(
            this@MainActivity,
            { dataItems: List<DataItems?>? -> setDataItemsList(dataItems) })
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val setAppToBackground = moveTaskToBack(true)
            if (!setAppToBackground) {
                val intent = Intent()
                intent.action = Intent.ACTION_MAIN
                intent.addCategory(Intent.CATEGORY_HOME)
                this.startActivity(intent)
            }
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun makeToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun setBackup(id: Int) {
        makeToast(getString(R.string.progress))
    }

    val backup: Unit
        get() {
            makeToast(getString(R.string.progress))
        }

    private fun go(): Pair<Int, Int> {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return Pair.create(height, width)
    }

    private fun setBackupFirebase(id: Int) {
        val database = "expense.database"
        val sd = Environment.getExternalStorageDirectory().path
        if (id == R.id.set_backup) {
            val permission =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permission == PackageManager.PERMISSION_GRANTED) {
                myDatabase.getDbINSTANCE(this).close()
                val db = getDatabasePath(database)
                val dbShm = File(db.parent, "$database-shm")
                val dbWal = File(db.parent, "$database-wal")
                val db2 = File(sd, database)
                val dbShm2 = File(db2.parent, "$database-shm")
                val dbWal2 = File(db2.parent, "$database-wal")
                try {
                    FileUtils.copyFile(db, db2)
                    FileUtils.copyFile(dbShm, dbShm2)
                    FileUtils.copyFile(dbWal, dbWal2)
                    makeToast("Saved " + sd + " " + getDatabasePath(database))
                    println(getDatabasePath(database).toString() + "can you ")
                } catch (e: Exception) {
                    Log.e("SAVEDB", e.toString())
                }
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please allow access to your storage",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Allow") {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), 0
                        )
                    }.show()
            }
        } else if (id == R.id.get_backup) {
            val permission =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permission == PackageManager.PERMISSION_GRANTED) {
                myDatabase.getDbINSTANCE(this).close()
                val db = File(sd, database)
                val dbShm = File(db.parent, "$database-shm")
                val dbWal = File(db.parent, "$database-wal")
                val db2 = getDatabasePath(database)
                val dbShm2 = File(db2.parent, "$database-shm")
                val dbWal2 = File(db2.parent, "$database-wal")
                try {
                    FileUtils.copyFile(db, db2)
                    FileUtils.copyFile(dbShm, dbShm2)
                    FileUtils.copyFile(dbWal, dbWal2)
                    makeToast("Get $sd")
                } catch (e: Exception) {
                    Log.e("RESTOREDB", e.toString())
                }
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please allow access to your storage",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Allow") {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), 0
                        )
                    }.show()
            }
        }
    } //    void settingData(){
    //        String database="expense.database";
    //        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    //        File db = getDatabasePath("expense.database");
    //        File dbShm = new File(db.getParent(), "expense.database-shm");
    //        File dbWal = new File(db.getParent(), "expense.database-wal");
    //        storageReference.putFile()
    //    }
}
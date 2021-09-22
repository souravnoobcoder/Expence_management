package com.example.expense_management.activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_management.activities.MainActivity
import com.example.expense_management.R
import com.example.expense_management.RecyclerViewAdapters.detailed_adapter
import com.example.expense_management.dataClasses.psfs.CHECK
import com.example.expense_management.dataClasses.psfs.DATA_ID
import com.example.expense_management.dataClasses.psfs.DETAIL_DATE
import com.example.expense_management.dataClasses.psfs.DETAIL_GROSS_MONEY_GOT
import com.example.expense_management.dataClasses.psfs.DETAIL_GROSS_MONEY_PAID
import com.example.expense_management.dataClasses.psfs.DETAIL_MONEY_EXPENSE
import com.example.expense_management.dataClasses.psfs.DETAIL_MONEY_EXPENSE_PURPOSE
import com.example.expense_management.dataClasses.psfs.DETAIL_MONEY_GOT
import com.example.expense_management.dataClasses.psfs.DETAIL_MONEY_GOT_PURPOSE
import com.example.expense_management.dataClasses.psfs.OUR_DATE
import com.example.expense_management.dataClasses.psfs.makeDate
import com.example.expense_management.dataClasses.psfs.setForAdapterIntent
import com.example.expense_management.dataClasses.psfs.setGrossMoney
import com.example.expense_management.database.DataItems
import com.example.expense_management.database.DataViewModel
import com.example.expense_management.database.myDatabase
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*

class DetailedData : AppCompatActivity() {
    private var dateId: Long = -1
    private var button: FloatingActionButton? = null
    private var gainRecycle: RecyclerView? = null
    private var expenseRecycle: RecyclerView? = null
    private var da: TextView? = null
    private var pa: TextView? = null
    private var go: TextView? = null
    private var date: String? = null
    private var grossGot: String? = null
    private var grossPaid: String? = null
    private var cheek: String? = null
    private var gotDescription: MutableList<String>? = null
    private var paidDescription: MutableList<String>? = null
    private var got: MutableList<Int>? = null
    private var paid: MutableList<Int>? = null
    private var dates: List<Long>? = null
    private var forGainAdapter: detailed_adapter? = null
    private var forExpenseAdapter: detailed_adapter? = null
    private var dataItems: DataItems? = null
    private var items: DataItems? = null
    private var adapterCheck = false
    private var aBoolean = false
    private var deletePosition = -1
    private var forEditIntent: Intent? = null
    private var dialogBuilder: AlertDialog.Builder? = null
    private var dateDailogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null
    private var dateDailog: AlertDialog? = null
    private var model: DataViewModel? = null
    private var changeDatePicker: MaterialDatePicker<Long>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.detailed_data)
        model = ViewModelProviders.of(this).get(DataViewModel::class.java)
        button = findViewById(R.id.add_while_watching)
        da = findViewById(R.id.date_of_detail)
        pa = findViewById(R.id.paid_amount)
        go = findViewById(R.id.gain_amount)
        gainRecycle = findViewById(R.id.gain_recyclerView)
        expenseRecycle = findViewById(R.id.expense_recyclerView)
        val intent = intent
        val builder = MaterialDatePicker.Builder.datePicker()
        changeDatePicker = builder.setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        changeDatePicker!!.addOnPositiveButtonClickListener { selection: Long ->
            CoroutineScope(Main).launch {
                dates = myDatabase.getDbINSTANCE(this@DetailedData).Dao().allDate()
                dateChange(selection)
            }
        }
        cheek = intent.getStringExtra(CHECK)
        if (cheek == "yes") {
            button?.visibility = View.VISIBLE
            dateId = intent.getLongExtra(DATA_ID, -1)
            println("yes its$dateId")
            CoroutineScope(IO).launch {
                dataItems = myDatabase.getDbINSTANCE(this@DetailedData).Dao().getRoww(dateId)
                CoroutineScope(Main).launch {  initializeLists()
                    date = makeDate(dataItems!!.date)
                    grossGot = java.lang.String.valueOf(dataItems!!.grossMoneyGot)
                    grossPaid = java.lang.String.valueOf(dataItems!!.grossMoneyExpense)
                    paid!!.addAll(dataItems!!.moneyExpense!!)
                    got!!.addAll(dataItems!!.moneyGot!!)
                    paidDescription!!.addAll(dataItems!!.moneyExpensePurposes!!)
                    gotDescription!!.addAll(dataItems!!.moneyGotPurposes!!)
                    iMain()

                }
            }
        } else {
            date = intent.getStringExtra(DETAIL_DATE)
            grossGot = intent.getStringExtra(DETAIL_GROSS_MONEY_GOT)
            grossPaid = intent.getStringExtra(DETAIL_GROSS_MONEY_PAID)
            got = intent.getIntegerArrayListExtra(DETAIL_MONEY_GOT)
            paid = intent.getIntegerArrayListExtra(DETAIL_MONEY_EXPENSE)
            gotDescription = intent.getStringArrayListExtra(DETAIL_MONEY_GOT_PURPOSE)
            paidDescription = intent.getStringArrayListExtra(DETAIL_MONEY_EXPENSE_PURPOSE)
            aBoolean = true
        }
        if(cheek!="yes"){
            da?.text = date
            go?.text = "Got : $grossGot"
            pa?.text = "Paid : $grossPaid"
            forExpenseAdapter = detailed_adapter(paid, paidDescription, cheek, false)
            forGainAdapter = detailed_adapter(got, gotDescription, cheek, true)
            gainRecycle?.layoutManager = LinearLayoutManager(this)
            expenseRecycle?.layoutManager = LinearLayoutManager(this)
            gainRecycle?.adapter = forGainAdapter
            expenseRecycle?.adapter = forExpenseAdapter
        }

        button?.setOnClickListener {
            val addNew = Intent(this@DetailedData, EditHandler::class.java)
            addNew.putExtra(DATA_ID, dateId)
            addNew.putExtra(OUR_DATE, date)
            addNew.putExtra(CHECK, true)
            startActivity(addNew)
            finish()
        }
        forGainAdapter?.setOnItemLongClickListener { integer, string, listPosition ->
            deletePosition = listPosition
            val intentBooleanPair: Pair<Intent, Boolean> = setForAdapterIntent(
                true, integer, string, listPosition,
                dateId, date, this@DetailedData
            )
            forEditIntent = intentBooleanPair.first
            adapterCheck = intentBooleanPair.second
            makeAlertDailogbBox(integer, string)
            dialog?.show()
        }
        forExpenseAdapter?.setOnItemLongClickListener { integer, string, listPosition ->
            deletePosition = listPosition
            val intentBooleanPair: Pair<Intent, Boolean> = setForAdapterIntent(
                false, integer, string,
                listPosition, dateId, date, this@DetailedData
            )
            forEditIntent = intentBooleanPair.first
            adapterCheck = intentBooleanPair.second
            makeAlertDailogbBox(integer, string)
            dialog?.show()
        }
        if (!aBoolean) {
            da?.setOnLongClickListener {
                changeDatePicker!!.show(supportFragmentManager, null)
                false
            }
        }
    }

    private fun initializeLists() {
        paid = ArrayList()
        got = ArrayList()
        paidDescription = ArrayList()
        gotDescription = ArrayList()
    }

    override fun onBackPressed() {
        if (aBoolean) {
            super.onBackPressed()
        } else {
            val intent = Intent(this@DetailedData, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun makeAlertDailogbBox(money: String, description: String) {
        dialogBuilder = AlertDialog.Builder(this@DetailedData)
        dialogBuilder!!.setTitle("Money : $money\n Description : $description")
            .setPositiveButton("Update") { _, _ ->
                startActivity(forEditIntent)
                finish()
            }.setNegativeButton("Delete") { _, _ ->
                CoroutineScope(IO).launch {
                    items = myDatabase.getDbINSTANCE(this@DetailedData).Dao().getRoww(dateId)
                   iMain()
                }
                Thread {
                    Thread {
                        items = myDatabase.getDbINSTANCE(this@DetailedData).Dao().getRoww(dateId)
                    }
                        .start()
                    try {
                        Thread.sleep(150)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    myDatabase.DELETE_INSTANCE()
                    if (adapterCheck) {
                        items!!.moneyGot!!.removeAt(deletePosition)
                        items!!.grossMoneyGot = setGrossMoney(items!!.moneyGot)
                        items!!.moneyGotPurposes!!.removeAt(deletePosition)
                    } else {
                        items!!.moneyExpense!!.removeAt(deletePosition)
                        items!!.grossMoneyExpense = setGrossMoney(items!!.moneyExpense)
                        items!!.moneyExpensePurposes!!.removeAt(deletePosition)
                    }
                    model!!.update(items)
                }.start()
                try {
                    Thread.sleep(250)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                val dataIntent = Intent(this@DetailedData, DetailedData::class.java)
                dataIntent.putExtra(DATA_ID, dateId)
                dataIntent.putExtra(CHECK, "yes")
                startActivity(dataIntent)
                finish()
            }
        dialog = dialogBuilder!!.create()
    }

    private fun changeDateAlert(date: Long) {
        dateDailogBuilder = AlertDialog.Builder(this@DetailedData)
        dateDailogBuilder!!.setTitle(makeDate(date) + " Already exists ")
            .setPositiveButton("Change") { _, _ ->
                changeDatePicker!!.show(
                    supportFragmentManager,
                    null
                )
            }
        dateDailog = dateDailogBuilder!!.create()
        dateDailog!!.show()
    }

    private fun dateChange(selection: Long) {
        CoroutineScope(Main).launch { launchingIntent(selection) }
    }

    private fun launchingIntent(selection: Long) {
        val get = Arrays.binarySearch(dates!!.toTypedArray(), selection)
        if (get < 0) {
            model!!.updateDate(dateId, selection)
            val dataIntent = Intent(this@DetailedData, DetailedData::class.java)
            dataIntent.putExtra(DATA_ID, selection)
            dataIntent.putExtra(CHECK, "yes")
            startActivity(dataIntent)
        } else {
            changeDateAlert(selection)
        }
    }
    private suspend fun iMain(){
        CoroutineScope(Main).launch {
            initializingAdapter()
        }
    }
    private suspend fun initializingAdapter(){
        da?.text = date
        go?.text = "Got : $grossGot"
        pa?.text = "Paid : $grossPaid"
        forExpenseAdapter = detailed_adapter(paid, paidDescription, cheek, false)
        forGainAdapter = detailed_adapter(got, gotDescription, cheek, true)
        gainRecycle?.layoutManager = LinearLayoutManager(this)
        expenseRecycle?.layoutManager = LinearLayoutManager(this)
        gainRecycle?.adapter = forGainAdapter
        expenseRecycle?.adapter = forExpenseAdapter
    }
    fun click(){
        forGainAdapter?.setOnItemLongClickListener { integer, string, listPosition ->
            deletePosition = listPosition
            val intentBooleanPair: Pair<Intent, Boolean> = setForAdapterIntent(
                true, integer, string, listPosition,
                dateId, date, this@DetailedData
            )
            forEditIntent = intentBooleanPair.first
            adapterCheck = intentBooleanPair.second
            makeAlertDailogbBox(integer, string)
            dialog?.show()
        }
        forExpenseAdapter?.setOnItemLongClickListener { integer, string, listPosition ->
            deletePosition = listPosition
            val intentBooleanPair: Pair<Intent, Boolean> = setForAdapterIntent(
                false, integer, string,
                listPosition, dateId, date, this@DetailedData
            )
            forEditIntent = intentBooleanPair.first
            adapterCheck = intentBooleanPair.second
            makeAlertDailogbBox(integer, string)
            dialog?.show()
        }
    }
}
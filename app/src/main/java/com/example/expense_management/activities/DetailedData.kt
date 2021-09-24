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
import com.example.expense_management.R
import com.example.expense_management.recyclerViewAdapters.DetailedAdapter
import com.example.expense_management.dataClasses.ConstantFuntions.CHECK
import com.example.expense_management.dataClasses.ConstantFuntions.DATA_ID
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_DATE
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_GROSS_MONEY_GOT
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_GROSS_MONEY_PAID
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_MONEY_EXPENSE
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_MONEY_EXPENSE_PURPOSE
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_MONEY_GOT
import com.example.expense_management.dataClasses.ConstantFuntions.DETAIL_MONEY_GOT_PURPOSE
import com.example.expense_management.dataClasses.ConstantFuntions.OUR_DATE
import com.example.expense_management.dataClasses.ConstantFuntions.makeDate
import com.example.expense_management.dataClasses.ConstantFuntions.setForAdapterIntent
import com.example.expense_management.dataClasses.ConstantFuntions.setGrossMoney
import com.example.expense_management.database.DataItems
import com.example.expense_management.database.DataViewModel
import com.example.expense_management.database.ExpenseDatabase
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
    private var forGainAdapter: DetailedAdapter? = null
    private var forExpenseAdapter: DetailedAdapter? = null
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
                dates = ExpenseDatabase.getDbINSTANCE(this@DetailedData)?.dao()?.allDate()
                dateChange(selection)
            }
        }
        cheek = intent.getStringExtra(CHECK)
        if (cheek == "yes") {
            button?.visibility = View.VISIBLE
            dateId = intent.getLongExtra(DATA_ID, -1)
            println("yes its$dateId")
            CoroutineScope(IO).launch {
                dataItems = ExpenseDatabase.getDbINSTANCE(this@DetailedData)?.dao()
                    ?.getRoww(dateId)
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
            initializingAdapter()
            click()
        }

        button?.setOnClickListener {
            val addNew = Intent(this@DetailedData, EditHandler::class.java)
            addNew.putExtra(DATA_ID, dateId)
            addNew.putExtra(OUR_DATE, date)
            addNew.putExtra(CHECK, true)
            startActivity(addNew)
            finish()
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
                    items = ExpenseDatabase.getDbINSTANCE(this@DetailedData)?.dao()?.getRoww(dateId)
                   iMain()
                }
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
    private fun iMain(){
        CoroutineScope(Main).launch {
            initializingAdapter()
            click()
        }
    }
    private fun initializingAdapter(){
        da?.text = date
        go?.text = "Got : $grossGot"
        pa?.text = "Paid : $grossPaid"
        forExpenseAdapter = cheek?.let { DetailedAdapter(paid, paidDescription, it, false) }
        forGainAdapter = cheek?.let { DetailedAdapter(got, gotDescription, it, true) }
        gainRecycle?.layoutManager = LinearLayoutManager(this)
        expenseRecycle?.layoutManager = LinearLayoutManager(this)
        gainRecycle?.adapter = forGainAdapter
        expenseRecycle?.adapter = forExpenseAdapter
    }
    private fun click(){
        forGainAdapter?.setOnItemLongClickListener(object : DetailedAdapter.OnItemLongClickListener{
            override fun onItemLongClicked(integer: String, string: String, position: Int) {
                deletePosition = position
                val intentBooleanPair: Pair<Intent, Boolean> = setForAdapterIntent(
                    true, integer, string, position,
                    dateId, date, this@DetailedData
                )
                forEditIntent = intentBooleanPair.first
                adapterCheck = intentBooleanPair.second
                makeAlertDailogbBox(integer, string)
                dialog?.show()
            }
        })
        forExpenseAdapter?.setOnItemLongClickListener(object : DetailedAdapter.OnItemLongClickListener{
            override fun onItemLongClicked(integer: String, string: String, position: Int) {
                deletePosition = position
                val intentBooleanPair: Pair<Intent, Boolean> = setForAdapterIntent(
                    false, integer, string,
                    position, dateId, date, this@DetailedData
                )
                forEditIntent = intentBooleanPair.first
                adapterCheck = intentBooleanPair.second
                makeAlertDailogbBox(integer, string)
                dialog?.show()
            }
        })
    }
}